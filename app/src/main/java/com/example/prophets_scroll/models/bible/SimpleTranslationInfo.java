package com.example.prophets_scroll.models.bible;

/**
 * Model for translation info in bible-api.com books response
 */
public class SimpleTranslationInfo {
    private String identifier;
    private String name;
    private String language;
    private String language_code;
    private String license;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getLanguageCode() {
        return language_code;
    }

    public void setLanguageCode(String language_code) {
        this.language_code = language_code;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
