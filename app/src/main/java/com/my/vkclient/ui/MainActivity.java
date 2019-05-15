package com.my.vkclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.my.vkclient.ui.utils.FriendRecyclerViewAdapter;
import com.my.vkclient.R;
import com.my.vkclient.ResultCallback;
import com.my.vkclient.entities.User;

public class MainActivity extends AppCompatActivity {

    public static final String USER_INTENT_KEY = "User";
    private RecyclerView friendRecyclerView;
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        friendRecyclerView = findViewById(R.id.friendRecyclerView);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(linearLayoutManager);
        friendRecyclerViewAdapter.setOnItemClickListener(new ResultCallback<User>() {
            @Override
            public void onResult(User user) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra(USER_INTENT_KEY, user);
                startActivity(intent);
            }
        });
        friendRecyclerView.setAdapter(friendRecyclerViewAdapter);

        friendRecyclerViewAdapter.initialLoadItems();
        friendRecyclerViewAdapter.autoUpdateItems(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (friendRecyclerViewAdapter != null) {
            friendRecyclerViewAdapter.autoUpdateItems(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (friendRecyclerViewAdapter != null) {
            friendRecyclerViewAdapter.autoUpdateItems(false);
        }
    }
}
