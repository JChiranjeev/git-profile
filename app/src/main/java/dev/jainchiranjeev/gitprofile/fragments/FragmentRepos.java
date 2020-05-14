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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jainchiranjeev.gitprofile.R;
import dev.jainchiranjeev.gitprofile.models.GitRepoModel;
import dev.jainchiranjeev.gitprofile.presenters.GitReposRVAdapter;
import dev.jainchiranjeev.gitprofile.utils.JSONParser;
import dev.jainchiranjeev.gitprofile.viewmodels.GitReposViewModel;

public class FragmentRepos extends Fragment {

    @BindView(R.id.cl_repos_list)
    ConstraintLayout clReposList;
    @BindView(R.id.cl_repos_loading)
    ConstraintLayout clReposLoading;
    @BindView(R.id.rv_repos_list)
    RecyclerView rvReposLoading;

    View view;
    FragmentManager manager;
    FragmentTransaction transaction;
    Context context;
    GitReposViewModel gitReposViewModel;
    List<GitRepoModel> reposList;
    String reposListJson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repos, container, false);
        ButterKnife.bind(this,view);

        manager = getFragmentManager();
        context = getContext();

        Bundle bundle = getArguments();
        if(bundle == null) {
            manager.popBackStack();
        } else {
            reposListJson = bundle.getString("GitReposListJson");
            if(reposListJson == null || reposListJson.isEmpty()) {
                manager.popBackStack();
            } else {
                clReposList.setVisibility(View.GONE);
                clReposLoading.setVisibility(View.VISIBLE);
                loadReposList(context, reposListJson);
            }
        }

        return view;
    }

    private void loadReposList(Context context, String reposListJson) {
        GitReposRVAdapter adapter = new GitReposRVAdapter(context, reposListJson);
        rvReposLoading.setAdapter(adapter);
        rvReposLoading.setLayoutManager(new LinearLayoutManager(context));
        clReposList.setVisibility(View.VISIBLE);
        clReposLoading.setVisibility(View.GONE);
    }
}
