import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';

class MusicPage extends StatelessWidget {
  const MusicPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.surface,
      appBar: AppBar(
        title: const Text('Music'),
        backgroundColor: AppColors.navy,
        elevation: 0,
      ),
      body: const Center(
        child: Text('Music Page - Coming Soon'),
      ),
    );
  }
}
