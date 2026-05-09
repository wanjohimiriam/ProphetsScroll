// lib/features/devotional/devotional_page.dart

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';
import 'package:prophets_scroll/core/constants/constants.dart';
import 'package:prophets_scroll/features/controllers/home_controller.dart';
import 'package:prophets_scroll/features/devotional/devotional_model.dart';
import 'package:prophets_scroll/features/devotional/widgets/devotional_hero_card.dart';
import 'package:prophets_scroll/features/devotional/widgets/recommendation_card.dart';

class DevotionalPage extends StatelessWidget {
  const DevotionalPage({super.key});

  @override
  Widget build(BuildContext context) {
    final ctrl = Get.find<HomeController>();

    return Scaffold(
      backgroundColor: AppColors.surface,
      body: Obx(() {
        if (ctrl.isLoading.value) {
          return const Center(
            child: CircularProgressIndicator(color: AppColors.primary),
          );
        }
        return CustomScrollView(
          slivers: [
            _AppBar(ctrl: ctrl),
            SliverToBoxAdapter(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _HeroSection(ctrl: ctrl),
                  _RecommendationsSection(ctrl: ctrl),
                  _RecentGridSection(ctrl: ctrl),
                  _ArchiveSection(ctrl: ctrl),
                  const SizedBox(height: 32),
                ],
              ),
            ),
          ],
        );
      }),
    );
  }
}

// ─── App Bar ──────────────────────────────────────────────────

class _AppBar extends StatelessWidget {
  final HomeController ctrl;
  const _AppBar({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return SliverAppBar(
      pinned: true,
      expandedHeight: 172,
      backgroundColor: Colors.white,
      automaticallyImplyLeading: false,
      flexibleSpace: FlexibleSpaceBar(
        background: SafeArea(
          child: Padding(
            padding: const EdgeInsets.fromLTRB(14, 10, 14, 12),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // Blue container for app title
                Container(
                  width: double.infinity,
                  padding: const EdgeInsets.symmetric(
                      horizontal: 14, vertical: 12),
                  decoration: BoxDecoration(
                    color: AppColors.primary,
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // ── App title ──
                      _AppTitleA(),
                      const SizedBox(height: 12),
                      // ── Greeting + language ──
                      Row(
                        children: [
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Obx(() => Text(
                                      'Good morning, ${ctrl.userName.value} 🌅',
                                      style: AppTextStyles.caption.copyWith(
                                          color: Colors.white.withOpacity(0.7)),
                                    )),
                                const SizedBox(height: 1),
                                Text(ctrl.todayFormatted,
                                    style: AppTextStyles.navyTitle.copyWith(
                                        color: Colors.white)),
                              ],
                            ),
                          ),
                          _LangButton(ctrl: ctrl),
                        ],
                      ),
                      const SizedBox(height: 10),
                      // ── Date filters ──
                      _DateFilterRow(ctrl: ctrl),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// ─── Option A: icon tile + "THE" micro-label + "Prophets Scroll" ─

class _AppTitleA extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        // Orange icon tile
        Container(
          width: 36,
          height: 36,
          decoration: BoxDecoration(
            color: AppColors.accent,
            borderRadius: BorderRadius.circular(9),
          ),
          child: const Center(
            child: Text('📜', style: TextStyle(fontSize: 18)),
          ),
        ),
        const SizedBox(width: 10),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'THE',
              style: AppTextStyles.caption.copyWith(
                color: Colors.white.withOpacity(0.45),
                fontSize: 9,
                fontWeight: FontWeight.w600,
                letterSpacing: 2.5,
              ),
            ),
            const SizedBox(height: 1),
            RichText(
              text: TextSpan(
                style: const TextStyle(
                  fontSize: 19,
                  fontWeight: FontWeight.w700,
                  letterSpacing: 0.2,
                  height: 1.1,
                ),
                children: [
                  const TextSpan(
                    text: 'Prophets ',
                    style: TextStyle(color: Colors.white),
                  ),
                  TextSpan(
                    text: 'Scroll',
                    style: TextStyle(color: AppColors.accent),
                  ),
                ],
              ),
            ),
          ],
        ),
      ],
    );
  }
}

// ─── Language button ──────────────────────────────────────────

class _LangButton extends StatelessWidget {
  final HomeController ctrl;
  const _LangButton({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final lang = AppConstants.languageByCode(ctrl.selectedLang.value);
      return GestureDetector(
        onTap: ctrl.openLanguagePicker,
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
          decoration: BoxDecoration(
            color: Colors.white.withOpacity(0.15),
            border: Border.all(color: Colors.white.withOpacity(0.3)),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(lang.flag, style: const TextStyle(fontSize: 14)),
              const SizedBox(width: 5),
              Text(
                lang.code.toUpperCase(),
                style: AppTextStyles.caption.copyWith(
                  color: Colors.white,
                  fontWeight: FontWeight.w600,
                ),
              ),
              const SizedBox(width: 4),
              Icon(Icons.keyboard_arrow_down_rounded,
                  color: Colors.white.withOpacity(0.7), size: 14),
            ],
          ),
        ),
      );
    });
  }
}

