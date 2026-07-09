package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Response model for bible-api.com books endpoint
 * Example: https://bible-api.com/data/web
 */
public class SimpleBibleBooksResponse {
    private SimpleTranslationInfo translation;
    private List<SimpleBibleBook> books;

    public SimpleTranslationInfo getTranslation() {
        return translation;
    }

    public void setTranslation(SimpleTranslationInfo translation) {
        this.translation = translation;
    }

    public List<SimpleBibleBook> getBooks() {
        return books;
    }

    public void setBooks(List<SimpleBibleBook> books) {
        this.books = books;
    }
}
