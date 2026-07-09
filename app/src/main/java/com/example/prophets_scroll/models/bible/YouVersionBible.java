package com.example.prophets_scroll.models.bible;

import java.util.List;

/**
 * Model for YouVersion Bible translation
 */
public class YouVersionBible {
    private int id;
    private String abbreviation;
    private String promotional_content;
    private String copyright;
    private String info;
    private String publisher_url;
    private String language_tag;
    private String localized_abbreviation;
    private String localized_title;
    private String title;
    private List<String> books;
    private String youversion_deep_link;
    private String organization_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getPromotionalContent() {
        return promotional_content;
    }

    public void setPromotionalContent(String promotional_content) {
        this.promotional_content = promotional_content;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPublisherUrl() {
        return publisher_url;
    }

    public void setPublisherUrl(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String getLanguageTag() {
        return language_tag;
    }

    public void setLanguageTag(String language_tag) {
        this.language_tag = language_tag;
    }

    public String getLocalizedAbbreviation() {
        return localized_abbreviation;
    }

    public void setLocalizedAbbreviation(String localized_abbreviation) {
        this.localized_abbreviation = localized_abbreviation;
    }

    public String getLocalizedTitle() {
        return localized_title;
    }

    public void setLocalizedTitle(String localized_title) {
        this.localized_title = localized_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }

    public String getYouversionDeepLink() {
        return youversion_deep_link;
    }

    public void setYouversionDeepLink(String youversion_deep_link) {
        this.youversion_deep_link = youversion_deep_link;
    }

    public String getOrganizationId() {
        return organization_id;
    }

    public void setOrganizationId(String organization_id) {
        this.organization_id = organization_id;
    }
}
