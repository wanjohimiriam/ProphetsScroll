package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Response model for bible-api.com translations endpoint
 * Example: https://bible-api.com/data
 */
public class SimpleBibleTranslationsResponse {
    private List<SimpleBibleTranslation> translations;

    public List<SimpleBibleTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<SimpleBibleTranslation> translations) {
        this.translations = translations;
    }
}
