package com.example.prophets_scroll.models.bible;

public class BibleVerse {
    private int verse;
    private String text;

    public BibleVerse() {
    }

    public BibleVerse(int verse, String text) {
        this.verse = verse;
        this.text = text;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