// ─── Date filter row ──────────────────────────────────────────

class _DateFilterRow extends StatelessWidget {
  final HomeController ctrl;
  const _DateFilterRow({required this.ctrl});

  static const _monthNames = [
    'January', 'February', 'March', 'April',
    'May', 'June', 'July', 'August',
    'September', 'October', 'November', 'December',
  ];
  static const _dows = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

  @override
  Widget build(BuildContext context) {
    return Obx(() => Row(
          children: [
            Expanded(
              child: _FilterPill(
                label: ctrl.selectedYear.value.toString(),
                onTap: () => _pick(context,
                    title: 'Select Year',
                    items: ctrl.availableYears.map((y) => y.toString()).toList(),
                    selected: ctrl.selectedYear.value.toString(),
                    onSelect: (v) => ctrl.onYearSelected(int.parse(v))),
              ),
            ),
            const SizedBox(width: 6),
            Expanded(
              child: _FilterPill(
                label: ctrl.selectedMonthName,
                onTap: () => _pick(context,
                    title: 'Select Month',
                    items: ctrl.availableMonths
                        .map((m) => _monthNames[m - 1])
                        .toList(),
                    selected: _monthNames[ctrl.selectedMonth.value - 1],
                    onSelect: (v) =>
                        ctrl.onMonthSelected(_monthNames.indexOf(v) + 1)),
              ),
            ),
            const SizedBox(width: 6),
            Expanded(
              child: _FilterPill(
                label: ctrl.selectedDayLabel,
                onTap: () => _pick(context,
                    title: 'Select Day',
                    items: ctrl.availableDays.map((d) {
                      final dt = DateTime(ctrl.selectedYear.value,
                          ctrl.selectedMonth.value, d);
                      return '$d  ${_dows[dt.weekday - 1]}';
                    }).toList(),
                    selected: ctrl.selectedDayLabel,
                    onSelect: (v) => ctrl.onDaySelected(
                        int.parse(v.trim().split(' ').first))),
              ),
            ),
          ],
        ));
  }

  void _pick(BuildContext context,
      {required String title,
      required List<String> items,
      required String selected,
      required void Function(String) onSelect}) {
    Get.bottomSheet(
      _PickerSheet(
          title: title, items: items, selected: selected, onSelect: onSelect),
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      isScrollControlled: true,
    );
  }
}

// ─── Filter pill ──────────────────────────────────────────────

class _FilterPill extends StatelessWidget {
  final String label;
  final VoidCallback onTap;
  const _FilterPill({required this.label, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 7),
        decoration: BoxDecoration(
          color: Colors.white.withOpacity(0.10),
          border: Border.all(color: Colors.white.withOpacity(0.28)),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Flexible(
              child: Text(
                label,
                style: AppTextStyles.caption.copyWith(
                  color: Colors.white,
                  fontWeight: FontWeight.w500,
                  fontSize: 10,
                ),
                overflow: TextOverflow.ellipsis,
              ),
            ),
            const SizedBox(width: 4),
            Icon(Icons.keyboard_arrow_down_rounded,
                color: Colors.white.withOpacity(0.6), size: 13),
          ],
        ),
      ),
    );
  }
}

// ─── Picker sheet ─────────────────────────────────────────────

class _PickerSheet extends StatelessWidget {
  final String title;
  final List<String> items;
  final String selected;
  final void Function(String) onSelect;

  const _PickerSheet({
    required this.title,
    required this.items,
    required this.selected,
    required this.onSelect,
  });

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 12, bottom: 8),
            child: Container(
              width: 36,
              height: 4,
              decoration: BoxDecoration(
                color: AppColors.cardBorder,
                borderRadius: BorderRadius.circular(2),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 4, 16, 12),
            child: Align(
              alignment: Alignment.centerLeft,
              child: Text(title, style: AppTextStyles.h3),
            ),
          ),
          const Divider(height: 1, thickness: 0.8),
          ConstrainedBox(
            constraints: BoxConstraints(
              maxHeight: MediaQuery.of(context).size.height * 0.55,
            ),
            child: ListView.separated(
              shrinkWrap: true,
              itemCount: items.length,
              separatorBuilder: (_, __) =>
                  const Divider(height: 1, thickness: 0.8, indent: 16),
              itemBuilder: (_, i) {
                final item = items[i];
                final isActive = item
                    .trim()
                    .startsWith(selected.trim().split(' ').first);
                return InkWell(
                  onTap: () {
                    onSelect(item);
                    Get.back();
                  },
                  child: Container(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 16, vertical: 13),
                    color: isActive
                        ? AppColors.primary.withOpacity(0.06)
                        : Colors.transparent,
                    child: Row(
                      children: [
                        Expanded(
                          child: Text(
                            item,
                            style: AppTextStyles.body.copyWith(
                              fontWeight: isActive
                                  ? FontWeight.w500
                                  : FontWeight.w400,
                              color: isActive
                                  ? AppColors.primary
                                  : AppColors.textSecond,
                            ),
                          ),
                        ),
                        if (isActive)
                          Icon(Icons.check_rounded,
                              color: AppColors.primary, size: 18),
                      ],
                    ),
                  ),
                );
              },
            ),
          ),
          const SizedBox(height: 12),
        ],
      ),
    );
  }
}

