package com.example.prophets_scroll.models.bible;

public class BibleTranslation {
    private String translation;
    private String name;
    private String language;

    public BibleTranslation() {
    }

    public BibleTranslation(String translation, String name, String language) {
        this.translation = translation;
        this.name = name;
        this.language = language;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return name + " (" + translation.toUpperCase() + ")";
    }
}
