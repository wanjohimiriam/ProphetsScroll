package com.example.prophets_scroll.models.bible;

/**
 * Model for individual chapter in bible-api.com chapters response
 */
public class SimpleBibleChapter {
    private String book_id;
    private String book;
    private int chapter;
    private String url;

    public String getBookId() {
        return book_id;
    }

    public void setBookId(String book_id) {
        this.book_id = book_id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
