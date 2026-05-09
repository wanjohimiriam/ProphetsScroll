// lib/features/devotional/mock_data.dart

import 'package:prophets_scroll/features/devotional/devotional_model.dart';
import 'package:prophets_scroll/features/devotional/song_model.dart';

/// Mock data for static UI development
class MockDevotionalData {
  static final Devotional todayDevotional = Devotional(
    id: 'dev-001',
    date: DateTime.now(),
    title: 'Walk in the Light',
    verseReference: 'John 8:12',
    verseText: '"I am the light of the world. Whoever follows me will never walk in darkness, but will have the light of life." - John 8:12',
    preview: 'True light doesn\'t just illuminate the path ahead — it transforms the one who walks in it.',
    paragraphs: [
      'True light doesn\'t just illuminate the path ahead — it transforms the one who walks in it. Complete article content goes here.',
      'When we choose to follow the light, we align ourselves with truth, beauty, and goodness.',
      'This devotional invites you to reflect on areas where you might still be walking in darkness.',
    ],
    lang: 'en',
    themes: ['light', 'guidance', 'faith'],
  );

  static final DevotionalSong todaySong = DevotionalSong(
    id: 'song-001',
    title: 'He Will Guide Us All Along',
    artist: 'Trust in the Lord',
    lang: 'en',
  );

  static final List<Devotional> recentDevotionals = [
    Devotional(
      id: 'dev-002',
      date: DateTime(2026, 4, 15),
      title: 'Grace Sufficient',
      verseReference: '2 Corinthians 12:9',
      verseText: '"My grace is sufficient for you, for power is made perfect in weakness." - 2 Corinthians 12:9',
      preview: 'His grace is always enough for our weakness and struggles.',
      paragraphs: [
        'His grace is always enough for our weakness.',
        'In our moments of weakness, God\'s strength becomes visible.',
      ],
      lang: 'en',
      themes: ['grace', 'strength'],
    ),
    Devotional(
      id: 'dev-003',
      date: DateTime(2026, 4, 14),
      title: 'Seek First His Kingdom',
      verseReference: 'Matthew 6:33',
      verseText: '"But seek first his kingdom and his righteousness, and all these things will be given to you as well." - Matthew 6:33',
      preview: 'When we prioritize God\'s kingdom, everything else falls into place.',
      paragraphs: [
        'When we prioritize God\'s kingdom, everything else falls into place.',
        'What are you seeking first in your life today?',
      ],
      lang: 'en',
      themes: ['priorities', 'kingdom'],
    ),
    Devotional(
      id: 'dev-004',
      date: DateTime(2026, 4, 13),
      title: 'Peace Beyond Understanding',
      verseReference: 'Philippians 4:7',
      verseText: '"And the peace of God, which transcends all understanding, will guard your hearts and your minds in Christ Jesus." - Philippians 4:7',
      preview: 'Peace that surpasses all understanding guards our hearts.',
      paragraphs: [
        'Peace that surpasses all understanding guards our hearts.',
        'This peace is available to us even in times of anxiety.',
      ],
      lang: 'en',
      themes: ['peace', 'trust'],
    ),
    Devotional(
      id: 'dev-005',
      date: DateTime(2026, 4, 12),
      title: 'Love Covers All',
      verseReference: '1 Peter 4:8',
      verseText: '"Important: love covers a multitude of sins." - 1 Peter 4:8',
      preview: 'Love has the power to heal and restore broken relationships.',
      paragraphs: [
        'Love has the power to heal and restore.',
        'How can you extend love to those around you today?',
      ],
      lang: 'en',
      themes: ['love', 'healing'],
    ),
    Devotional(
      id: 'dev-006',
      date: DateTime(2026, 4, 11),
      title: 'Joy in the Struggle',
      verseReference: 'James 1:2-3',
      verseText: '"Consider it pure joy, my brothers and sisters, whenever you face trials of many kinds, because you know that the testing of your faith produces perseverance." - James 1:2-3',
      preview: 'Our trials produce perseverance and character.',
      paragraphs: [
        'Our trials produce perseverance and character.',
        'Joy in difficult times strengthens our faith.',
      ],
      lang: 'en',
      themes: ['joy', 'perseverance'],
    ),
  ];

  static final List<Devotional> recommendations = [
    Devotional(
      id: 'dev-007',
      date: DateTime(2026, 3, 20),
      title: 'Foundations of Faith',
      verseReference: 'Hebrews 11:1',
      verseText: '"Now faith is confidence in what we hope for and assurance about what we do not see." - Hebrews 11:1',
      preview: 'Faith is the substance of things hoped for.',
      paragraphs: [
        'Faith is the substance of things hoped for.',
        'Building strong faith foundations requires daily devotion.',
      ],
      lang: 'en',
      themes: ['faith', 'light'],
    ),
    Devotional(
      id: 'dev-008',
      date: DateTime(2026, 2, 14),
      title: 'Love One Another',
      verseReference: 'John 13:34',
      verseText: '"A new command I give you: Love one another. As I have loved you, so you must love one another." - John 13:34',
      preview: 'Jesus gave us a new commandment to love one another.',
      paragraphs: [
        'Jesus gave us a new commandment.',
        'This love should define our relationships with all people.',
      ],
      lang: 'en',
      themes: ['love', 'guidance'],
    ),
  ];

  static final List<Devotional> archiveSample = [
    Devotional(
      id: 'dev-009',
      date: DateTime(2024, 6, 15),
      title: 'Reflect & Renew',
      verseReference: 'Psalm 139:23',
      verseText: '"Search me, O God, and know my heart; test me and know my anxious thoughts." - Psalm 139:23',
      preview: 'Search me, O God, and know my heart.',
      paragraphs: [
        'Search me, O God, and know my heart.',
        'Take time for soul-searching and renewal today.',
      ],
      lang: 'en',
      themes: ['reflection', 'renewal'],
    ),
    Devotional(
      id: 'dev-010',
      date: DateTime(2023, 1, 10),
      title: 'New Beginnings',
      verseReference: '2 Corinthians 5:17',
      verseText: '"Therefore, if anyone is in Christ, he is a new creation; old things have passed away; behold, all things have become new." - 2 Corinthians 5:17',
      preview: 'Therefore, if anyone is in Christ, he is a new creation.',
      paragraphs: [
        'Therefore, if anyone is in Christ, he is a new creation.',
        'Every day offers a fresh start in Christ.',
      ],
      lang: 'en',
      themes: ['new beginnings', 'faith'],
    ),
    Devotional(
      id: 'dev-011',
      date: DateTime(2022, 12, 25),
      title: 'Hope Eternal',
      verseReference: 'Titus 2:13',
      verseText: '"We wait for the blessed hope and glorious appearing of our great God and Savior Jesus Christ." - Titus 2:13',
      preview: 'We wait for the blessed hope and glorious appearing.',
      paragraphs: [
        'We wait for the blessed hope and glorious appearing.',
        'Our eternal hope is secure in Christ.',
      ],
      lang: 'en',
      themes: ['hope', 'eternity'],
    ),
  ];
}
