package com.my.vkclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.my.vkclient.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getView());

        setupBottomNavigation();
    }

    protected abstract int getView();

    private void setupBottomNavigation() {
        BottomNavigationItemView action_news = findViewById(R.id.action_news);
        BottomNavigationItemView action_friends = findViewById(R.id.action_friends);
        BottomNavigationItemView action_settings = findViewById(R.id.action_settings);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (!(BaseActivity.this instanceof NewsActivity)) {
            action_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this, NewsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_news);
        }
        if (!(BaseActivity.this instanceof FriendsActivity)) {
            action_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this, FriendsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_friends);
        }
        if (!(BaseActivity.this instanceof SettingsActivity)) {
            action_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_settings);
        }
    }
}
