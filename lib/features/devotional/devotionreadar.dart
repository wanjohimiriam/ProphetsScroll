// lib/features/devotional/reader/devotional_reader_page.dart

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';

import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';
import 'package:prophets_scroll/features/controllers/reader_controller.dart';

class DevotionalReaderPage extends StatelessWidget {
  const DevotionalReaderPage({super.key});

  @override
  Widget build(BuildContext context) {
    final ctrl = Get.find<ReaderController>();

    return Scaffold(
      backgroundColor: Colors.white,
      body: Obx(() {
        if (ctrl.isLoading.value) {
          return const Center(
            child: CircularProgressIndicator(color: AppColors.accent),
          );
        }
        final dev = ctrl.devotional.value;
        if (dev == null) {
          return const Center(child: Text('Devotional not found'));
        }
        return Column(
          children: [
            // Fixed top navigation
            _TopNav(ctrl: ctrl),
            // Reading progress bar
            _ProgressBar(ctrl: ctrl),
            // Scrollable body
            Expanded(
              child: SingleChildScrollView(
                controller: ctrl.scrollController,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _HeroImage(ctrl: ctrl),
                    _ReaderBody(ctrl: ctrl),
                    const SizedBox(height: 16),
                  ],
                ),
              ),
            ),
            // Fixed bottom action bar
            _BottomBar(ctrl: ctrl),
          ],
        );
      }),
    );
  }
}

// ─── Top navigation ───────────────────────────────────────────

