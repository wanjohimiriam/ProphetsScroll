package com.example.prophets_scroll.models.bible;

public class BibleBook {
    private String book;
    private String name;
    private int chapters;

    public BibleBook() {
    }

    public BibleBook(String book, String name, int chapters) {
        this.book = book;
        this.name = name;
        this.chapters = chapters;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }
}
