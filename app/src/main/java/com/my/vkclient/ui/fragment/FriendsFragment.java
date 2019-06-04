package com.my.vkclient.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.entities.User;
import com.my.vkclient.ui.activity.UserActivity;
import com.my.vkclient.ui.utils.FriendRecyclerViewAdapter;

public class FriendsFragment extends Fragment {
    //TODo don't ignore android studio warning
    private FriendRecyclerViewAdapter friendRecyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        RecyclerView friendRecyclerView = view.findViewById(R.id.friendRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerViewAdapter = new FriendRecyclerViewAdapter(linearLayoutManager);
        friendRecyclerViewAdapter.setOnItemClickListener(new ResultCallback<User>() {
            @Override
            public void onResult(User user) {
                Intent intent = new Intent(FriendsFragment.this.getContext(), UserActivity.class);
                intent.putExtra(Constants.USER_ID_INTENT_KEY, user.getId());
                startActivity(intent);
            }
        });

        friendRecyclerView.setAdapter(friendRecyclerViewAdapter);

        friendRecyclerViewAdapter.initialLoadItems();
    }
}
