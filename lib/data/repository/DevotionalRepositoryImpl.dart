// lib/data/repository/DevotionalRepositoryImpl.dart

import 'package:prophets_scroll/features/devotional/DevotionalRepository.dart';
import 'package:prophets_scroll/features/devotional/devotional_model.dart';
import 'package:prophets_scroll/features/devotional/song_model.dart';
import 'package:prophets_scroll/features/devotional/mock_data.dart';

/// Concrete implementation using mock data for static UI development.
/// Replace with actual API/DB calls when backend is ready.
class DevotionalRepositoryImpl implements DevotionalRepository {
  // Simulate network delay
  static const Duration _mockDelay = Duration(milliseconds: 300);

  // ── Today's devotional ──────────────────────────────────

  @override
  Future<Devotional> getTodayDevotional({required String lang}) async {
    await Future.delayed(_mockDelay);
    return MockDevotionalData.todayDevotional;
  }

  // ── Devotional by specific date ─────────────────────────

  @override
  Future<Devotional> getDevotionalByDate({
    required DateTime date,
    required String lang,
  }) async {
    await Future.delayed(_mockDelay);
    // For now, return today's devotional regardless of date
    // Later, implement actual date lookup
    final isToday = date.year == DateTime.now().year &&
        date.month == DateTime.now().month &&
        date.day == DateTime.now().day;
    
    if (isToday) {
      return MockDevotionalData.todayDevotional;
    }
    
    // Return from recent devotionals if available
    final recent = MockDevotionalData.recentDevotionals;
    if (recent.isNotEmpty) {
      return recent.first;
    }
    
    throw const DevotionalNotFoundException('No devotional for that date');
  }

  // ── Today's song ────────────────────────────────────────

  @override
  Future<DevotionalSong?> getTodaysSong({required String lang}) async {
    await Future.delayed(_mockDelay);
    return MockDevotionalData.todaySong;
  }

  // ── Recommendations ─────────────────────────────────────

  @override
  Future<List<Devotional>> getRecommendations({
    required List<String> themes,
    required String excludeId,
    int limit = 2,
  }) async {
    await Future.delayed(_mockDelay);
    return MockDevotionalData.recommendations.take(limit).toList();
  }

  // ── Recent devotionals (horizontal grid) ────────────────

  @override
  Future<List<Devotional>> getRecent({
    required String lang,
    required String excludeId,
    int limit = 12,
  }) async {
    await Future.delayed(_mockDelay);
    return MockDevotionalData.recentDevotionals.take(limit).toList();
  }

  // ── Archive sample (vertical list) ──────────────────────

  @override
  Future<List<Devotional>> getArchiveSample({
    required String lang,
    int limit = 6,
  }) async {
    await Future.delayed(_mockDelay);
    return MockDevotionalData.archiveSample.take(limit).toList();
  }

  // ── Get by ID ────────────────────────────────────────────

  @override
  Future<Devotional?> getById(String id) async {
    await Future.delayed(_mockDelay);
    
    // Search in all mock data
    final allDevotionals = [
      MockDevotionalData.todayDevotional,
      ...MockDevotionalData.recentDevotionals,
      ...MockDevotionalData.recommendations,
      ...MockDevotionalData.archiveSample,
    ];
    
    try {
      return allDevotionals.firstWhere((dev) => dev.id == id);
    } catch (e) {
      return null;
    }
  }

  // ── Get by month ─────────────────────────────────────────

  @override
  Future<List<Devotional>> getByMonth({
    required int year,
    required int month,
    required String lang,
  }) async {
    await Future.delayed(_mockDelay);
    // For mock, just return some devotionals
    return MockDevotionalData.recentDevotionals.take(5).toList();
  }

  // ── Mark as read ─────────────────────────────────────────

  @override
  Future<void> markAsRead(String id) async {
    await Future.delayed(_mockDelay);
    // Mock implementation - do nothing
  }

  // ── Save to notes ────────────────────────────────────────

  @override
  Future<void> saveToNotes({
    required String devotionalId,
    required String lang,
    String? reflection,
    String tag = 'Devotional',
  }) async {
    await Future.delayed(_mockDelay);
    // Mock implementation - do nothing
  }
}