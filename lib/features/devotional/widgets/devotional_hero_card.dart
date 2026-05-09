// lib/features/devotional/widgets/devotional_hero_card.dart

import 'package:flutter/material.dart';
import '../../../core/theme/app_colors.dart';
import '../../../core/theme/app_text_styles.dart';

class DevotionalHeroCard extends StatelessWidget {
  final String dayBadge;     // e.g. "Apr 14 · Day 4,107"
  final String tag;          // e.g. "TODAY'S DEVOTIONAL"
  final String title;
  final String verseReference;
  final String preview;
  final VoidCallback onTap;

  const DevotionalHeroCard({
    super.key,
    required this.dayBadge,
    required this.tag,
    required this.title,
    required this.verseReference,
    required this.preview,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: AppColors.navy2,
          borderRadius: BorderRadius.circular(14),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _HeroImage(dayBadge: dayBadge),
            _HeroBody(
              tag: tag,
              title: title,
              verseReference: verseReference,
              preview: preview,
            ),
          ],
        ),
      ),
    );
  }
}

// ─── Image area with asset image ──────────────────────────────

class _HeroImage extends StatelessWidget {
  final String dayBadge;
  const _HeroImage({required this.dayBadge});

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: const BorderRadius.vertical(top: Radius.circular(13)),
      child: SizedBox(
        height: 120,
        width: double.infinity,
        child: Stack(
          fit: StackFit.expand,
          children: [
            // Background image from assets
            Image.asset(
              'assets/devotional/devotional.png',
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                // Fallback to gradient if image not found
                return Container(
                  color: AppColors.navy2,
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                          width: 40,
                          height: 40,
                          decoration: const BoxDecoration(
                            shape: BoxShape.circle,
                            color: AppColors.accent,
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),

            // Day badge — top right
            Positioned(
              top: 8,
              right: 10,
              child: Container(
                padding:
                    const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                decoration: BoxDecoration(
                  color: Colors.black.withOpacity(0.4),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  dayBadge,
                  style: AppTextStyles.caption.copyWith(
                    color: AppColors.gold,
                    fontSize: 9,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ─── Text body ────────────────────────────────────────────────

class _HeroBody extends StatelessWidget {
  final String tag;
  final String title;
  final String verseReference;
  final String preview;

  const _HeroBody({
    required this.tag,
    required this.title,
    required this.verseReference,
    required this.preview,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(12, 10, 12, 12),
      decoration: const BoxDecoration(
        color: AppColors.navy2,
        borderRadius: BorderRadius.vertical(bottom: Radius.circular(13)),
        border: Border(
          top: BorderSide(color: Color(0xFF2A5299)),
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(tag.toUpperCase(), style: AppTextStyles.goldLabel),
          const SizedBox(height: 3),
          Text(
            title,
            style: AppTextStyles.h3.copyWith(color: Colors.white),
          ),
          const SizedBox(height: 3),
          Text(
            verseReference,
            style: AppTextStyles.caption.copyWith(
              color: const Color(0xFFA8C4E8),
              fontStyle: FontStyle.italic,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            preview,
            style: AppTextStyles.caption.copyWith(
              color: const Color(0xFFCBD8EB),
              height: 1.5,
            ),
            maxLines: 3,
            overflow: TextOverflow.ellipsis,
          ),
        ],
      ),
    );
  }
}