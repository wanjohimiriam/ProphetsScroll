// lib/features/devotional/widgets/recommendation_card.dart

import 'package:flutter/material.dart';
import '../../../core/theme/app_colors.dart';
import '../../../core/theme/app_text_styles.dart';

class RecommendationCard extends StatelessWidget {
  final String date;        // e.g. "Jan 12, 2021"
  final String title;
  final String themeTag;    // e.g. "Same theme → Light"
  final VoidCallback onTap;

  const RecommendationCard({
    super.key,
    required this.date,
    required this.title,
    required this.themeTag,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
          border: Border.all(color: AppColors.cardBorder),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _CardThumb(),
            _CardBody(date: date, title: title, themeTag: themeTag),
          ],
        ),
      ),
    );
  }
}

// ─── Top thumbnail area ───────────────────────────────────────

class _CardThumb extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: const BorderRadius.vertical(top: Radius.circular(9)),
      child: SizedBox(
        height: 52,
        width: double.infinity,
        child: Image.asset(
          'assets/devotional/devotional.png',
          fit: BoxFit.cover,
          errorBuilder: (context, error, stackTrace) {
            return Container(
              color: AppColors.navy2,
              child: Center(
                child: Container(
                  width: 18,
                  height: 18,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: AppColors.accent.withOpacity(0.7),
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}

// ─── Text body ────────────────────────────────────────────────

class _CardBody extends StatelessWidget {
  final String date;
  final String title;
  final String themeTag;

  const _CardBody({
    required this.date,
    required this.title,
    required this.themeTag,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8, 7, 8, 8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            date,
            style: AppTextStyles.caption.copyWith(fontSize: 9),
          ),
          const SizedBox(height: 2),
          Text(
            title,
            style: AppTextStyles.body.copyWith(
              fontWeight: FontWeight.w500,
              fontSize: 10,
            ),
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
          const SizedBox(height: 4),
          Text(
            '→ $themeTag',
            style: AppTextStyles.caption.copyWith(
              color: AppColors.linkBlue,
              fontSize: 9,
            ),
          ),
        ],
      ),
    );
  }
}