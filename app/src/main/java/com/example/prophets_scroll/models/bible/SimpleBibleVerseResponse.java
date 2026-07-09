package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Response model for bible-api.com endpoint
 * Example: https://bible-api.com/John+3:16
 */
public class SimpleBibleVerseResponse {
    private String reference;
    private List<VerseDetail> verses;
    private String text;
    private String translation_id;
    private String translation_name;
    private String translation_note;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<VerseDetail> getVerses() {
        return verses;
    }

    public void setVerses(List<VerseDetail> verses) {
        this.verses = verses;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslationId() {
        return translation_id;
    }

    public void setTranslationId(String translation_id) {
        this.translation_id = translation_id;
    }

    public String getTranslationName() {
        return translation_name;
    }

    public void setTranslationName(String translation_name) {
        this.translation_name = translation_name;
    }

    public String getTranslationNote() {
        return translation_note;
    }

    public void setTranslationNote(String translation_note) {
        this.translation_note = translation_note;
    }
}
