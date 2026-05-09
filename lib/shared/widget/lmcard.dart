// shared/widgets/lm_card.dart
import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';

class LmCard extends StatelessWidget {
  final Widget child;
  final EdgeInsets? padding;
  final Color? background;
  final BorderRadius? radius;

  const LmCard({super.key, required this.child, this.padding, this.background, this.radius});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: padding ?? const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: background ?? Colors.white,
        borderRadius: radius ?? BorderRadius.circular(12),
        border: Border.all(color: AppColors.cardBorder, width: 0.8),
      ),
      child: child,
    );
  }
}