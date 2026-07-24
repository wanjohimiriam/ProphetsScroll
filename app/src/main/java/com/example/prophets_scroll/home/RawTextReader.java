package com.example.prophets_scroll.home;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads a devotional's raw text AND pulls the opening scripture out of it.
 *
 * The opening scripture can span MULTIPLE paragraphs (e.g. several
 * separate quoted verses in a row, with the reference only appearing at
 * the end of the LAST one):
 *
 *   TODAY'S DEVOTIONAL (Wednesday 8th July 2026)
 *
 *   "But evil men and impostors will grow worse and worse..."
 *
 *   "But you must continue in the things which you have learned..."
 *
 *   "All Scripture is given by inspiration of God..." 2 Timothy 3:13-17 NKJV
 *
 *   WELCOME TO THE 189TH DAY OF OUR YEAR OF STRATEGIC VICTORIES!
 *
 * Parsing keeps consuming paragraphs after the header line until EITHER a
 * trailing Bible reference is found (that paragraph is included, then
 * stop) OR a paragraph looks like a new ALL-CAPS section heading (stop
 * BEFORE consuming it, since that's the start of the body, not scripture).
 * A hard cap prevents runaway consumption if neither signal ever fires.
 */
public final class RawTextReader {

    private static final String TAG = "RawTextReader";

    // Matches a Bible reference at the END of a string, e.g.
    // "John 21:15-17 NKJV" or "2 Timothy 3:13-17 NKJV".
    private static final Pattern REFERENCE_PATTERN = Pattern.compile(
            "([1-3]?\\s?[A-Z][a-zA-Z]+(?:\\s[A-Z][a-zA-Z]+)?\\s\\d{1,3}:\\d{1,3}(?:-\\d{1,3})?\\s+[A-Z]{2,6})\\.?\\s*$");

    private static final Pattern DATE_PATTERN = Pattern.compile("\\(([^)]+)\\)");

    private static final int MAX_VERSE_PARAGRAPHS = 6; // safety cap

    private RawTextReader() {}

    @NonNull
    public static String read(@NonNull Context context, @RawRes int rawResId) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = context.getResources().openRawResource(rawResId);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read raw resource id " + rawResId, e);
        }
        return builder.toString().trim();
    }

    @NonNull
    public static ScripturePreview readAndParseOpeningScripture(
            @NonNull Context context, @RawRes int rawResId) {
        return parseOpeningScripture(read(context, rawResId));
    }

    @NonNull
    public static ScripturePreview parseOpeningScripture(@NonNull String fullText) {
        String[] paragraphs = splitIntoParagraphs(fullText);
        if (paragraphs.length == 0) {
            return new ScripturePreview("", null, null);
        }

        String dateLabel = null;
        int headerIndex = -1;

        for (int i = 0; i < paragraphs.length; i++) {
            String upper = paragraphs[i].toUpperCase();
            if (upper.contains("DEVOTIONAL") && paragraphs[i].contains("(")) {
                Matcher dateMatcher = DATE_PATTERN.matcher(paragraphs[i]);
                if (dateMatcher.find()) {
                    dateLabel = dateMatcher.group(1).trim();
                }
                headerIndex = i;
                break;
            }
        }

        int startIdx = (headerIndex >= 0) ? headerIndex + 1 : 0;

        List<String> verseParagraphs = new ArrayList<>();
        String reference = null;
        int scanned = 0;

        for (int idx = startIdx; idx < paragraphs.length && scanned < MAX_VERSE_PARAGRAPHS; idx++, scanned++) {
            String para = paragraphs[idx];

            // Stop BEFORE consuming what looks like the next section's
            // heading (e.g. "WELCOME TO THE 189TH DAY...") -- but only
            // once we've already collected at least one verse paragraph,
            // so a heading-shaped header line itself doesn't block us.
            if (!verseParagraphs.isEmpty() && isAllCapsHeading(para)) {
                break;
            }

            Matcher referenceMatcher = REFERENCE_PATTERN.matcher(para.trim());
            if (referenceMatcher.find()) {
                reference = referenceMatcher.group(1).trim();
                String trimmedPara = para.trim().substring(0, referenceMatcher.start()).trim();
                verseParagraphs.add(trimmedPara);
                break; // reference found -- this is the last verse paragraph
            }

            verseParagraphs.add(para.trim());
        }

        StringBuilder verseBuilder = new StringBuilder();
        for (String p : verseParagraphs) {
            if (verseBuilder.length() > 0) verseBuilder.append("\n\n");
            verseBuilder.append(stripSurroundingQuotes(p));
        }

        return new ScripturePreview(verseBuilder.toString().trim(), reference, dateLabel);
    }

    /** True if a paragraph looks like an ALL-CAPS section heading rather than scripture. */
    private static boolean isAllCapsHeading(String paragraph) {
        String trimmed = paragraph.trim();
        if (trimmed.isEmpty()) return false;

        int letters = 0;
        int upperLetters = 0;
        for (char c : trimmed.toCharArray()) {
            if (Character.isLetter(c)) {
                letters++;
                if (Character.isUpperCase(c)) upperLetters++;
            }
        }
        if (letters < 5) return false; // too short to judge reliably

        double upperRatio = (double) upperLetters / letters;
        return upperRatio > 0.85;
    }

    private static String[] splitIntoParagraphs(String fullText) {
        String[] raw = fullText.split("\\r?\\n\\s*\\r?\\n");
        List<String> nonEmpty = new ArrayList<>();
        for (String p : raw) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) nonEmpty.add(trimmed);
        }
        return nonEmpty.toArray(new String[0]);
    }

    private static String stripSurroundingQuotes(String text) {
        String result = text.trim();
        if (result.isEmpty()) return result;
        char first = result.charAt(0);
        if (first == '\u201C' || first == '"' || first == '\u2018') {
            result = result.substring(1);
        }
        if (!result.isEmpty()) {
            char last = result.charAt(result.length() - 1);
            if (last == '\u201D' || last == '"' || last == '\u2019') {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result.trim();
    }

    public static final class ScripturePreview {
        @NonNull public final String verseText;
        @Nullable public final String reference;
        @Nullable public final String dateLabel;

        public ScripturePreview(@NonNull String verseText, @Nullable String reference,
                                @Nullable String dateLabel) {
            this.verseText = verseText;
            this.reference = reference;
            this.dateLabel = dateLabel;
        }
    }
}