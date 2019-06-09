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

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.ui.adapters.ViewPagerAdapter;
import com.my.vkclient.ui.fragment.FriendsFragment;
import com.my.vkclient.ui.fragment.NewsFragment;
import com.my.vkclient.ui.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager contentViewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupViewPager();
        setupBottomNavigation();
    }

    public static void show(final Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void setupViewPager() {
        contentViewPager = findViewById(R.id.contentViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NewsFragment());
        viewPagerAdapter.addFragment(new FriendsFragment());
        viewPagerAdapter.addFragment(new SettingsFragment());
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
