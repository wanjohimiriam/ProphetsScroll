package com.example.prophets_scroll.streaming;

import android.app.PictureInPictureParams;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.hls.HlsMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

import com.example.prophets_scroll.R;

import java.util.Collections;

/**
 * Plays the MCTV live HLS stream.
 *
 * TLS note: live2.nixsat.com serves an incomplete certificate chain
 * (missing the "YR2" and "Root YR" intermediates). That's fixed at the
 * network layer via res/xml/network_security_config.xml, which bundles
 * the missing intermediates as trust anchors for the nixsat.com domain --
 * see res/raw/nixsat_chain_bundle.pem. No code-level workaround needed
 * here anymore.
 */
@OptIn(markerClass = UnstableApi.class)
public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayerActivity";

    // -- Stream config --------------------------------------------------
    private static final String STREAM_URL = "https://live2.nixsat.com/play/MCTV/index.m3u8";

    // UNVERIFIED hypothesis, kept from before the SSL chain issue was found
    // and fixed -- the original symptom (plays via a Facebook-shared link,
    // not directly) may turn out to have been the SSL error the whole time,
    // with no real Referer/hotlink check on the server's side at all.
    // Now that TLS is fixed, test without these two lines first; only keep
    // them if removing them actually breaks playback.
    private static final String REFERER = "https://www.facebook.com/";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/124.0.0.0 Mobile Safari/537.36";

    // -- Views ------------------------------------------------------------
    private PlayerView playerView;
    private ImageButton btnBack;
    private ImageButton btnRefresh;
    private ImageButton btnPip;
    private ImageButton btnFitToggle;

    // -- Player -------------------------------------------------------------
    private ExoPlayer player;

    // -- Gesture state ------------------------------------------------------
    private ScaleGestureDetector scaleGestureDetector;
    private boolean isZoomedIn = false;

    // =====================================================================
    // Lifecycle
    // =====================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_video_player);

        bindViews();
        setupClickListeners();
        setupPinchToZoom();
        applyBottomInsetPadding();
        initPlayer();
        applyOrientationChrome(getResources().getConfiguration().orientation);
    }

    /**
     * Handles rotation via manifest configChanges instead of a full Activity
     * recreate (see AndroidManifest -- android:configChanges on this
     * Activity). Re-inflates the right layout (portrait vs res/layout-land)
     * but reattaches the SAME ExoPlayer instance, so the stream doesn't
     * restart on rotation.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_video_player); // resolves to layout-land automatically

        bindViews();
        setupClickListeners();
        setupPinchToZoom();
        applyBottomInsetPadding();
        playerView.setPlayer(player); // reattach -- no new MediaSource, no restart

        applyOrientationChrome(newConfig.orientation);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        playerView.setUseController(!isInPictureInPictureMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (player != null) player.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) player.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    // =====================================================================
    // View setup
    // =====================================================================

    private void bindViews() {
        playerView = findViewById(R.id.playerView);
        btnBack = findViewById(R.id.btnBack);
        btnRefresh = findViewById(R.id.btnRefresh);

        // Live inside the inflated controller layout -- PlayerView inflates
        // controller_layout_id as part of its own hierarchy, so these
        // resolve fine as long as the ids exist in custom_player_controls.xml.
        btnPip = playerView.findViewById(R.id.btnPip);
        btnFitToggle = playerView.findViewById(R.id.btnFitToggle);

        boolean pipSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
        if (!pipSupported && btnPip != null) {
            btnPip.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnRefresh.setOnClickListener(v -> reloadStream());

        if (btnPip != null) {
            btnPip.setOnClickListener(v -> enterPipMode());
        }
        if (btnFitToggle != null) {
            btnFitToggle.setOnClickListener(v -> toggleFitMode());
        }
    }

    // =====================================================================
    // Orientation / immersive chrome
    // =====================================================================

    private void applyOrientationChrome(int orientation) {
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            controller.hide(WindowInsetsCompat.Type.systemBars());
            controller.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        } else {
            controller.show(WindowInsetsCompat.Type.systemBars());
        }
    }

    /**
     * Pads the bottom control bar by the real system-bar/gesture-bar inset
     * instead of a guessed static value, since gesture bar height varies
     * by device.
     */
    private void applyBottomInsetPadding() {
        LinearLayout controlsBar = playerView.findViewById(R.id.controlsBottomBar);
        if (controlsBar == null) return;

        int basePaddingPx = (int) (28 * getResources().getDisplayMetrics().density);

        ViewCompat.setOnApplyWindowInsetsListener(playerView, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            controlsBar.setPadding(
                    controlsBar.getPaddingLeft(),
                    controlsBar.getPaddingTop(),
                    controlsBar.getPaddingRight(),
                    basePaddingPx + bottomInset);
            return insets;
        });
        ViewCompat.requestApplyInsets(playerView);
    }

    // =====================================================================
    // Gestures
    // =====================================================================

    /**
     * YouTube-style pinch: not continuous scaling, just a threshold-based
     * toggle between FIT (letterboxed, nothing cropped) and ZOOM (cropped
     * to fill) -- which is actually how YouTube's own pinch gesture behaves.
     */
    private void setupPinchToZoom() {
        scaleGestureDetector = new ScaleGestureDetector(this,
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        float scaleFactor = detector.getScaleFactor();
                        if (scaleFactor > 1.05f && !isZoomedIn) {
                            setZoomed(true);
                        } else if (scaleFactor < 0.95f && isZoomedIn) {
                            setZoomed(false);
                        }
                        return true;
                    }
                });

        playerView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            // Return false so PlayerView still gets the event for its own
            // single-tap-to-show/hide-controls behavior.
            return false;
        });
    }

    private void toggleFitMode() {
        setZoomed(!isZoomedIn);
    }

    private void setZoomed(boolean zoomed) {
        isZoomedIn = zoomed;
        playerView.setResizeMode(zoomed
                ? AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                : AspectRatioFrameLayout.RESIZE_MODE_FIT);
    }

    // =====================================================================
    // Player
    // =====================================================================

    private void initPlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e(TAG, "Playback error: code=" + error.errorCode
                        + " name=" + error.getErrorCodeName(), error);
                Toast.makeText(VideoPlayerActivity.this,
                        "Stream error: " + error.getErrorCodeName(), Toast.LENGTH_LONG).show();
            }
        });

        loadSource();
    }

    private void loadSource() {
        DefaultHttpDataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                .setUserAgent(USER_AGENT)
                .setDefaultRequestProperties(Collections.singletonMap("Referer", REFERER))
                .setConnectTimeoutMs(30000)
                .setReadTimeoutMs(30000)
                .setAllowCrossProtocolRedirects(true);

        MediaItem mediaItem = MediaItem.fromUri(STREAM_URL);
        MediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem);

        player.setMediaSource(hlsMediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    /** Wired to the refresh button -- tears down and reloads the source cleanly. */
    private void reloadStream() {
        if (player == null) return;
        player.stop();
        player.clearMediaItems();
        loadSource();
        Toast.makeText(this, "Reloading stream...", Toast.LENGTH_SHORT).show();
    }

    // =====================================================================
    // Picture-in-picture
    // =====================================================================

    private void enterPipMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        try {
            Rational aspectRatio = new Rational(16, 9);
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build();
            enterPictureInPictureMode(params);
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Cannot enter PIP mode", Toast.LENGTH_SHORT).show();
        }
    }
}