package com.my.vkclient.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.vkclient.R;
import com.my.vkclient.entities.User;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import static com.my.vkclient.Constants.IntentKey.USER_ID_INTENT_KEY;

public class UserInfoDialogFragment extends DialogFragment {
    TextView userStatusTextView;
    TextView userHomeTownTextView;
    TextView userUniversityTextView;
    TextView userFacultyTextView;
    TextView userInterestsTextView;
    TextView userMusicTextView;
    TextView userMovieTextView;
    TextView userGamesTextView;
    TextView userAboutTextView;
    TextView userStatusLabelTextView;
    TextView userHomeTownLabelTextView;
    TextView userEducationLabelTextView;
    TextView userInterestsLabelTextView;
    TextView userMusicLabelTextView;
    TextView userMovieLabelTextView;
    TextView userGamesLabelTextView;
    TextView userAboutLabelTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView(view);

        Bundle bundle = getArguments();

        if (bundle != null) {
            int userId = bundle.getInt(USER_ID_INTENT_KEY);
            VkRepository.getInstance().getUser(userId, new ResultCallback<User>() {
                @Override
                public void onResult(User result) {
                    setUser(result);
                }
            });
        }
    }

    public void setUser(User user) {
        Utils.getInstance().setInfoAndVisibilityFieldView(userStatusTextView, userStatusLabelTextView, user.getStatus());
        Utils.getInstance().setInfoAndVisibilityFieldView(userHomeTownTextView, userHomeTownLabelTextView, user.getHomeTown());
        Utils.getInstance().setInfoAndVisibilityFieldView(userUniversityTextView, userEducationLabelTextView, user.getUniversityName());
        Utils.getInstance().setInfoAndVisibilityFieldView(userFacultyTextView, userEducationLabelTextView, user.getFacultyName());
        Utils.getInstance().setInfoAndVisibilityFieldView(userInterestsTextView, userInterestsLabelTextView, user.getInterests());
        Utils.getInstance().setInfoAndVisibilityFieldView(userMusicTextView, userMusicLabelTextView, user.getMusic());
        Utils.getInstance().setInfoAndVisibilityFieldView(userMovieTextView, userMovieLabelTextView, user.getMovies());
        Utils.getInstance().setInfoAndVisibilityFieldView(userGamesTextView, userGamesLabelTextView, user.getGames());
        Utils.getInstance().setInfoAndVisibilityFieldView(userAboutTextView, userAboutLabelTextView, user.getAbout());
    }

    private void setupView(@NonNull View view) {
        userStatusTextView = view.findViewById(R.id.userStatusTextView);
        userHomeTownTextView = view.findViewById(R.id.userHomeTownTextView);
        userUniversityTextView = view.findViewById(R.id.userUniversityTextView);
        userFacultyTextView = view.findViewById(R.id.userFacultyTextView);
        userInterestsTextView = view.findViewById(R.id.userInterestsTextView);
        userMusicTextView = view.findViewById(R.id.userMusicTextView);
        userMovieTextView = view.findViewById(R.id.userMovieTextView);
        userGamesTextView = view.findViewById(R.id.userGamesTextView);
        userAboutTextView = view.findViewById(R.id.userAboutTextView);

        userStatusLabelTextView = view.findViewById(R.id.userStatusLabelTextView);
        userHomeTownLabelTextView = view.findViewById(R.id.userHomeTownLabelTextView);
        userEducationLabelTextView = view.findViewById(R.id.userEducationLabelTextView);
        userInterestsLabelTextView = view.findViewById(R.id.userInterestsLabelTextView);
        userMusicLabelTextView = view.findViewById(R.id.userMusicLabelTextView);
        userMovieLabelTextView = view.findViewById(R.id.userMovieLabelTextView);
        userGamesLabelTextView = view.findViewById(R.id.userGamesLabelTextView);
        userAboutLabelTextView = view.findViewById(R.id.userAboutLabelTextView);
    }
}