// ─── Hero section ─────────────────────────────────────────────
// Tap the card itself to open the reader — no floating button.

class _HeroSection extends StatelessWidget {
  final HomeController ctrl;
  const _HeroSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final dev = ctrl.devotional.value;
      if (dev == null) return const SizedBox.shrink();
      return Padding(
        padding: const EdgeInsets.fromLTRB(14, 14, 14, 0),
        child: DevotionalHeroCard(
          dayBadge: '${ctrl.todayShort} · Day ${ctrl.dayNumber}',
          tag: ctrl.isViewingToday
              ? "Today's Devotional"
              : '${ctrl.selectedDayLabel} ${ctrl.selectedMonthName}',
          title: dev.title,
          verseReference: dev.verseReference,
          preview: dev.preview,
          onTap: ctrl.openReader,
        ),
      );
    });
  }
}

// ─── Recommendations ──────────────────────────────────────────

class _RecommendationsSection extends StatelessWidget {
  final HomeController ctrl;
  const _RecommendationsSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final recs = ctrl.recommendations;
      if (recs.isEmpty) return const SizedBox.shrink();
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _SectionHeader(
            title: 'Recommended for You',
            actionLabel: 'See all',
            onAction: ctrl.openRecommendations,
            topPadding: 18,
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 14),
            child: Row(
              children: List.generate(recs.take(2).length, (i) {
                final rec = recs[i];
                return Expanded(
                  child: Padding(
                    padding: EdgeInsets.only(right: i == 0 ? 8 : 0),
                    child: RecommendationCard(
                      date: rec.dateFormatted,
                      title: rec.title,
                      themeTag: rec.matchedTheme,
                      onTap: () => ctrl.openArchivedDevotional(rec.id),
                    ),
                  ),
                );
              }),
            ),
          ),
          const SizedBox(height: 4),
        ],
      );
    });
  }
}

// ─── Recent devotionals (horizontal scroll) ───────────────────

class _RecentGridSection extends StatelessWidget {
  final HomeController ctrl;
  const _RecentGridSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final items = ctrl.recentDevotionals;
      if (items.isEmpty) return const SizedBox.shrink();
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Padding(
            padding: EdgeInsets.fromLTRB(14, 8, 14, 0),
            child: Divider(color: AppColors.cardBorder, thickness: 0.8),
          ),
          _SectionHeader(
            title: 'Recent Devotionals',
            actionLabel: 'See all',
            onAction: ctrl.openArchive,
          ),
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.only(left: 14, right: 8, bottom: 2),
            child: Row(
              children: List.generate(items.length, (i) {
                final dev = items[i];
                return Padding(
                  padding: EdgeInsets.only(
                      right: i < items.length - 1 ? 10 : 0),
                  child: _HorizontalCard(
                    dev: dev,
                    onTap: () => ctrl.openDevotionalById(dev.id),
                  ),
                );
              }),
            ),
          ),
          const SizedBox(height: 6),
        ],
      );
    });
  }
}

// ─── Horizontal card ──────────────────────────────────────────

