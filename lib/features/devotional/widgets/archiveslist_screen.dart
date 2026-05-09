// lib/features/devotional/widgets/archive_list_section.dart

import 'package:flutter/material.dart';

import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';
import 'package:prophets_scroll/features/devotional/devotional_model.dart';

class ArchiveListSection extends StatelessWidget {
  final List<Devotional> devotionals;
  final void Function(Devotional) onTap;
  final VoidCallback onBrowseYears;

  const ArchiveListSection({
    super.key,
    required this.devotionals,
    required this.onTap,
    required this.onBrowseYears,
  });

  @override
  Widget build(BuildContext context) {
    if (devotionals.isEmpty) return const SizedBox.shrink();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // Section header
        Padding(
          padding: const EdgeInsets.fromLTRB(14, 6, 14, 8),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('From the Archive', style: AppTextStyles.sectionLabel),
              GestureDetector(
                onTap: onBrowseYears,
                child: Text(
                  'Browse years',
                  style: AppTextStyles.caption.copyWith(
                    color: AppColors.linkBlue,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ],
          ),
        ),

        // Vertical list — not wrapped in a scrollable itself,
        // the parent CustomScrollView handles the scroll
        ...devotionals.map(
          (dev) => _ArchiveCard(
            devotional: dev,
            onTap: () => onTap(dev),
          ),
        ),
      ],
    );
  }
}

// ─── Archive list card ────────────────────────────────────────

class _ArchiveCard extends StatelessWidget {
  final Devotional devotional;
  final VoidCallback onTap;

  const _ArchiveCard({required this.devotional, required this.onTap});

  static const _dotColors = [
    AppColors.gold,
    Color(0xFFA8C4E8),
    Color(0xFFFFCF40),
    Color(0xFF9FE1CB),
  ];

  Color _accentColor(String id) =>
      _dotColors[id.hashCode.abs() % _dotColors.length];

  @override
  Widget build(BuildContext context) {
    final accent = _accentColor(devotional.id);

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
            // Left accent bar — gold if unread, green if read
            Container(
              width: 4,
              height: 72,
              decoration: BoxDecoration(
                color: devotional.isRead ? AppColors.success : AppColors.gold,
                borderRadius: const BorderRadius.horizontal(
                  left: Radius.circular(11),
                ),
              ),
            ),

            // Thumbnail
            Container(
              width: 54,
              height: 72,
              color: AppColors.navy2,
              child: Center(
                child: Container(
                  width: 16,
                  height: 16,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: accent.withOpacity(0.65),
                  ),
                ),
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
                        Text(
                          devotional.dateFormatted,
                          style: AppTextStyles.caption.copyWith(fontSize: 9),
                        ),
                        if (devotional.isRead)
                          Container(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 6, vertical: 2),
                            decoration: BoxDecoration(
                              color: AppColors.success.withOpacity(0.1),
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Text(
                              '✓ Read',
                              style: AppTextStyles.caption.copyWith(
                                color: AppColors.success,
                                fontSize: 8,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          )
                        else
                          Text(
                            'Unread',
                            style: AppTextStyles.caption.copyWith(fontSize: 9),
                          ),
                      ],
                    ),
                    const SizedBox(height: 3),
                    Text(
                      devotional.title,
                      style: AppTextStyles.body.copyWith(
                        fontWeight: FontWeight.w500,
                        fontSize: 11,
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
                        color: AppColors.navyMuted,
                        fontStyle: FontStyle.italic,
                      ),
                    ),
                  ],
                ),
              ),
            ),

            // Chevron
            const Padding(
              padding: EdgeInsets.only(right: 10),
              child: Icon(
                Icons.chevron_right_rounded,
                color: AppColors.cardBorder,
                size: 18,
              ),
            ),
          ],
        ),
      ),
    );
  }
}