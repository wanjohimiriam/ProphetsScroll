package com.example.prophets_scroll.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    // ── Intent keys ──
    public static final String EXTRA_DEVOTIONAL_TITLE    = "extra_devotional_title";
    public static final String EXTRA_DEVOTIONAL_DATE     = "extra_devotional_date";
    public static final String EXTRA_DEVOTIONAL_CATEGORY = "extra_devotional_category";

    // ── Views ──
    private Toolbar toolbar;
    private ImageButton btnBack, btnSendComment;
    private TextView tvCommentCount, tvRefTitle, tvRefMeta, tvMyAvatar;
    private EditText etComment;
    private RecyclerView recyclerComments;

    // ── Adapter + data ──
    private CommentsAdapter adapter;
    private final List<CommentItem> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        initViews();
        setupToolbar();
        populateDevotionalRef();
        setupRecyclerView();
        loadDummyComments();
        setupInput();
    }

    // ─────────────────────────────────────────────
    //  1. INIT
    // ─────────────────────────────────────────────
    private void initViews() {
        toolbar          = findViewById(R.id.toolbar);
        btnBack          = findViewById(R.id.btnBack);
        tvCommentCount   = findViewById(R.id.tvCommentCount);
        tvRefTitle       = findViewById(R.id.tvRefTitle);
        tvRefMeta        = findViewById(R.id.tvRefMeta);
        tvMyAvatar       = findViewById(R.id.tvMyAvatar);
        etComment        = findViewById(R.id.etComment);
        btnSendComment   = findViewById(R.id.btnSendComment);
        recyclerComments = findViewById(R.id.recyclerComments);
    }

    // ─────────────────────────────────────────────
    //  2. TOOLBAR
    // ─────────────────────────────────────────────
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnBack.setOnClickListener(v -> finish());
    }

    // ─────────────────────────────────────────────
    //  3. DEVOTIONAL REFERENCE CARD
    // ─────────────────────────────────────────────
    private void populateDevotionalRef() {
        Intent intent = getIntent();
        String title    = intent.getStringExtra(EXTRA_DEVOTIONAL_TITLE);
        String date     = intent.getStringExtra(EXTRA_DEVOTIONAL_DATE);
        String category = intent.getStringExtra(EXTRA_DEVOTIONAL_CATEGORY);

        if (title    == null) title    = "Walking in Faith";
        if (date     == null) date     = "May 25, 2026";
        if (category == null) category = "Faith";

        tvRefTitle.setText(title);
        tvRefMeta.setText(date + " \u2022 " + category);

        // Current user initials — replace with real user session later
        tvMyAvatar.setText("MW");

        // View button → go back to detail screen
        findViewById(R.id.btnViewDevotional).setOnClickListener(v -> finish());
    }

    // ─────────────────────────────────────────────
    //  4. RECYCLERVIEW
    // ─────────────────────────────────────────────
    private void setupRecyclerView() {
        adapter = new CommentsAdapter(comments);
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerComments.setAdapter(adapter);
    }

    // ─────────────────────────────────────────────
    //  5. DUMMY DATA — swap with API call
    // ─────────────────────────────────────────────
    private void loadDummyComments() {
        comments.add(new CommentItem("Sarah Kimani",  "SK", "2h ago",
                "This message really spoke to me today. I've been struggling with uncertainty " +
                        "in my career, and this reminder to walk by faith came at the perfect time. Thank you! \uD83D\uDE4F", 5));

        comments.add(new CommentItem("John Mwangi",   "JM", "5h ago",
                "Powerful devotional! The example of Abraham really resonated with me. " +
                        "Sometimes we just need to take that first step even when we can't see the whole path.", 8));

        comments.add(new CommentItem("Alice Njeri",   "AN", "1d ago",
                "Amen! Faith is truly about trusting God even when the path is unclear. " +
                        "This has encouraged me to keep moving forward in faith.", 3));

        comments.add(new CommentItem("David Kariuki", "DK", "2d ago",
                "This is exactly what I needed to hear today. God's timing is always perfect.", 6));

        adapter.notifyDataSetChanged();
        updateCount();
    }

    // ─────────────────────────────────────────────
    //  6. COMMENT INPUT
    // ─────────────────────────────────────────────
    private void setupInput() {
        btnSendComment.setOnClickListener(v -> postComment());

        // Also send on keyboard "send" action
        etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                postComment();
                return true;
            }
            return false;
        });
    }

    private void postComment() {
        String text = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        // Add to top of list — newest first
        CommentItem newComment = new CommentItem("Miriam Wanjohi", "MW", "Just now", text, 0);
        comments.add(0, newComment);
        adapter.notifyItemInserted(0);
        recyclerComments.scrollToPosition(0);

        etComment.setText("");
        updateCount();
    }

    private void updateCount() {
        tvCommentCount.setText(String.valueOf(comments.size()));
    }

    // ─────────────────────────────────────────────
    //  INNER: data model
    // ─────────────────────────────────────────────
    public static class CommentItem {
        public String name, initials, timestamp, text;
        public int likeCount;
        public boolean liked = false;

        public CommentItem(String name, String initials, String timestamp,
                           String text, int likeCount) {
            this.name      = name;
            this.initials  = initials;
            this.timestamp = timestamp;
            this.text      = text;
            this.likeCount = likeCount;
        }
    }

    // ─────────────────────────────────────────────
    //  INNER: adapter
    // ─────────────────────────────────────────────
    static class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.VH> {

        private final List<CommentItem> items;

        CommentsAdapter(List<CommentItem> items) { this.items = items; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            CommentItem item = items.get(pos);

            h.tvAvatar.setText(item.initials);
            h.tvName.setText(item.name);
            h.tvTimestamp.setText(item.timestamp);
            h.tvText.setText(item.text);
            h.tvLikeCount.setText(String.valueOf(item.likeCount));

            // Like toggle
            h.btnLike.setOnClickListener(v -> {
                item.liked = !item.liked;
                item.likeCount += item.liked ? 1 : -1;
                h.tvLikeCount.setText(String.valueOf(item.likeCount));
                h.btnLike.setColorFilter(item.liked
                        ? h.itemView.getContext().getColor(R.color.error_red)
                        : h.itemView.getContext().getColor(R.color.text_hint));
            });

            // Reply — for now just focuses the input in parent Activity
            h.btnReply.setOnClickListener(v -> {
                // Cast to CommentsActivity to access etComment
                if (h.itemView.getContext() instanceof CommentsActivity) {
                    CommentsActivity activity = (CommentsActivity) h.itemView.getContext();
                    activity.etComment.setHint("Replying to " + item.name + "...");
                    activity.etComment.requestFocus();
                }
            });
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvAvatar, tvName, tvTimestamp, tvText, tvLikeCount, btnReply;
            ImageButton btnLike;

            VH(View v) {
                super(v);
                tvAvatar    = v.findViewById(R.id.tvAvatar);
                tvName      = v.findViewById(R.id.tvCommenterName);
                tvTimestamp = v.findViewById(R.id.tvTimestamp);
                tvText      = v.findViewById(R.id.tvCommentText);
                tvLikeCount = v.findViewById(R.id.tvLikeCount);
                btnLike     = v.findViewById(R.id.btnLike);
                btnReply    = v.findViewById(R.id.btnReply);
            }
        }
    }
}
