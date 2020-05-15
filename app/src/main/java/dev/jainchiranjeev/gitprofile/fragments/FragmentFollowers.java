package dev.jainchiranjeev.gitprofile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jainchiranjeev.gitprofile.R;
import dev.jainchiranjeev.gitprofile.presenters.GitFollowersRVAdapter;
import dev.jainchiranjeev.gitprofile.viewmodels.GitFollowersViewModel;

public class FragmentFollowers extends Fragment {

    @BindView(R.id.rv_followers_list)
    RecyclerView rvFollowersList;
    @BindView(R.id.tv_followers_toolbar)
    MaterialTextView tvFollowersToolbar;
    @BindView(R.id.cl_loading)
    ConstraintLayout clLoading;

    View view;
    Bundle bundle;
    FragmentManager manager;
    FragmentTransaction transaction;
    Boolean followers;
    Context context;
    GitFollowersViewModel gitFollowersViewModel;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, view);

        manager = getFragmentManager();
        bundle = getArguments();
        context = getContext();

        rvFollowersList.setVisibility(View.GONE);
        clLoading.setVisibility(View.VISIBLE);

        if(bundle == null) {
            manager.popBackStack();
        } else {
            String fragmentToLoad = bundle.getString("FragmentToLoad",null);
            username = bundle.getString("Username", null);
            if(username != null) {
                if(fragmentToLoad.equals("Followers")) {
                    followers = true;
                    tvFollowersToolbar.setText("@" + username + "'s Followers");
                } else if(fragmentToLoad.equals("Following")) {
                    followers = false;
                    tvFollowersToolbar.setText("People Following @" + username);
                } else {
                    manager.popBackStack();
                }
            } else {
                manager.popBackStack();
            }
        }

        loadList(context, username);

        return view;
    }

    private void loadList(Context context, String username) {
        gitFollowersViewModel = ViewModelProviders.of(this).get(GitFollowersViewModel.class);
        gitFollowersViewModel.getGitFollowers(context, username, followers).observe(this, data -> {
            rvFollowersList.setVisibility(View.VISIBLE);
            clLoading.setVisibility(View.GONE);
            GitFollowersRVAdapter adapter = new GitFollowersRVAdapter(context, data);
            rvFollowersList.setAdapter(adapter);
            rvFollowersList.setLayoutManager(new LinearLayoutManager(context));
        });
    }
}
