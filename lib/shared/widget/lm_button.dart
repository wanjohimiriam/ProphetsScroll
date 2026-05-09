// shared/widgets/lm_button.dart
import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';

class LmButton extends StatelessWidget {
  final String label;
  final VoidCallback? onTap;
  final bool isOutlined;

  const LmButton({super.key, required this.label, this.onTap, this.isOutlined = false});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: double.infinity,
        height: 48,
        decoration: BoxDecoration(
          color: isOutlined ? Colors.transparent : AppColors.gold,
          border: isOutlined ? Border.all(color: AppColors.cardBorder) : null,
          borderRadius: BorderRadius.circular(10),
        ),
        alignment: Alignment.center,
        child: Text(label,
          style: isOutlined
            ? AppTextStyles.body.copyWith(color: AppColors.textSecond)
            : AppTextStyles.body.copyWith(color: AppColors.navy, fontWeight: FontWeight.w600),
        ),
      ),
    );
  }
}