import 'package:flutter/material.dart';
import 'package:prophets_scroll/core/theme/app_colors.dart';

class BiblePage extends StatelessWidget {
  const BiblePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.surface,
      appBar: AppBar(
        title: const Text('Bible'),
        backgroundColor: AppColors.navy,
        elevation: 0,
      ),
      body: const Center(
        child: Text('Bible Page - Coming Soon'),
      ),
    );
  }
}
