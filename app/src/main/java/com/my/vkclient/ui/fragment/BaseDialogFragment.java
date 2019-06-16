package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.my.vkclient.R;

import static android.view.Gravity.BOTTOM;
import static com.my.vkclient.Constants.StateKey.SCROLL_VIEW_POSITION_STATE_KEY;

public abstract class BaseDialogFragment extends DialogFragment {
    private ScrollView scrollView;
    private int scrollViewPosition;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Window dialogWindow = getDialog().getWindow();

        if (dialogWindow != null) {
            WindowManager.LayoutParams windowAttributes = dialogWindow.getAttributes();
            windowAttributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
            windowAttributes.gravity = BOTTOM;
            dialogWindow.setAttributes(windowAttributes);
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SCROLL_VIEW_POSITION_STATE_KEY, scrollView.getScrollY());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        scrollView = view.findViewById(R.id.scrollView);

        if (savedInstanceState != null) {
            scrollViewPosition = savedInstanceState.getInt(SCROLL_VIEW_POSITION_STATE_KEY);
        }
    }

    public void restoreScrollPosition() {
        scrollView.scrollTo(0, scrollViewPosition);
    }
}
