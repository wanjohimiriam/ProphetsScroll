// lib/features/devotional/DevotionalRepository.dart

import 'package:prophets_scroll/features/devotional/devotional_model.dart';
import 'package:prophets_scroll/features/devotional/song_model.dart';

abstract class DevotionalRepository {

  /// Today's devotional in the given language.
  /// Falls back to English if no translation exists.
  Future<Devotional> getTodayDevotional({required String lang});

  /// Devotional for a specific calendar date in the given language.
  Future<Devotional> getDevotionalByDate({
    required DateTime date,
    required String lang,
  });

  /// Song linked to today's devotional.
  /// Returns null if no song has been linked yet.
  Future<DevotionalSong?> getTodaysSong({required String lang});

  /// Devotionals sharing at least one theme with [themes],
  /// excluding [excludeId], capped at [limit].
  Future<List<Devotional>> getRecommendations({
    required List<String> themes,
    required String excludeId,
    int limit = 2,
  });

  /// Most recent [limit] devotionals before today,
  /// excluding [excludeId], in the given language.
  /// Powers the horizontal 2-row grid on the home page.
  Future<List<Devotional>> getRecent({
    required String lang,
    required String excludeId,
    int limit = 12,
  });

  /// A spread of devotionals sampled across all years (2012 → now).
  /// Powers the "From the Archive" vertical list.
  Future<List<Devotional>> getArchiveSample({
    required String lang,
    int limit = 6,
  });

  /// Single devotional by its ID.
  Future<Devotional?> getById(String id);

  /// All devotionals for a given year and month.
  Future<List<Devotional>> getByMonth({
    required int year,
    required int month,
    required String lang,
  });

  /// Marks a devotional as read locally.
  Future<void> markAsRead(String id);

  /// Saves a devotional to the user's Notes tab.
  Future<void> saveToNotes({
    required String devotionalId,
    required String lang,
    String? reflection,
    String tag,
  });
}

/// Thrown when no devotional exists for the requested date/language.
class DevotionalNotFoundException implements Exception {
  final String message;
  const DevotionalNotFoundException([
    this.message = 'No devotional found for the requested date.',
  ]);

  @override
  String toString() => 'DevotionalNotFoundException: $message';
}