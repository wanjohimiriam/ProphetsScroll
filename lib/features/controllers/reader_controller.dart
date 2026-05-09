// lib/features/devotional/reader/reader_controller.dart

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:share_plus/share_plus.dart';

import 'package:prophets_scroll/core/router/app_router.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/constants/constants.dart';
import 'package:prophets_scroll/features/devotional/DevotionalRepository.dart';
import 'package:prophets_scroll/features/devotional/devotional_model.dart';

class ReaderController extends GetxController {
  final DevotionalRepository _repo;
  ReaderController(this._repo);

  // ── State ──────────────────────────────────────────────────
  final devotional  = Rxn<Devotional>();
  final isLoading   = false.obs;
  final isRead      = false.obs;
  final isBookmarked = false.obs;

  // Reading progress (0.0 – 1.0) driven by scroll position
  final readingProgress = 0.0.obs;

  // Font size cycling: small → medium → large
  final _fontSizeIndex = 1.obs;
  static const _fontSizes = [13.0, 15.0, 17.0];
  static const _fontSizeLabels = ['−', '', '+'];

  double get fontSize => _fontSizes[_fontSizeIndex.value];
  String get fontSizeLabel => _fontSizeLabels[_fontSizeIndex.value];

  // Scroll controller — used to calculate reading progress
  final scrollController = ScrollController();

  // ── Computed ───────────────────────────────────────────────
  bool get isToday {
    final d = devotional.value;
    if (d == null) return false;
    final now = DateTime.now();
    return d.date.year == now.year &&
        d.date.month == now.month &&
        d.date.day == now.day;
  }

  int get dayNumber =>
      DateTime.now().difference(AppConstants.archiveStart).inDays + 1;

  // ── Lifecycle ──────────────────────────────────────────────
  @override
  void onInit() {
    super.onInit();
    _loadArguments();
    scrollController.addListener(_onScroll);
  }

  @override
  void onClose() {
    scrollController.dispose();
    super.onClose();
  }

  // ── Load devotional from navigation arguments ──────────────
  void _loadArguments() {
    final args = Get.arguments as Map<String, dynamic>?;
    if (args == null) return;

    if (args.containsKey('devotional')) {
      // Passed directly from home page (today's devotional)
      devotional.value = args['devotional'] as Devotional;
      isRead.value = devotional.value!.isRead;
    } else if (args.containsKey('id')) {
      // Load by ID (from archive, recommendations, recent grid)
      _loadById(args['id'] as String);
    }
  }

  Future<void> _loadById(String id) async {
    isLoading(true);
    try {
      devotional.value = await _repo.getById(id);
      if (devotional.value != null) {
        isRead.value = devotional.value!.isRead;
      }
    } catch (_) {
      Get.snackbar(
        'Oops',
        'Could not load this devotional.',
        snackPosition: SnackPosition.BOTTOM,
      );
    } finally {
      isLoading(false);
    }
  }

  // ── Scroll → progress ──────────────────────────────────────
  void _onScroll() {
    final max = scrollController.position.maxScrollExtent;
    if (max <= 0) return;
    final progress =
        (scrollController.offset / max).clamp(0.0, 1.0);
    readingProgress.value = progress;

    // Auto-mark as read when 80% scrolled
    if (progress >= 0.8 && !isRead.value) {
      markAsRead();
    }
  }

  // ── Actions ────────────────────────────────────────────────
  void cycleFontSize() {
    _fontSizeIndex.value = (_fontSizeIndex.value + 1) % _fontSizes.length;
  }

  void toggleBookmark() {
    isBookmarked.toggle();
    HapticFeedback.lightImpact();
  }

  Future<void> markAsRead() async {
    if (isRead.value) return;
    isRead(true);
    HapticFeedback.lightImpact();
    if (devotional.value != null) {
      await _repo.markAsRead(devotional.value!.id);
    }
  }

  // ── Highlights ─────────────────────────────────────────────
  // Store highlighted text strings that are applied across all paragraphs
  final _globalHighlights = <String>[].obs;

  void addHighlight(TextSelection selection) {
    final dev = devotional.value;
    if (dev == null) return;
    // Extract the selected text from the entire document
    final fullText = dev.paragraphs.join('\n');
    final selectedText = selection.textInside(fullText);
    
    if (selectedText.trim().isNotEmpty && !_globalHighlights.contains(selectedText)) {
      _globalHighlights.add(selectedText);
      HapticFeedback.selectionClick();
      Get.snackbar(
        'Highlighted',
        '"${selectedText.length > 40 ? '${selectedText.substring(0, 40)}…' : selectedText}"',
        snackPosition: SnackPosition.BOTTOM,
        duration: const Duration(seconds: 2),
        backgroundColor: Colors.white,
        colorText: AppColors.textPrimary,
      );
    }
  }

  /// Returns TextRanges within a paragraph that match any global highlights
  List<TextRange> highlightsForParagraph(int index) {
    final dev = devotional.value;
    if (dev == null || index >= dev.paragraphs.length) return [];
    
    final paragraphText = dev.paragraphs[index];
    final ranges = <TextRange>[];
    
    for (final highlighted in _globalHighlights) {
      int searchStart = 0;
      while (true) {
        final foundIndex = paragraphText.indexOf(highlighted, searchStart);
        if (foundIndex == -1) break;
        ranges.add(TextRange(start: foundIndex, end: foundIndex + highlighted.length));
        searchStart = foundIndex + highlighted.length;
      }
    }
    
    return ranges;
  }

  // ── Navigation ─────────────────────────────────────────────
  void openSong() {
    final songId = devotional.value?.linkedSongId;
    if (songId == null) return;
    Get.toNamed(AppRoutes.music,
        arguments: {'songId': songId});
  }

  void saveToNotes() {
    final dev = devotional.value;
    if (dev == null) return;
    Get.toNamed(
      AppRoutes.saveNote,
      arguments: {
        'devotionalId': dev.id,
        'lang':         dev.lang,
        'title':        dev.title,
        'verseRef':     dev.verseReference,
      },
    );
  }

  void share() {
    final dev = devotional.value;
    if (dev == null) return;
    Share.share(
      '📜 ${dev.title}\n\n${dev.verseText}\n\n${dev.preview}\n\n— The Prophets Scroll',
      subject: dev.title,
    );
  }
}