// lib/features/devotional/widgets/recent_devotionals_grid.dart

import 'package:flutter/material.dart';

import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';
import 'package:prophets_scroll/features/devotional/devotional_model.dart';


class RecentDevotionalsGrid extends StatelessWidget {
  final List<Devotional> devotionals;
  final void Function(Devotional) onTap;
  final VoidCallback onSeeAll;

  const RecentDevotionalsGrid({
    super.key,
    required this.devotionals,
    required this.onTap,
    required this.onSeeAll,
  });

  @override
  Widget build(BuildContext context) {
    if (devotionals.isEmpty) return const SizedBox.shrink();

    // Pair items into columns of 2 for the 2-row layout
    final cols = _pairIntoColumns(devotionals);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // Section header
        Padding(
          padding: const EdgeInsets.fromLTRB(14, 10, 14, 8),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('Recent Devotionals', style: AppTextStyles.sectionLabel),
              GestureDetector(
                onTap: onSeeAll,
                child: Text(
                  'See all',
                  style: AppTextStyles.caption.copyWith(
                    color: AppColors.linkBlue,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ],
          ),
        ),

        // Horizontal scrolling 2-row grid
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          padding: const EdgeInsets.only(left: 14, right: 6, bottom: 4),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: cols.map((pair) {
              return Padding(
                padding: const EdgeInsets.only(right: 8),
                child: Column(
                  children: pair.map((dev) {
                    return Padding(
                      padding: const EdgeInsets.only(bottom: 8),
                      child: _GridCard(devotional: dev, onTap: () => onTap(dev)),
                    );
                  }).toList(),
                ),
              );
            }).toList(),
          ),
        ),
      ],
    );
  }

  /// Groups a flat list into pairs: [[0,1],[2,3],[4,5]…]
  /// Last pair may have only 1 item.
  List<List<Devotional>> _pairIntoColumns(List<Devotional> items) {
    final cols = <List<Devotional>>[];
    for (var i = 0; i < items.length; i += 2) {
      cols.add([
        items[i],
        if (i + 1 < items.length) items[i + 1],
      ]);
    }
    return cols;
  }
}

// ─── Individual grid card ─────────────────────────────────────

class _GridCard extends StatelessWidget {
  final Devotional devotional;
  final VoidCallback onTap;

  const _GridCard({required this.devotional, required this.onTap});

  // Cycle accent colours for visual interest
  static const _dotColors = [
    AppColors.gold,
    Color(0xFFA8C4E8), // light blue
    Color(0xFFFFCF40), // bright gold
    Color(0xFF9FE1CB), // teal
    Color(0xFF85B7EB), // mid blue
  ];

  Color _accentColor(String id) {
    final idx = id.hashCode.abs() % _dotColors.length;
    return _dotColors[idx];
  }

  @override
  Widget build(BuildContext context) {
    final accent = _accentColor(devotional.id);

    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 115,
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
          border: Border.all(color: AppColors.cardBorder),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Thumbnail
            ClipRRect(
              borderRadius:
                  const BorderRadius.vertical(top: Radius.circular(9)),
              child: SizedBox(
                height: 52,
                width: double.infinity,
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    Container(color: AppColors.navy2),
                    Container(
                      width: 14,
                      height: 14,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: accent.withOpacity(0.7),
                      ),
                    ),
                    // Read badge
                    if (devotional.isRead)
                      Positioned(
                        top: 5,
                        right: 6,
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 5, vertical: 2),
                          decoration: BoxDecoration(
                            color: AppColors.success.withOpacity(0.85),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            '✓ Read',
                            style: AppTextStyles.caption.copyWith(
                              color: Colors.white,
                              fontSize: 7,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
                      ),
                  ],
                ),
              ),
            ),
            // Body
            Padding(
              padding: const EdgeInsets.fromLTRB(7, 6, 7, 7),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    devotional.dateFormatted,
                    style: AppTextStyles.caption.copyWith(fontSize: 8),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    devotional.title,
                    style: AppTextStyles.body.copyWith(
                      fontWeight: FontWeight.w500,
                      fontSize: 9,
                      color: AppColors.textPrimary,
                      height: 1.3,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 3),
                  Text(
                    devotional.verseReference,
                    style: AppTextStyles.caption.copyWith(
                      color: AppColors.linkBlue,
                      fontSize: 8,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}