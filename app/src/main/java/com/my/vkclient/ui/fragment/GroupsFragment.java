package com.my.vkclient.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.vkclient.Constants;
import com.my.vkclient.R;
import com.my.vkclient.entities.Group;
import com.my.vkclient.repository.VkRepository;
import com.my.vkclient.ui.adapters.GroupRecyclerViewAdapter;
import com.my.vkclient.utils.ResultCallback;

import java.util.List;

import static com.my.vkclient.Constants.IntentKey.GROUP_ID_INTENT_KEY;

public class GroupsFragment extends BaseFragment<GroupRecyclerViewAdapter, Group> {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = Constants.RecyclerView.VIEW_MARGIN;
                }
            }
        };
    }

    @Override
    void refreshRepository() {
        VkRepository.getInstance().refreshUserGroups();
    }

    @Override
    void createRecyclerViewAdapter() {
        recyclerViewAdapter = new GroupRecyclerViewAdapter(this.getContext(), linearLayoutManager) {
            @Override
            public void load(int startPosition, int size, ResultCallback<List<Group>> listResultCallback) {
                VkRepository.getInstance().getUserGroups(startPosition, size, listResultCallback);
            }

            @Override
            public void onLoadStateChanged(boolean state) {
                swipeRefresh.setEnabled(!state);
            }
        };
        recyclerViewAdapter.setOnItemClickListener(new ResultCallback<Group>() {
            @Override
            public void onResult(Group group) {
                if (getActivity() != null) {
                    GroupInfoDialogFragment groupInfoDialogFragment = new GroupInfoDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(GROUP_ID_INTENT_KEY, group.getId());
                    groupInfoDialogFragment.setArguments(bundle);
                    groupInfoDialogFragment.show(getActivity().getSupportFragmentManager(), Constants.FragmentTag.GROUP_INFO_FRAGMENT_TAG);
                }
            }
        });
    }

    @Override
    List<Group> getItemsFromRepository() {
        return VkRepository.getInstance().getUserGroupList();
    }
}
