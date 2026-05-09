// lib/data/models/devotional.dart

import 'package:intl/intl.dart';

class Devotional {
  final String id;
  final DateTime date;
  final String title;
  final String verseReference;  // e.g. "John 8:12"
  final String verseText;       // full verse quote
  final String preview;         // ~180 chars shown on home card
  final List<String> paragraphs; // full body — up to 10 paragraphs
  final String? imageUrl;
  final String lang;            // 'en' | 'sw' | 'fr' | 'ki' | 'lu'
  final List<String> themes;    // e.g. ['light', 'faith', 'direction']
  final String? linkedSongId;
  bool isRead;

  Devotional({
    required this.id,
    required this.date,
    required this.title,
    required this.verseReference,
    required this.verseText,
    required this.preview,
    required this.paragraphs,
    this.imageUrl,
    required this.lang,
    required this.themes,
    this.linkedSongId,
    this.isRead = false,
  });

  // ── Computed ───────────────────────────────────────────────

  String get dateFormatted => DateFormat('MMM d, y').format(date);

  String get matchedTheme {
    if (themes.isEmpty) return '';
    final t = themes.first;
    return '${t[0].toUpperCase()}${t.substring(1)}';
  }

  // ── Serialisation ──────────────────────────────────────────

  factory Devotional.fromJson(Map<String, dynamic> json) => Devotional(
        id:             json['id'] as String,
        date:           DateTime.parse(json['date'] as String),
        title:          json['title'] as String,
        verseReference: json['verse_reference'] as String,
        verseText:      json['verse_text'] as String,
        preview:        json['preview'] as String,
        paragraphs:     List<String>.from(json['paragraphs'] as List),
        imageUrl:       json['image_url'] as String?,
        lang:           (json['lang'] as String?) ?? 'en',
        themes:         List<String>.from(json['themes'] as List? ?? []),
        linkedSongId:   json['linked_song_id'] as String?,
        isRead:         (json['is_read'] as bool?) ?? false,
      );

  Map<String, dynamic> toJson() => {
        'id':              id,
        'date':            date.toIso8601String(),
        'title':           title,
        'verse_reference': verseReference,
        'verse_text':      verseText,
        'preview':         preview,
        'paragraphs':      paragraphs,
        'image_url':       imageUrl,
        'lang':            lang,
        'themes':          themes,
        'linked_song_id':  linkedSongId,
        'is_read':         isRead,
      };

  // ── Copy with ──────────────────────────────────────────────

  Devotional copyWith({bool? isRead}) => Devotional(
        id:             id,
        date:           date,
        title:          title,
        verseReference: verseReference,
        verseText:      verseText,
        preview:        preview,
        paragraphs:     paragraphs,
        imageUrl:       imageUrl,
        lang:           lang,
        themes:         themes,
        linkedSongId:   linkedSongId,
        isRead:         isRead ?? this.isRead,
      );
}