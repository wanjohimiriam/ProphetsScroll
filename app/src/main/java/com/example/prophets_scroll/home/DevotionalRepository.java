package com.example.prophets_scroll.home;

import com.example.prophets_scroll.R;
import com.example.prophets_scroll.models.devotionals.Devotional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Source of truth for the devotionals -- the "backend" for now is just this
 * class. Swap in a real API/DB later without touching any caller.
 *
 * NO title field. NO verse/reference fields -- those are parsed out of
 * each entry's raw text at read time (RawTextReader.parseOpeningScripture).
 *
 * File naming: raw text and images do NOT share a base name (raw text is
 * dated -- devotional_july_2_2026.txt; images are dev2.jpeg, dev3.jpeg...).
 */
public final class DevotionalRepository {

    private static final List<Devotional> DEVOTIONALS = Collections.unmodifiableList(Arrays.asList(
            new Devotional(
                    "devotional_july_1_2026",
                    "TODO: set July 1's real category",
                    "July 1, 2026",
                    R.drawable.devpage,
                    R.raw.devotional_july_1_2026),
            new Devotional(
                    "devotional_july_2_2026",
                    "Guidance",
                    "July 2, 2026",
                    R.drawable.dev2,
                    R.raw.devotional_july_2_2026),
            new Devotional(
                    "devotional_july_3_2026",
                    "Rest",
                    "July 3, 2026",
                    R.drawable.dev3,
                    R.raw.devotional_july_3_2026),
            new Devotional(
                    "devotional_july_4_2026",
                    "Grace",
                    "July 4, 2026",
                    R.drawable.dev4,
                    R.raw.devotional_july_4_2026),
            new Devotional(
                    "devotional_july_5_2026",
                    "Hope",
                    "July 5, 2026",
                    R.drawable.dev5,
                    R.raw.devotional_july_5_2026),
            new Devotional(
                    "devotional_july_6_2026",
                    "Hope",
                    "July 6, 2026",
                    R.drawable.dev6,
                    R.raw.devotional_july_6_2026),
            new Devotional(
                    "devotional_july_7_2026",
                    "Renewal",
                    "July 7, 2026",
                    R.drawable.dev7,
                    R.raw.devotional_july_7_2026)
    ));

    private DevotionalRepository() {}

    public static List<Devotional> getAll() {
        return DEVOTIONALS;
    }

    public static Devotional getById(String id) {
        for (Devotional devotional : DEVOTIONALS) {
            if (devotional.getId().equals(id)) {
                return devotional;
            }
        }
        return null;
    }

    public static List<String> getCategories() {
        return DEVOTIONALS.stream()
                .map(Devotional::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}