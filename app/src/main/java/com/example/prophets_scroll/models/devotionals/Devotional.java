package com.example.prophets_scroll.models.devotionals;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

/**
 * A single devotional entry. No title field (see DevotionalRepository's
 * class comment). No verse/reference fields either -- those are PARSED
 * out of the raw text at read time via
 * com.example.prophets_scroll.home.RawTextReader.parseOpeningScripture(),
 * instead of being hand-typed here as a second copy that could drift out
 * of sync with the real content.
 */
public final class Devotional {

    private final String id;
    private final String category;
    private final String dayLabel;
    @DrawableRes private final int imageRes;
    @RawRes private final int contentRawRes;

    public Devotional(@NonNull String id, @NonNull String category, @NonNull String dayLabel,
                      @DrawableRes int imageRes, @RawRes int contentRawRes) {
        this.id = id;
        this.category = category;
        this.dayLabel = dayLabel;
        this.imageRes = imageRes;
        this.contentRawRes = contentRawRes;
    }

    @NonNull public String getId() { return id; }
    @NonNull public String getCategory() { return category; }
    @NonNull public String getDayLabel() { return dayLabel; }
    @DrawableRes public int getImageRes() { return imageRes; }
    @RawRes public int getContentRawRes() { return contentRawRes; }
}