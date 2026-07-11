package com.example.prophets_scroll.favorites;

public class FavoriteItem {
    public enum Type {
        DEVOTIONAL,
        SONG
    }

    private String id;
    private Type type;
    private String title;
    private String subtitle; // Date for devotional, Artist for song
    private String extra; // Category for devotional, Duration for song
    private long timestamp;

    public FavoriteItem(String id, Type type, String title, String subtitle, String extra) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
        this.extra = extra;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getExtra() {
        return extra;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
