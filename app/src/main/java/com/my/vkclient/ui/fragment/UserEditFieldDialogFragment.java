package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.SharedPreferencesHelper;

import static com.my.vkclient.Constants.IntentKey.EDIT_CITY_INTENT_KEY;
import static com.my.vkclient.Constants.IntentKey.EDIT_STATUS_INTENT_KEY;

public class UserEditFieldDialogFragment extends BaseDialogFragment {
    private TextView fieldLabelTextView;
    private EditText fieldEditText;
    private boolean editStatus;
    private boolean editCity;
    private User user;
    private ResultCallback<User> onApplyEditField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_edit_user_field, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        setupView(view);

        Bundle bundle = getArguments();

        if (bundle != null) {
            editStatus = bundle.getBoolean(EDIT_STATUS_INTENT_KEY);
            editCity = bundle.getBoolean(EDIT_CITY_INTENT_KEY);
            VkRepository.getInstance().getUser(SharedPreferencesHelper.getInstance().getProfileId(), new ResultCallback<User>() {
                @Override
                public void onResult(User result) {
                    if (result != null) {
                        setDate(result);
                    }
                }
            });
        }
    }

    public void setOnApplyEditField(ResultCallback<User> onApplyEditField) {
        this.onApplyEditField = onApplyEditField;
    }

    private void setDate(User result) {
        user = result;
        if (editStatus) {
            fieldLabelTextView.setText(R.string.status_label);
            fieldEditText.setText(user.getStatus());
            fieldEditText.setHint(R.string.status_label);
        } else if (editCity) {
            fieldLabelTextView.setText(R.string.home_town_label);
            fieldEditText.setText(user.getHomeTown());
            fieldEditText.setHint(R.string.home_town_label);
        }
    }

    private void setupView(@NonNull View view) {
        fieldLabelTextView = view.findViewById(R.id.fieldLabelTextView);
        fieldEditText = view.findViewById(R.id.fieldEditText);
        
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onApplyEditField != null) {
                    if (editStatus) {
                        VkRepository.getInstance().setStatusToUser(user, fieldEditText.getText().toString(),
                                new ResultCallback<User>() {
                                    @Override
                                    public void onResult(User result) {
                                        onApplyEditField.onResult(user);
                                        getDialog().dismiss();
                                    }
                                });
                    } else if (editCity) {
                        VkRepository.getInstance().setHomeTownToUser(user, fieldEditText.getText().toString(),
                                new ResultCallback<User>() {
                                    @Override
                                    public void onResult(User result) {
                                        onApplyEditField.onResult(user);
                                        getDialog().dismiss();
                                    }
                                });
                    }
                }
            }
        });
    }
}
