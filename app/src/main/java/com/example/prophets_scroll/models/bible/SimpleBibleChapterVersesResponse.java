package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Response model for bible-api.com chapter verses endpoint
 * Example: https://bible-api.com/data/web/JHN/3
 */
public class SimpleBibleChapterVersesResponse {
    private SimpleTranslationInfo translation;
    private List<VerseDetail> verses;

    public SimpleTranslationInfo getTranslation() {
        return translation;
    }

    public void setTranslation(SimpleTranslationInfo translation) {
        this.translation = translation;
    }

    public List<VerseDetail> getVerses() {
        return verses;
    }

    public void setVerses(List<VerseDetail> verses) {
        this.verses = verses;
    }
}
