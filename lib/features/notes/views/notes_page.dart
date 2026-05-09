import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';

class NotesPage extends StatelessWidget {
  const NotesPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.surface,
      appBar: AppBar(
        title: const Text('Notes'),
        backgroundColor: AppColors.navy,
        elevation: 0,
      ),
      body: const Center(
        child: Text('Notes Page - Coming Soon'),
      ),
    );
  }
}
