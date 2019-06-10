package com.my.vkclient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.my.vkclient.R;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.adapters.ViewPagerAdapter;
import com.my.vkclient.ui.fragment.FriendsFragment;
import com.my.vkclient.ui.fragment.NewsFragment;
import com.my.vkclient.ui.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager contentViewPager;
    private BottomNavigationView bottomNavigationView;
    private NewsFragment newsFragment;
    private FriendsFragment friendsFragment;
    private SettingsFragment settingsFragment;

    public static void show(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupFragments();
        setupViewPager();
        setupBottomNavigation();
    }

    private void setupViewPager() {
        contentViewPager = findViewById(R.id.contentViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(newsFragment);
        viewPagerAdapter.addFragment(friendsFragment);
        viewPagerAdapter.addFragment(settingsFragment);
        contentViewPager.setAdapter(viewPagerAdapter);
        contentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bottomNavigationView.setSelected(false);
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setupFragments() {
        newsFragment = new NewsFragment();
        friendsFragment = new FriendsFragment();
        settingsFragment = new SettingsFragment();
        settingsFragment.setOnLogoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VkRepository.getInstance().logout();

                clearCookies();

                LoginActivity.show(MainActivity.this);

                finish();
            }
        });
    }

    private void clearCookies() {
        CookieSyncManager.createInstance(MainActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_news:
                        contentViewPager.setCurrentItem(0);

                        return true;
                    case R.id.action_friends:
                        contentViewPager.setCurrentItem(1);

                        return true;
                    case R.id.action_settings:
                        contentViewPager.setCurrentItem(2);

                        return true;
                }

                return false;
            }
        });
    }
}
