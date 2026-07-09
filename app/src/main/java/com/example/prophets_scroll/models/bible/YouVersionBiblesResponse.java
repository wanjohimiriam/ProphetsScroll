package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Response model for YouVersion Bibles endpoint
 * Example: https://api.youversion.com/v1/bibles
 */
public class YouVersionBiblesResponse {
    private List<YouVersionBible> data;
    private String next_page_token;
    private int total_size;

    public List<YouVersionBible> getData() {
        return data;
    }

    public void setData(List<YouVersionBible> data) {
        this.data = data;
    }

    public String getNextPageToken() {
        return next_page_token;
    }

    public void setNextPageToken(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public int getTotalSize() {
        return total_size;
    }

    public void setTotalSize(int total_size) {
        this.total_size = total_size;
    }
}
