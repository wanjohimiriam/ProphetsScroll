// core/router/app_router.dart
import 'package:get/get_navigation/src/routes/get_route.dart';
import 'package:prophets_scroll/features/devotional/devotional.dart';

abstract class AppRoutes {
  static const home         = '/';
  static const reader       = '/devotional/reader';
  static const archive      = '/devotional/archive';
  static const archiveMonth = '/devotional/archive/month';
  static const bible        = '/bible';
  static const bibleReader  = '/bible/reader';
  static const notes        = '/notes';
  static const noteDetail   = '/notes/detail';
  static const music        = '/music';
  static const musicLyrics  = '/music/lyrics';
  static const recommendations = '/recommendations';
}

class AppPages {
  static final pages = [
    GetPage(name: AppRoutes.home, page: () => DevotionalPage()),
    // GetPage(name: AppRoutes.reader,  page: () => const ReaderScreen()),
    // GetPage(name: AppRoutes.archive, page: () => const ArchiveScreen()),
    // GetPage(name: AppRoutes.bible,   page: () => const BibleScreen()),
    // GetPage(name: AppRoutes.notes,   page: () => const NotesScreen()),
    // GetPage(name: AppRoutes.music,   page: () => const MusicScreen()),
  ];
}