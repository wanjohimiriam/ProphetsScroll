package com.example.prophets_scroll.retrofit;

import com.example.prophets_scroll.models.bible.BibleBook;
import com.example.prophets_scroll.models.bible.BibleChapter;
import com.example.prophets_scroll.models.bible.BibleTranslation;
import com.example.prophets_scroll.models.bible.BibleVerse;
import com.example.prophets_scroll.models.bible.RandomVerseResponse;
import com.example.prophets_scroll.models.bible.SimpleBibleBooksResponse;
import com.example.prophets_scroll.models.bible.SimpleBibleChaptersResponse;
import com.example.prophets_scroll.models.bible.SimpleBibleChapterVersesResponse;
import com.example.prophets_scroll.models.bible.SimpleBibleTranslationsResponse;
import com.example.prophets_scroll.models.bible.SimpleBibleVerseResponse;
import com.example.prophets_scroll.models.bible.YouVersionBiblesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * API Service interface defining all API endpoints
 */
public interface ApiService {

    // ==================== Bible Endpoints ====================
    
    /**
     * GET /data
     * Lists all available translations — populate your translation-picker screen
     */
    @GET("data")
    Call<List<BibleTranslation>> getTranslations();

    /**
     * GET /data/{translation}
     * Lists all books for that translation — populate your book list/home screen
     */
    @GET("data/{translation}")
    Call<List<BibleBook>> getBooks(@Path("translation") String translation);

    /**
     * GET /data/{translation}/{book}
     * Lists chapters in a book — populate a chapter grid when a user taps a book
     */
    @GET("data/{translation}/{book}")
    Call<List<BibleChapter>> getChapters(
            @Path("translation") String translation,
            @Path("book") String book
    );

    /**
     * GET /data/{translation}/{book}/{chapter}
     * Full verse text for a chapter — this is your actual "Bible reader" screen content
     */
    @GET("data/{translation}/{book}/{chapter}")
    Call<List<BibleVerse>> getVerses(
            @Path("translation") String translation,
            @Path("book") String book,
            @Path("chapter") int chapter
    );

    /**
     * GET /data/{translation}/random
     * Random verse from that whole translation — good for "Verse of the Day" or a shuffle feature
     */
    @GET("data/{translation}/random")
    Call<RandomVerseResponse> getRandomVerse(@Path("translation") String translation);

    /**
     * GET /data/{translation}/random/{book_ids}
     * Random verse limited to specific book(s), or OT / NT
     * Example: random/NT for a New-Testament-only devotional feature
     */
    @GET("data/{translation}/random/{book_ids}")
    Call<RandomVerseResponse> getRandomVerseFromBooks(
            @Path("translation") String translation,
            @Path("book_ids") String bookIds
    );

    // ==================== Simple Bible Verse Lookup (bible-api.com) ====================
    
    /**
     * GET /data
     * Get all available translations from bible-api.com
     * Example: https://bible-api.com/data
     * Returns list of 18 public domain translations in multiple languages
     */
    @GET("data")
    Call<SimpleBibleTranslationsResponse> getSimpleBibleTranslations();
    
    /**
     * GET /data/{translation}
     * Get all books for a translation from bible-api.com
     * Example: https://bible-api.com/data/web
     * Returns nested structure with translation info and books array
     */
    @GET("data/{translation}")
    Call<SimpleBibleBooksResponse> getSimpleBibleBooks(@Path("translation") String translation);
    
    /**
     * GET /data/{translation}/{book_id}
     * Get all chapters for a book from bible-api.com
     * Example: https://bible-api.com/data/web/JHN
     * Returns nested structure with translation info and chapters array
     */
    @GET("data/{translation}/{book_id}")
    Call<SimpleBibleChaptersResponse> getSimpleBibleChapters(
            @Path("translation") String translation,
            @Path("book_id") String bookId
    );
    
    /**
     * GET /data/{translation}/{book_id}/{chapter}
     * Get all verses for a specific chapter from bible-api.com
     * Example: https://bible-api.com/data/web/JHN/3
     * Returns nested structure with translation info and verses array
     */
    @GET("data/{translation}/{book_id}/{chapter}")
    Call<SimpleBibleChapterVersesResponse> getSimpleBibleChapterVerses(
            @Path("translation") String translation,
            @Path("book_id") String bookId,
            @Path("chapter") int chapter
    );
    
    /**
     * GET /{reference}
     * Simple verse lookup by reference
     * Example: John 3:16 or John+3:16
     * Response includes full verse text with translation details
     */
    @GET("{reference}")
    Call<SimpleBibleVerseResponse> getSimpleVerse(@Path("reference") String reference);
    
    // Note: Authentication endpoints (login, signup, googleAuth) will be added
    // once the corresponding model classes are created
}