class _TopNav extends StatelessWidget {
  final ReaderController ctrl;
  const _TopNav({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      color: AppColors.navy,
      child: SafeArea(
        bottom: false,
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
          child: Row(
            children: [
              // Back
              GestureDetector(
                onTap: Get.back,
                child: Icon(Icons.arrow_back_ios_new_rounded,
                    color: AppColors.accent, size: 18),
              ),
              const SizedBox(width: 10),
              // Title (truncated)
              Expanded(
                child: Obx(() => Text(
                      ctrl.devotional.value?.title ?? '',
                      style: AppTextStyles.body.copyWith(
                        color: Colors.white,
                        fontWeight: FontWeight.w500,
                        fontSize: 13,
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    )),
              ),
              const SizedBox(width: 8),
              // Font size toggle
              GestureDetector(
                onTap: ctrl.cycleFontSize,
                child: Padding(
                  padding: const EdgeInsets.all(6),
                  child: Obx(() => Text(
                        'A${ctrl.fontSizeLabel}',
                        style: AppTextStyles.caption.copyWith(
                          color: Colors.white.withOpacity(0.75),
                          fontWeight: FontWeight.w600,
                          fontSize: 12,
                        ),
                      )),
                ),
              ),
              const SizedBox(width: 4),
              // Bookmark
              GestureDetector(
                onTap: ctrl.toggleBookmark,
                child: Padding(
                  padding: const EdgeInsets.all(6),
                  child: Obx(() => Icon(
                        ctrl.isBookmarked.value
                            ? Icons.bookmark_rounded
                            : Icons.bookmark_border_rounded,
                        color: ctrl.isBookmarked.value
                            ? AppColors.accent
                            : Colors.white.withOpacity(0.75),
                        size: 20,
                      )),
                ),
              ),
              const SizedBox(width: 4),
              // Share
              GestureDetector(
                onTap: ctrl.share,
                child: Padding(
                  padding: const EdgeInsets.all(6),
                  child: Icon(Icons.ios_share_rounded,
                      color: Colors.white.withOpacity(0.75), size: 18),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ─── Reading progress bar ─────────────────────────────────────

class _ProgressBar extends StatelessWidget {
  final ReaderController ctrl;
  const _ProgressBar({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() => LinearProgressIndicator(
          value: ctrl.readingProgress.value,
          minHeight: 3,
          backgroundColor: AppColors.cardBorder,
          valueColor: const AlwaysStoppedAnimation<Color>(AppColors.accent),
        ));
  }
}

// ─── Hero image ───────────────────────────────────────────────

class _HeroImage extends StatelessWidget {
  final ReaderController ctrl;
  const _HeroImage({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 200,
      width: double.infinity,
      child: Stack(
        fit: StackFit.expand,
        children: [
          // Background — image asset with colour fallback
          Image.asset(
            'assets/devotional/devotional.png',
            fit: BoxFit.cover,
            errorBuilder: (_, __, ___) => Container(
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                  colors: [AppColors.navy, AppColors.navy2],
                ),
              ),
              child: Center(
                child: Text('📜',
                    style: TextStyle(
                        fontSize: 48,
                        color: Colors.white.withOpacity(0.2))),
              ),
            ),
          ),
          // Gradient overlay — fades to white at bottom so text reads naturally
          const DecoratedBox(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
                colors: [
                  Colors.transparent,
                  Color(0xCCFFFFFF),
                ],
                stops: [0.5, 1.0],
              ),
            ),
          ),
          // Badges bottom-left
          Obx(() {
            final dev = ctrl.devotional.value;
            if (dev == null) return const SizedBox.shrink();
            return Positioned(
              left: 14,
              bottom: 12,
              child: Row(
                children: [
                  _Badge(
                      label: 'Day ${ctrl.dayNumber}',
                      color: AppColors.accent),
                  const SizedBox(width: 6),
                  _Badge(label: dev.dateFormatted),
                ],
              ),
            );
          }),
        ],
      ),
    );
  }
}

class _Badge extends StatelessWidget {
  final String label;
  final Color? color;
  const _Badge({required this.label, this.color});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 4),
      decoration: BoxDecoration(
        color: color ?? Colors.black.withOpacity(0.45),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Text(
        label,
        style: AppTextStyles.caption.copyWith(
          color: Colors.white,
          fontSize: 10,
          fontWeight: FontWeight.w500,
        ),
      ),
    );
  }
}

// ─── Main reader body ─────────────────────────────────────────

class _ReaderBody extends StatelessWidget {
  final ReaderController ctrl;
  const _ReaderBody({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final dev = ctrl.devotional.value;
      if (dev == null) return const SizedBox.shrink();
      final fontSize = ctrl.fontSize;

      return Padding(
        padding: const EdgeInsets.fromLTRB(16, 14, 16, 0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Tag
            Text(
              ctrl.isToday ? "TODAY'S DEVOTIONAL" : "DEVOTIONAL",
              style: AppTextStyles.label.copyWith(
                color: AppColors.accent,
                letterSpacing: 0.8,
              ),
            ),
            const SizedBox(height: 5),
            // Title
            Text(
              dev.title,
              style: AppTextStyles.h1.copyWith(
                fontSize: fontSize + 4,
                height: 1.2,
              ),
            ),
            const SizedBox(height: 6),
            // Reference row
            Row(
              children: [
                Container(
                    width: 4,
                    height: 4,
                    decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: AppColors.primary)),
                const SizedBox(width: 6),
                Text(
                  dev.verseReference,
                  style: AppTextStyles.body.copyWith(
                    color: AppColors.primary,
                    fontWeight: FontWeight.w500,
                    fontSize: 12,
                  ),
                ),
                Text(
                  '  ·  ${dev.dateFormatted}',
                  style: AppTextStyles.caption
                      .copyWith(color: AppColors.textMuted),
                ),
              ],
            ),
            const SizedBox(height: 14),
            // Scripture quote block
            _QuoteBlock(text: dev.verseText, reference: dev.verseReference),
            const SizedBox(height: 14),
            // Body paragraphs — SelectableText for highlight support
            ...dev.paragraphs.asMap().entries.map((entry) {
              final isLast = entry.key == dev.paragraphs.length - 1;
              return Padding(
                padding: EdgeInsets.only(bottom: isLast ? 0 : 12),
                child: _SelectableParagraph(
                  text: entry.value,
                  fontSize: fontSize,
                  highlights: ctrl.highlightsForParagraph(entry.key),
                ),
              );
            }),
            const SizedBox(height: 16),
            // Prayer section
            _PrayerSection(dev: dev, fontSize: fontSize),
            const SizedBox(height: 8),
          ],
        ),
      );
    });
  }
}

