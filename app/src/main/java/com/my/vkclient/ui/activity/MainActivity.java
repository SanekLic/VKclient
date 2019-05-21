package com.my.vkclient.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.my.vkclient.R;
import com.my.vkclient.ui.fragment.FriendsFragment;
import com.my.vkclient.ui.fragment.NewsFragment;
import com.my.vkclient.ui.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private NewsFragment newsFragment;
    private FriendsFragment friendsFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupFragment();
        setupBottomNavigation();
    }

    private void setupFragment() {
        newsFragment = new NewsFragment();
        friendsFragment = new FriendsFragment();
        settingsFragment = new SettingsFragment();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.action_news:
                        fragmentTransaction.replace(R.id.contentFrameLayout, newsFragment);

                        break;
                    case R.id.action_friends:
                        fragmentTransaction.replace(R.id.contentFrameLayout, friendsFragment);

                        break;
                    case R.id.action_settings:
                        fragmentTransaction.replace(R.id.contentFrameLayout, settingsFragment);

                        break;
                }

                fragmentTransaction.commit();
                return true;
            }
        });
    }
}
