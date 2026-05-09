// core/theme/app_colors.dart
import 'package:flutter/material.dart';

class AppColors {
  AppColors._();
  // Primary colors
  static const primary     = Color(0xFF2196F3);  // Bright Blue (Material Design)
  static const accent      = Color(0xFFFF5722);  // Orange/Red
  static const secondary   = Color(0xFF1976D2);  // Dark Blue
  
  // Legacy colors (kept for compatibility)
  static const navy        = Color(0xFF1976D2);
  static const navy2       = Color(0xFF1E88E5);
  static const gold        = Color(0xFFFF5722);  // Now matches accent
  static const goldSoft    = Color(0xFFFFEBEE);
  static const goldText    = Color(0xFFD84315);
  static const surface     = Color(0xFFF5F7FB);
  static const cardBorder  = Color(0xFFE0E0E0);
  static const textPrimary = Color(0xFF0D1B2A);
  static const textSecond  = Color(0xFF3D4F63);
  static const textMuted   = Color(0xFF8A93A2);
  static const navyMuted   = Color(0xFF90CAF9);
  static const success     = Color(0xFF27AE60);
  static const linkBlue    = Color(0xFF2196F3);
}