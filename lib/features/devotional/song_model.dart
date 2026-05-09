// lib/data/models/song.dart

class DevotionalSong {
  final String id;
  final String title;
  final String artist;
  final String lang;             // 'en' | 'sw' | 'fr' | 'ki' | 'lu'
  final String? audioUrl;
  final String? linkedDevotionalId;
  final String? linkedDevotionalTitle;
  final List<LyricSection> lyrics;

  const DevotionalSong({
    required this.id,
    required this.title,
    required this.artist,
    required this.lang,
    this.audioUrl,
    this.linkedDevotionalId,
    this.linkedDevotionalTitle,
    this.lyrics = const [],
  });

  // ── Serialisation ──────────────────────────────────────────

  factory DevotionalSong.fromJson(Map<String, dynamic> json) => DevotionalSong(
        id:                     json['id'] as String,
        title:                  json['title'] as String,
        artist:                 json['artist'] as String,
        lang:                   (json['lang'] as String?) ?? 'en',
        audioUrl:               json['audio_url'] as String?,
        linkedDevotionalId:     json['linked_devotional_id'] as String?,
        linkedDevotionalTitle:  json['linked_devotional_title'] as String?,
        lyrics: (json['lyrics'] as List? ?? [])
            .map((s) => LyricSection.fromJson(s as Map<String, dynamic>))
            .toList(),
      );

  Map<String, dynamic> toJson() => {
        'id':                    id,
        'title':                 title,
        'artist':                artist,
        'lang':                  lang,
        'audio_url':             audioUrl,
        'linked_devotional_id':  linkedDevotionalId,
        'linked_devotional_title': linkedDevotionalTitle,
        'lyrics':                lyrics.map((s) => s.toJson()).toList(),
      };
}

// ─── Lyric Section (Verse 1, Chorus, Bridge…) ─────────────────

class LyricSection {
  final String label;       // e.g. 'Verse 1', 'Chorus'
  final List<LyricLine> lines;

  const LyricSection({
    required this.label,
    required this.lines,
  });

  factory LyricSection.fromJson(Map<String, dynamic> json) => LyricSection(
        label: json['label'] as String,
        lines: (json['lines'] as List)
            .map((l) => LyricLine.fromJson(l as Map<String, dynamic>))
            .toList(),
      );

  Map<String, dynamic> toJson() => {
        'label': label,
        'lines': lines.map((l) => l.toJson()).toList(),
      };
}

// ─── Lyric Line ───────────────────────────────────────────────

class LyricLine {
  final String text;
  final String? translation;            // English translation when lang != 'en'
  final String? linkedParagraphIndex;   // e.g. '2' — links back to devotional body

  const LyricLine({
    required this.text,
    this.translation,
    this.linkedParagraphIndex,
  });

  factory LyricLine.fromJson(Map<String, dynamic> json) => LyricLine(
        text:                  json['text'] as String,
        translation:           json['translation'] as String?,
        linkedParagraphIndex:  json['linked_paragraph_index'] as String?,
      );

  Map<String, dynamic> toJson() => {
        'text':                   text,
        'translation':            translation,
        'linked_paragraph_index': linkedParagraphIndex,
      };
}