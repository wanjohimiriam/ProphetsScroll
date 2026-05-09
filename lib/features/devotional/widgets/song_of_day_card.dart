// lib/features/devotional/widgets/song_of_day_card.dart

import 'package:flutter/material.dart';
import '../../../core/theme/app_colors.dart';
import '../../../core/theme/app_text_styles.dart';

class SongOfDayCard extends StatelessWidget {
  final String title;
  final String artist;
  final bool isPlaying;
  final VoidCallback onTap;    // opens full music screen
  final VoidCallback onPlay;   // mini play/pause toggle

  const SongOfDayCard({
    super.key,
    required this.title,
    required this.artist,
    required this.isPlaying,
    required this.onTap,
    required this.onPlay,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(11),
        decoration: BoxDecoration(
          color: AppColors.navy2,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: const Color(0xFF2A5299)),
        ),
        child: Row(
          children: [
            _AlbumThumb(),
            const SizedBox(width: 10),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('♪  SONG OF THE DAY',
                      style: AppTextStyles.goldLabel),
                  const SizedBox(height: 2),
                  Text(
                    title,
                    style: AppTextStyles.body.copyWith(
                      color: Colors.white,
                      fontWeight: FontWeight.w500,
                      fontSize: 12,
                    ),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  Text(
                    artist,
                    style: AppTextStyles.caption
                        .copyWith(color: AppColors.navyMuted),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
              ),
            ),
            const SizedBox(width: 8),
            _PlayButton(isPlaying: isPlaying, onTap: onPlay),
          ],
        ),
      ),
    );
  }
}

// ─── Album thumbnail ──────────────────────────────────────────

class _AlbumThumb extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 42,
      height: 42,
      decoration: BoxDecoration(
        color: AppColors.navy,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: AppColors.gold.withOpacity(0.3)),
      ),
      child: Center(
        child: Container(
          width: 16,
          height: 16,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: AppColors.gold.withOpacity(0.8),
          ),
        ),
      ),
    );
  }
}

// ─── Play / Pause button ──────────────────────────────────────

class _PlayButton extends StatelessWidget {
  final bool isPlaying;
  final VoidCallback onTap;

  const _PlayButton({required this.isPlaying, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 32,
        height: 32,
        decoration: const BoxDecoration(
          shape: BoxShape.circle,
          color: AppColors.gold,
        ),
        child: Icon(
          isPlaying ? Icons.pause_rounded : Icons.play_arrow_rounded,
          color: AppColors.navy,
          size: 18,
        ),
      ),
    );
  }
}