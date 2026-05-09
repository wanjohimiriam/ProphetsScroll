// core/constants.dart
import 'package:flutter/material.dart';

// ─── Assets ───────────────────────────────────────────────────

abstract class AppAssets {
  static const _img = 'assets/images/';

  static const sunHero  = '${_img}sun_hero.png';
  static const logoMark = '${_img}logo_mark.svg';
}

// ─── Strings ──────────────────────────────────────────────────

abstract class AppStrings {
  static const appName   = 'Prophets Scroll';
  static const tagline   = 'Daily Light';
  static const defaultLang = 'en';
}

// ─── Dimensions ───────────────────────────────────────────────

abstract class AppDimensions {
  static const pagePadding = EdgeInsets.symmetric(horizontal: 16);
  static const cardRadius  = BorderRadius.all(Radius.circular(12));
  static const chipRadius  = BorderRadius.all(Radius.circular(20));
  static const sheetRadius = BorderRadius.vertical(top: Radius.circular(20));
}

// ─── Language model ───────────────────────────────────────────

class AppLanguage {
  final String code;
  final String label;
  final String flag;
  final String nativeName;

  const AppLanguage({
    required this.code,
    required this.label,
    required this.flag,
    required this.nativeName,
  });
}

// ─── App-wide constants ───────────────────────────────────────

abstract class AppConstants {
  /// Devotional archive start date — used to compute day number
  static final archiveStart = DateTime(2015, 1, 1);

  /// All supported languages — order controls chip display order
  static const languages = [
    AppLanguage(
      code: 'en',
      label: 'English',
      flag: '🇬🇧',
      nativeName: 'English',
    ),
    AppLanguage(
      code: 'sw',
      label: 'Kiswahili',
      flag: '🇰🇪',
      nativeName: 'Kiswahili',
    ),
    AppLanguage(
      code: 'fr',
      label: 'Français',
      flag: '🇫🇷',
      nativeName: 'Français',
    ),
    AppLanguage(
      code: 'ki',
      label: 'Kikuyu',
      flag: '🇰🇪',
      nativeName: 'Gĩkũyũ',
    ),
    AppLanguage(
      code: 'lu',
      label: 'Dholuo',
      flag: '🇰🇪',
      nativeName: 'Dholuo',
    ),
  ];

  /// Convenience: look up a language by code — returns English as fallback
  static AppLanguage languageByCode(String code) {
    return languages.firstWhere(
      (l) => l.code == code,
      orElse: () => languages.first,
    );
  }
}