package com.example.prophets_scroll.models.bible;

public class BibleChapter {
    private int chapter;
    private int verses;

    public BibleChapter() {
    }

    public BibleChapter(int chapter, int verses) {
        this.chapter = chapter;
        this.verses = verses;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerses() {
        return verses;
    }

    public void setVerses(int verses) {
        this.verses = verses;
    }
}
