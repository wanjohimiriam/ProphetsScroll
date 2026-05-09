// core/theme/app_theme.dart
import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';

class AppTheme {
  static ThemeData get light => ThemeData(
    scaffoldBackgroundColor: AppColors.surface,
    colorScheme: ColorScheme.light(
      primary: AppColors.navy,
      secondary: AppColors.gold,
      surface: Colors.white,
    ),
    appBarTheme: const AppBarTheme(
      backgroundColor: AppColors.navy,
      foregroundColor: Colors.white,
      elevation: 0,
      titleTextStyle: AppTextStyles.navyTitle,
    ),
    bottomNavigationBarTheme: const BottomNavigationBarThemeData(
      backgroundColor: AppColors.navy,
      selectedItemColor: AppColors.gold,
      unselectedItemColor: AppColors.navyMuted,
    ),
  );
}