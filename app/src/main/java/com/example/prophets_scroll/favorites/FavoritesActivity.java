package com.example.prophets_scroll.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prophets_scroll.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FavoritesActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initViews();
        setupToolbar();
        setupViewPager();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewPager() {
        viewPager.setAdapter(new FavoritesPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.tab_devotionals);
                    break;
                case 1:
                    tab.setText(R.string.tab_songs);
                    break;
            }
        }).attach();
    }

    private class FavoritesPagerAdapter extends FragmentStateAdapter {

        public FavoritesPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return FavoriteListFragment.newInstance(
                    position == 0 ? FavoriteItem.Type.DEVOTIONAL : FavoriteItem.Type.SONG
            );
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