class _HorizontalCard extends StatelessWidget {
  final Devotional dev;
  final VoidCallback onTap;
  const _HorizontalCard({required this.dev, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 140,
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: AppColors.cardBorder),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ClipRRect(
              borderRadius:
                  const BorderRadius.vertical(top: Radius.circular(11)),
              child: SizedBox(
                height: 88,
                width: double.infinity,
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    Container(color: AppColors.navy2),
                    Container(
                      width: 26,
                      height: 26,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: AppColors.accent.withOpacity(0.7),
                      ),
                    ),
                    Positioned.fill(
                      child: Image.asset(
                        'assets/devotional/devotional.png',
                        fit: BoxFit.cover,
                        errorBuilder: (_, __, ___) =>
                            const SizedBox.shrink(),
                      ),
                    ),
                    if (dev.isRead)
                      Positioned(
                        top: 6,
                        right: 7,
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 6, vertical: 2),
                          decoration: BoxDecoration(
                            color: AppColors.success.withOpacity(0.85),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text('✓ Read',
                              style: AppTextStyles.caption.copyWith(
                                color: Colors.white,
                                fontSize: 7,
                                fontWeight: FontWeight.w500,
                              )),
                        ),
                      ),
                  ],
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(8, 7, 8, 8),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(dev.dateFormatted,
                      style: AppTextStyles.caption.copyWith(
                          color: AppColors.textMuted, fontSize: 9)),
                  const SizedBox(height: 3),
                  Text(dev.title,
                      style: AppTextStyles.body.copyWith(
                        fontWeight: FontWeight.w500,
                        fontSize: 11,
                        color: AppColors.textPrimary,
                        height: 1.3,
                      ),
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ─── Archive vertical list ────────────────────────────────────

class _ArchiveSection extends StatelessWidget {
  final HomeController ctrl;
  const _ArchiveSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final items = ctrl.archiveDevotionals;
      if (items.isEmpty) return const SizedBox.shrink();
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Padding(
            padding: EdgeInsets.fromLTRB(14, 6, 14, 0),
            child: Divider(color: AppColors.cardBorder, thickness: 0.8),
          ),
          _SectionHeader(
            title: 'From the Archive',
            actionLabel: 'Browse years',
            onAction: ctrl.openArchive,
          ),
          ...items.map((dev) => _VerticalCard(
                dev: dev,
                onTap: () => ctrl.openDevotionalById(dev.id),
              )),
        ],
      );
    });
  }
}

// ─── Vertical card (archive) ──────────────────────────────────

class _VerticalCard extends StatelessWidget {
  final Devotional dev;
  final VoidCallback onTap;
  const _VerticalCard({required this.dev, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.fromLTRB(14, 0, 14, 8),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(11),
          border: Border.all(color: AppColors.cardBorder),
        ),
        child: Row(
          children: [
            // Left accent bar
            Container(
              width: 4,
              height: 70,
              decoration: BoxDecoration(
                color: dev.isRead ? AppColors.success : AppColors.accent,
                borderRadius:
                    const BorderRadius.horizontal(left: Radius.circular(11)),
              ),
            ),
            // Thumbnail — uses same asset as horizontal card
            SizedBox(
              width: 54,
              height: 70,
              child: Stack(
                alignment: Alignment.center,
                children: [
                  Container(color: AppColors.navy2),
                  Container(
                    width: 14,
                    height: 14,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: AppColors.accent.withOpacity(0.65),
                    ),
                  ),
                  Positioned.fill(
                    child: Image.asset(
                      'assets/devotional/devotional.png',
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => const SizedBox.shrink(),
                    ),
                  ),
                ],
              ),
            ),
            // Content
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(
                    horizontal: 10, vertical: 9),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(dev.dateFormatted,
                            style: AppTextStyles.caption
                                .copyWith(fontSize: 9)),
                        if (dev.isRead)
                          Container(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 6, vertical: 2),
                            decoration: BoxDecoration(
                              color: AppColors.success.withOpacity(0.1),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text('✓ Read',
                                style: AppTextStyles.caption.copyWith(
                                  color: AppColors.success,
                                  fontSize: 8,
                                  fontWeight: FontWeight.w500,
                                )),
                          )
                        else
                          Text('Unread',
                              style: AppTextStyles.caption
                                  .copyWith(fontSize: 8)),
                      ],
                    ),
                    const SizedBox(height: 3),
                    Text(dev.title,
                        style: AppTextStyles.body.copyWith(
                          fontWeight: FontWeight.w500,
                          fontSize: 11,
                          height: 1.3,
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis),
                    const SizedBox(height: 2),
                    Text(dev.verseReference,
                        style: AppTextStyles.caption.copyWith(
                          color: AppColors.textMuted,
                          fontStyle: FontStyle.italic,
                        )),
                  ],
                ),
              ),
            ),
            const Padding(
              padding: EdgeInsets.only(right: 10),
              child: Icon(Icons.chevron_right_rounded,
                  color: AppColors.cardBorder, size: 18),
            ),
          ],
        ),
      ),
    );
  }
}

// ─── Shared section header ────────────────────────────────────

class _SectionHeader extends StatelessWidget {
  final String title;
  final String actionLabel;
  final VoidCallback onAction;
  final double topPadding;

  const _SectionHeader({
    required this.title,
    required this.actionLabel,
    required this.onAction,
    this.topPadding = 12,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.fromLTRB(14, topPadding, 14, 8),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(title, style: AppTextStyles.sectionLabel),
          GestureDetector(
            onTap: onAction,
            child: Text(actionLabel,
                style: AppTextStyles.caption.copyWith(
                  color: AppColors.linkBlue,
                  fontWeight: FontWeight.w500,
                )),
          ),
        ],
      ),
    );
  }
}