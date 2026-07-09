package com.example.prophets_scroll.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Retrofit client with logging interceptor
 */
public class ApiClient {

    private static final String MAIN_BASE_URL = "https://api.prophetscroll.com/v1/";
    private static final String BIBLE_BASE_URL = "https://bible-go-api.rkeplin.com/v1/";
    // private static final String SIMPLE_BIBLE_BASE_URL = "https://bible-api.com/"; // Commented - replaced by YouVersion
    private static final String YOUVERSION_BASE_URL = "https://api.youversion.com/v1/";
    private static final String YOUVERSION_API_KEY = "YC7EzmTxGnrQN9EHlRbDHmjuXGJa7eUQpXcG3110EBsS6k0c";
    
    private static Retrofit mainRetrofit = null;
    private static Retrofit bibleRetrofit = null;
    private static Retrofit youVersionRetrofit = null;

    private static OkHttpClient createHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static OkHttpClient createYouVersionHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request request = original.newBuilder()
                            .header("X-YouVersion-Developer-Token", YOUVERSION_API_KEY)
                            .header("Accept-Language", "en")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getMainClient() {
        if (mainRetrofit == null) {
            mainRetrofit = new Retrofit.Builder()
                    .baseUrl(MAIN_BASE_URL)
                    .client(createHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mainRetrofit;
    }

    public static Retrofit getBibleClient() {
        if (bibleRetrofit == null) {
            bibleRetrofit = new Retrofit.Builder()
                    .baseUrl(BIBLE_BASE_URL)
                    .client(createHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return bibleRetrofit;
    }

    public static Retrofit getYouVersionClient() {
        if (youVersionRetrofit == null) {
            youVersionRetrofit = new Retrofit.Builder()
                    .baseUrl(YOUVERSION_BASE_URL)
                    .client(createYouVersionHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return youVersionRetrofit;
    }

    public static ApiService getApiService() {
        return getMainClient().create(ApiService.class);
    }

    public static ApiService getBibleApiService() {
        return getBibleClient().create(ApiService.class);
    }

    public static ApiService getYouVersionApiService() {
        return getYouVersionClient().create(ApiService.class);
    }
    
    // Commented out - replaced by YouVersion
    /*
    public static Retrofit getSimpleBibleClient() {
        if (simpleBibleRetrofit == null) {
            simpleBibleRetrofit = new Retrofit.Builder()
                    .baseUrl(SIMPLE_BIBLE_BASE_URL)
                    .client(createHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return simpleBibleRetrofit;
    }

    public static ApiService getSimpleBibleApiService() {
        return getSimpleBibleClient().create(ApiService.class);
    }
    */
}
