package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Response model for bible-api.com chapters endpoint
 * Example: https://bible-api.com/data/web/JHN
 */
public class SimpleBibleChaptersResponse {
    private SimpleTranslationInfo translation;
    private List<SimpleBibleChapter> chapters;

    public SimpleTranslationInfo getTranslation() {
        return translation;
    }

    public void setTranslation(SimpleTranslationInfo translation) {
        this.translation = translation;
    }

    public List<SimpleBibleChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<SimpleBibleChapter> chapters) {
        this.chapters = chapters;
    }
}
