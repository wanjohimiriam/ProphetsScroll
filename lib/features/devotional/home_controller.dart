// lib/features/controllers/home_controller.dart

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';

import 'package:prophets_scroll/core/router/app_router.dart';
import 'package:prophets_scroll/core/constants/constants.dart';
import 'package:prophets_scroll/features/devotional/DevotionalRepository.dart';
import 'package:prophets_scroll/features/devotional/devotional_model.dart';
import 'package:prophets_scroll/features/devotional/song_model.dart';
import 'package:prophets_scroll/features/language/views/language_picker_sheet.dart';

class HomeController extends GetxController {
  final DevotionalRepository _repo;
  HomeController(this._repo);

  // ── Observables ─────────────────────────────────────────────
  final devotional         = Rxn<Devotional>();
  final song               = Rxn<DevotionalSong>();
  final recommendations    = <Devotional>[].obs;
  final recentDevotionals  = <Devotional>[].obs;
  final archiveDevotionals = <Devotional>[].obs;
  final selectedLang       = AppStrings.defaultLang.obs;
  final isLoading          = false.obs;
  final isSongPlaying      = false.obs;
  final userName           = ''.obs;

  // ── Date filter observables ──────────────────────────────────
  final selectedYear  = DateTime.now().year.obs;
  final selectedMonth = DateTime.now().month.obs;
  final selectedDay   = DateTime.now().day.obs;

  // ── Available date ranges ────────────────────────────────────
  static final int _startYear = AppConstants.archiveStart.year;

  List<int> get availableYears => List.generate(
        DateTime.now().year - _startYear + 1,
        (i) => DateTime.now().year - i,
      );

  List<int> get availableMonths {
    final max = selectedYear.value == DateTime.now().year
        ? DateTime.now().month
        : 12;
    return List.generate(max, (i) => i + 1);
  }

  List<int> get availableDays {
    final now = DateTime.now();
    final inMonth =
        DateUtils.getDaysInMonth(selectedYear.value, selectedMonth.value);
    final max = (selectedYear.value == now.year &&
            selectedMonth.value == now.month)
        ? now.day
        : inMonth;
    return List.generate(max, (i) => i + 1);
  }

  // ── Computed display strings ─────────────────────────────────
  String get todayFormatted =>
      DateFormat('EEEE, d MMM y').format(DateTime.now());

  String get todayShort => DateFormat('MMM d').format(DateTime.now());

  String get selectedMonthName => DateFormat('MMMM')
      .format(DateTime(selectedYear.value, selectedMonth.value));

  String get selectedDayLabel {
    final d = DateTime(
        selectedYear.value, selectedMonth.value, selectedDay.value);
    return DateFormat('d EEE').format(d);
  }

  int get dayNumber =>
      DateTime.now().difference(AppConstants.archiveStart).inDays + 1;

  bool get isViewingToday {
    final now = DateTime.now();
    return selectedYear.value == now.year &&
        selectedMonth.value == now.month &&
        selectedDay.value == now.day;
  }

  // ── Lifecycle ────────────────────────────────────────────────
  @override
  void onInit() {
    super.onInit();
    loadSelected();
  }

  // ── Load data ────────────────────────────────────────────────
  Future<void> loadSelected() async {
    isLoading(true);
    try {
      final date = DateTime(
          selectedYear.value, selectedMonth.value, selectedDay.value);

      final main = await Future.wait([
        _repo.getDevotionalByDate(date: date, lang: selectedLang.value),
        _repo.getTodaysSong(lang: selectedLang.value),
      ]);

      devotional.value = main[0] as Devotional;
      song.value       = main[1] as DevotionalSong?;

      if (devotional.value != null) {
        final extra = await Future.wait([
          _repo.getRecommendations(
            themes:    devotional.value!.themes,
            excludeId: devotional.value!.id,
            limit:     2,
          ),
          _repo.getRecent(
            lang:      selectedLang.value,
            excludeId: devotional.value!.id,
            limit:     12,
          ),
          _repo.getArchiveSample(
            lang:  selectedLang.value,
            limit: 6,
          ),
        ]);

        recommendations.value    = extra[0];
        recentDevotionals.value  = extra[1];
        archiveDevotionals.value = extra[2];
      }
    } catch (e) {
      Get.snackbar(
        'Oops',
        'Could not load devotional for that date.',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.white,
        colorText: Colors.black87,
      );
    } finally {
      isLoading(false);
    }
  }

  // ── Date filter setters ──────────────────────────────────────
  void onYearSelected(int year) {
    selectedYear(year);
    final maxMonth =
        year == DateTime.now().year ? DateTime.now().month : 12;
    if (selectedMonth.value > maxMonth) selectedMonth(maxMonth);
    _clampDay();
    loadSelected();
  }

  void onMonthSelected(int month) {
    selectedMonth(month);
    _clampDay();
    loadSelected();
  }

  void onDaySelected(int day) {
    selectedDay(day);
    loadSelected();
  }

  void _clampDay() {
    if (selectedDay.value > availableDays.length) {
      selectedDay(availableDays.length);
    }
  }

  // ── Language ─────────────────────────────────────────────────
  void switchLanguage(String code) {
    if (selectedLang.value == code) return;
    selectedLang(code);
    loadSelected();
  }

  void openLanguagePicker() {
    Get.bottomSheet(
      LanguagePickerSheet(
        currentLang: selectedLang.value,
        onSelect: switchLanguage,
      ),
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      isScrollControlled: true,
    );
  }

  // ── Navigation ───────────────────────────────────────────────
  void openReader() {
    if (devotional.value == null) return;
    Get.toNamed(AppRoutes.reader,
        arguments: {'devotional': devotional.value});
  }

  void openDevotionalById(String id) {
    Get.toNamed(AppRoutes.reader, arguments: {'id': id});
  }

  // alias kept so existing call-sites don't break
  void openArchivedDevotional(String id) => openDevotionalById(id);

  void openMusicPlayer() {
    if (song.value == null) return;
    Get.toNamed(AppRoutes.music, arguments: {'song': song.value});
  }

  void openRecommendations() {
    Get.toNamed(AppRoutes.recommendations, arguments: {
      'themes':      devotional.value?.themes ?? [],
      'sourceTitle': devotional.value?.title  ?? '',
    });
  }

  void openArchive() => Get.toNamed(AppRoutes.archive);

  // ── Song mini-player ─────────────────────────────────────────
  void toggleSong() => isSongPlaying.toggle();
}