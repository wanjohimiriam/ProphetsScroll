package com.example.prophets_scroll.streaming;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.R;

public class MorningCloudTVActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout topBar;
    private ImageButton btnBack;
    
    private static final String STREAM_URL = "https://live2.nixsat.com/play/MCTV/index.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morning_cloud_tv_simple);

        initViews();
        setupWebView();
        loadStream();
    }

    private void initViews() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        topBar = findViewById(R.id.topBar);
        btnBack = findViewById(R.id.btnBack);
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadStream() {
        progressBar.setVisibility(View.VISIBLE);
        // Load HLS stream embedded player
        String embedHtml = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>body{margin:0;padding:0;background:#000;}video{width:100%;height:100vh;}</style>" +
                "<script src='https://cdn.jsdelivr.net/npm/hls.js@latest'></script></head>" +
                "<body><video id='video' controls autoplay></video>" +
                "<script>var video=document.getElementById('video');" +
                "if(Hls.isSupported()){var hls=new Hls();hls.loadSource('" + STREAM_URL + "');" +
                "hls.attachMedia(video);}else if(video.canPlayType('application/vnd.apple.mpegurl')){" +
                "video.src='" + STREAM_URL + "';}</script></body></html>";
        
        webView.loadDataWithBaseURL("https://example.com", embedHtml, "text/html", "UTF-8", null);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
