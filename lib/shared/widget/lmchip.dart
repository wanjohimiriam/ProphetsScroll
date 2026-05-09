// shared/widgets/lm_chip.dart
import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';

class LmChip extends StatelessWidget {
  final String label;
  final bool isActive;
  final VoidCallback? onTap;

  const LmChip({super.key, required this.label, this.isActive = false, this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
        decoration: BoxDecoration(
          color: isActive ? AppColors.navy : Colors.white,
          border: Border.all(color: isActive ? AppColors.navy : AppColors.cardBorder),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Text(label,
          style: AppTextStyles.caption.copyWith(
            color: isActive ? AppColors.gold : AppColors.textSecond,
            fontWeight: FontWeight.w500,
          ),
        ),
      ),
    );
  }
}