package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.my.vkclient.R;

public class SettingsFragment extends Fragment {

    private View.OnClickListener onLogoutClickListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        Button logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLogoutClickListener != null) {
                    onLogoutClickListener.onClick(view);
                }
            }
        });
    }

    public void setOnLogoutClickListener(View.OnClickListener onLogoutClickListener) {
        this.onLogoutClickListener = onLogoutClickListener;
    }
}
