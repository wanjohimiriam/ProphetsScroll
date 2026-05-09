// lib/features/main_shell.dart

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'package:prophets_scroll/core/theme/app_colors.dart';
import 'package:prophets_scroll/core/theme/app_text_styles.dart';
import 'package:prophets_scroll/features/bible/views/bible_page.dart';
import 'package:prophets_scroll/features/devotional/devotional.dart';
import 'package:prophets_scroll/features/notes/views/notes_page.dart';
import 'package:prophets_scroll/features/music/views/music_page.dart';

// ─── Controller ───────────────────────────────────────────────

class MainShellController extends GetxController {
  final currentTabIndex = 0.obs;

  void changeTab(int index) => currentTabIndex(index);
}

// ─── Shell ────────────────────────────────────────────────────

class MainShell extends StatefulWidget {
  const MainShell({super.key});

  @override
  State<MainShell> createState() => _MainShellState();
}

class _MainShellState extends State<MainShell> {
  // Put the controller once in initState — not inside build()
  // build() is called on every rebuild; putting here means one registration
  late final MainShellController _ctrl;

  @override
  void initState() {
    super.initState();
    _ctrl = Get.put(MainShellController());
  }

  // All 4 pages stay mounted — scroll position, state preserved on tab switch
  static const _pages = [
    DevotionalPage(),
    BiblePage(),
    NotesPage(),
    MusicPage(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // IndexedStack keeps all pages alive — switching tabs never rebuilds them
      body: Obx(() => IndexedStack(
            index: _ctrl.currentTabIndex.value,
            children: _pages,
          )),
      bottomNavigationBar: Obx(() => BottomNavigationBar(
            currentIndex: _ctrl.currentTabIndex.value,
            onTap: _ctrl.changeTab,
            type: BottomNavigationBarType.fixed,
            backgroundColor: Colors.white,
            selectedItemColor: AppColors.accent,
            unselectedItemColor: AppColors.textMuted,
            selectedFontSize: 10,
            unselectedFontSize: 10,
            elevation: 8,
            selectedLabelStyle: AppTextStyles.caption.copyWith(
              color: AppColors.primary,
              fontWeight: FontWeight.w500,
            ),
            unselectedLabelStyle: AppTextStyles.caption.copyWith(
              color: AppColors.textMuted,
            ),
            items: const [
              BottomNavigationBarItem(
                icon: Icon(Icons.self_improvement_outlined),
                activeIcon: Icon(Icons.self_improvement_rounded),
                label: 'Devotional',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.menu_book_outlined),
                activeIcon: Icon(Icons.menu_book_rounded),
                label: 'Bible',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.note_outlined),
                activeIcon: Icon(Icons.note_rounded),
                label: 'Notes',
              ),
              BottomNavigationBarItem(
                icon: Icon(Icons.music_note_outlined),
                activeIcon: Icon(Icons.music_note_rounded),
                label: 'Music',
              ),
            ],
          )),
    );
  }
}