// ─── Scripture quote block ────────────────────────────────────

class _QuoteBlock extends StatelessWidget {
  final String text;
  final String reference;
  const _QuoteBlock({required this.text, required this.reference});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 10),
      decoration: BoxDecoration(
        color: const Color(0xFFFFEBE8),
        border: const Border(
          left: BorderSide(color: AppColors.accent, width: 3),
        ),
        borderRadius: const BorderRadius.horizontal(right: Radius.circular(10)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SelectableText(
            text,
            style: AppTextStyles.body.copyWith(
              fontStyle: FontStyle.italic,
              color: AppColors.goldText,
              fontSize: 13,
              height: 1.6,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            '— $reference',
            style: AppTextStyles.caption.copyWith(
              color: AppColors.accent,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }
}

// ─── Selectable paragraph ─────────────────────────────────────
// SelectableText allows highlight + copy but never edit.

class _SelectableParagraph extends StatelessWidget {
  final String text;
  final double fontSize;
  final List<TextRange> highlights;

  const _SelectableParagraph({
    required this.text,
    required this.fontSize,
    this.highlights = const [],
  });

  @override
  Widget build(BuildContext context) {
    return SelectableText.rich(
      _buildSpans(text, highlights, fontSize),
      contextMenuBuilder: (context, editableTextState) {
        return _HighlightContextMenu(editableTextState: editableTextState);
      },
    );
  }

  TextSpan _buildSpans(
      String text, List<TextRange> highlights, double fontSize) {
    if (highlights.isEmpty) {
      return TextSpan(
        text: text,
        style: _paraStyle(fontSize),
      );
    }

    final spans = <InlineSpan>[];
    int cursor = 0;

    final sorted = List<TextRange>.from(highlights)
      ..sort((a, b) => a.start.compareTo(b.start));

    for (final h in sorted) {
      if (h.start > cursor) {
        spans.add(TextSpan(
            text: text.substring(cursor, h.start),
            style: _paraStyle(fontSize)));
      }
      if (h.end <= text.length) {
        spans.add(TextSpan(
          text: text.substring(h.start, h.end),
          style: _paraStyle(fontSize).copyWith(
            backgroundColor: const Color(0xFFFFEB3B).withOpacity(0.45),
          ),
        ));
        cursor = h.end;
      }
    }

    if (cursor < text.length) {
      spans.add(TextSpan(
          text: text.substring(cursor), style: _paraStyle(fontSize)));
    }

    return TextSpan(children: spans);
  }

  TextStyle _paraStyle(double fontSize) => AppTextStyles.body.copyWith(
        fontSize: fontSize,
        height: 1.75,
        color: AppColors.textSecond,
      );
}

// ─── Custom context menu shown on text selection ──────────────

class _HighlightContextMenu extends StatelessWidget {
  final EditableTextState editableTextState;
  const _HighlightContextMenu({required this.editableTextState});

  @override
  Widget build(BuildContext context) {
    final anchor = editableTextState.contextMenuAnchors.primaryAnchor;
    return AdaptiveTextSelectionToolbar.buttonItems(
      anchors: editableTextState.contextMenuAnchors,
      buttonItems: [
        // Highlight
        ContextMenuButtonItem(
          label: '🟡 Highlight',
          onPressed: () {
            final selection = editableTextState.textEditingValue.selection;
            if (selection.isValid && !selection.isCollapsed) {
              // Access controller and save highlight
              if (Get.isRegistered<ReaderController>()) {
                Get.find<ReaderController>().addHighlight(selection);
              }
            }
            ContextMenuController.removeAny();
          },
        ),
        // Copy
        ContextMenuButtonItem(
          label: 'Copy',
          onPressed: () {
            final text = editableTextState.textEditingValue.selection
                .textInside(editableTextState.textEditingValue.text);
            Clipboard.setData(ClipboardData(text: text));
            ContextMenuController.removeAny();
            Get.snackbar(
              'Copied',
              'Text copied to clipboard',
              snackPosition: SnackPosition.BOTTOM,
              duration: const Duration(seconds: 2),
              backgroundColor: Colors.white,
              colorText: AppColors.textPrimary,
            );
          },
        ),
        // Share
        ContextMenuButtonItem(
          label: 'Share',
          onPressed: () {
            ContextMenuController.removeAny();
            if (Get.isRegistered<ReaderController>()) {
              Get.find<ReaderController>().share();
            }
          },
        ),
      ],
    );
  }
}

// ─── Prayer section ───────────────────────────────────────────

class _PrayerSection extends StatelessWidget {
  final dynamic dev;
  final double fontSize;
  const _PrayerSection({required this.dev, required this.fontSize});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: AppColors.primary.withOpacity(0.05),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: AppColors.primary.withOpacity(0.12)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Text('🙏', style: TextStyle(fontSize: 16)),
              const SizedBox(width: 8),
              Text(
                'A Prayer for Today',
                style: AppTextStyles.body.copyWith(
                  color: AppColors.navy,
                  fontWeight: FontWeight.w600,
                  fontSize: 13,
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          SelectableText(
            // Use last paragraph as prayer if available
            dev.paragraphs.length > 1
                ? dev.paragraphs.last
                : 'Lord, let your light guide every step I take today...',
            style: AppTextStyles.body.copyWith(
              fontSize: fontSize - 1,
              height: 1.7,
              color: AppColors.textSecond,
              fontStyle: FontStyle.italic,
            ),
          ),
        ],
      ),
    );
  }
}

// ─── Bottom action bar ────────────────────────────────────────

class _BottomBar extends StatelessWidget {
  final ReaderController ctrl;
  const _BottomBar({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(top: BorderSide(color: AppColors.cardBorder)),
      ),
      padding: const EdgeInsets.fromLTRB(14, 10, 14, 14),
      child: SafeArea(
        top: false,
        child: Row(
          children: [
            _BarBtn(
              icon: Icons.music_note_rounded,
              label: 'Song',
              onTap: ctrl.openSong,
            ),
            const SizedBox(width: 8),
            Obx(() => _BarBtn(
                  icon: ctrl.isRead.value
                      ? Icons.check_circle_rounded
                      : Icons.check_circle_outline_rounded,
                  label: ctrl.isRead.value ? 'Read ✓' : 'Mark Read',
                  isPrimary: true,
                  onTap: ctrl.markAsRead,
                )),
            const SizedBox(width: 8),
            _BarBtn(
              icon: Icons.edit_note_rounded,
              label: 'Save Note',
              onTap: ctrl.saveToNotes,
            ),
            const SizedBox(width: 8),
            _BarBtn(
              icon: Icons.ios_share_rounded,
              label: 'Share',
              onTap: ctrl.share,
            ),
          ],
        ),
      ),
    );
  }
}

class _BarBtn extends StatelessWidget {
  final IconData icon;
  final String label;
  final bool isPrimary;
  final VoidCallback onTap;

  const _BarBtn({
    required this.icon,
    required this.label,
    required this.onTap,
    this.isPrimary = false,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 8),
          decoration: BoxDecoration(
            color: isPrimary ? AppColors.accent : Colors.transparent,
            borderRadius: BorderRadius.circular(10),
            border: isPrimary
                ? null
                : Border.all(color: AppColors.cardBorder),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(icon,
                  size: 18,
                  color: isPrimary ? Colors.white : AppColors.textSecond),
              const SizedBox(height: 3),
              Text(
                label,
                style: AppTextStyles.caption.copyWith(
                  fontSize: 9,
                  color: isPrimary ? Colors.white : AppColors.textSecond,
                  fontWeight:
                      isPrimary ? FontWeight.w600 : FontWeight.w400,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}