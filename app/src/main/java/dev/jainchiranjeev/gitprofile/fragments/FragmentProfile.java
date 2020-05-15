package dev.jainchiranjeev.gitprofile.fragments;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jainchiranjeev.gitprofile.R;
import dev.jainchiranjeev.gitprofile.models.GitProfileModel;
import dev.jainchiranjeev.gitprofile.models.GitRepoModel;
import dev.jainchiranjeev.gitprofile.utils.GraphDataParser;
import dev.jainchiranjeev.gitprofile.utils.JSONParser;
import dev.jainchiranjeev.gitprofile.utils.NetworkCheck;
import dev.jainchiranjeev.gitprofile.viewmodels.GitProfileViewModel;
import dev.jainchiranjeev.gitprofile.viewmodels.GitReposViewModel;

public class FragmentProfile extends Fragment implements View.OnClickListener {

    @BindView(R.id.cl_fragment_profile)
    ConstraintLayout clFragmentProfile;
    @BindView(R.id.iv_profile_pic)
    AppCompatImageView ivProfilePic;
    @BindView(R.id.tv_username)
    MaterialTextView tvUsername;
    @BindView(R.id.tv_location)
    MaterialTextView tvLocation;
    @BindView(R.id.cl_loading)
    ConstraintLayout clLoading;
    @BindView(R.id.cl_content)
    ConstraintLayout clContent;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.av_content_loading)
    AVLoadingIndicatorView avContentLoading;
    @BindView(R.id.iv_location_icon)
    AppCompatImageView ivLocationIcon;
    @BindView(R.id.iv_calendar_icon)
    AppCompatImageView ivCalendarIcon;
    @BindView(R.id.tv_joining_date)
    MaterialTextView tvJoiningDate;
    @BindView(R.id.tv_repos)
    MaterialTextView tvRepos;
    @BindView(R.id.tv_followers)
    MaterialTextView tvFollowers;
    @BindView(R.id.tv_following)
    MaterialTextView tvFollowing;
    @BindView(R.id.cl_graphs)
    ConstraintLayout clGraphs;
    @BindView(R.id.cl_graphs_loading)
    ConstraintLayout clGraphsLoading;
    @BindView(R.id.av_graphs_loading)
    AVLoadingIndicatorView avGraphsLoading;
    @BindView(R.id.lang_graph)
    PieChart langGraph;
    @BindView(R.id.cl_user_not_found)
    ConstraintLayout clUserNotFound;
    @BindView(R.id.iv_user_not_found)
    AppCompatImageView ivUserNotFound;
    @BindView(R.id.iv_work_icon)
    AppCompatImageView ivWorkIcon;
    @BindView(R.id.tv_work)
    MaterialTextView tvWork;
    @BindView(R.id.stars_graph)
    BarChart starsGraph;
    @BindView(R.id.cl_disconnected)
    ConstraintLayout clDisconnected;
    @BindView(R.id.cv_repo_count)
    MaterialCardView cvRepoCount;
    @BindView(R.id.cv_followers_count)
    MaterialCardView cvFollowersCount;
    @BindView(R.id.cv_following_count)
    MaterialCardView cvFollowingCount;
    @BindView(R.id.ll_toolbar) LinearLayout llToolbar;


    View view;
    Bundle bundle = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    boolean profileLoaded = false;
    boolean graphsLoaded = false;
    boolean userFound = false;
    Context context;
    String reposListJson;
    List<GitRepoModel> reposList;
    String username;
    FragmentProfile profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        context = getContext();
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        displayContent(profileLoaded);
        displayGraphs(graphsLoaded);

//        Check if Username attached to bundle. Get user's profile data if username available. If not, go back.
        bundle = getArguments();
        if (bundle == null) {
            fragmentManager.popBackStack();
        } else {
            username = bundle.getString("username");
            tvUsername.setText("@" + username);
            if (username != null || username.length() > 0) {
//                Check Internet connection before API call
                if (new NetworkCheck(context).isNetworkAvailable()) {
                    getUserData(username);
                } else {
                    clDisconnected.setVisibility(View.VISIBLE);
                    clUserNotFound.setVisibility(View.GONE);
                    llToolbar.setVisibility(View.GONE);
                    svContent.setVisibility(View.GONE);
                    clLoading.setVisibility(View.GONE);
                }
            }
        }

        cvRepoCount.setOnClickListener(this);
        cvFollowersCount.setOnClickListener(this);
        cvFollowingCount.setOnClickListener(this);

        return view;
    }

    //    Connect the Profile ViewModel and get data from Github API.
    private void getUserData(String username) {
        GitProfileViewModel gitProfileViewModel = ViewModelProviders.of(this).get(GitProfileViewModel.class);
        gitProfileViewModel.getGitProfile(context, username).observe(this, data -> {
            if (data != null) {
//                Map profile data to views
                handleProfileData(data);
//                Get repos data for user
                getUserReposData(username);
//                Display content if profile data received
                displayContent(true);
            } else {
//                Display 404 if user not found
                display404(true);
            }
        });
    }

    //    Map profile data to views
    private void handleProfileData(GitProfileModel model) {
//        Format Date in required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
//        Load Images using Glide
        Glide.with(view).load(model.avatarUrl).fitCenter().into(ivProfilePic);
        Glide.with(view).load(R.drawable.ic_calendar).fitCenter().into(ivCalendarIcon);
        tvRepos.setText(String.valueOf(model.publicRepos));
        tvFollowers.setText(String.valueOf(model.followers));
        tvFollowing.setText(String.valueOf(model.following));
        tvJoiningDate.setText("Joined: " + dateFormat.format(model.createdAt));

//        Check location data and show/hide accordingly
        if (model.location == null || model.location.equals("null")) {
            tvLocation.setVisibility(View.GONE);
            ivLocationIcon.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.VISIBLE);
            ivLocationIcon.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_location).fitCenter().into(ivLocationIcon);
            tvLocation.setText(model.location);
        }

//        Check work data and show/hide accordingly
        if (model.company == null || model.company.equals("null")) {
            tvWork.setVisibility(View.GONE);
            ivWorkIcon.setVisibility(View.GONE);
        } else {
            tvWork.setVisibility(View.VISIBLE);
            ivWorkIcon.setVisibility(View.VISIBLE);
            Glide.with(view).load(R.drawable.ic_work).fitCenter().into(ivWorkIcon);
            tvWork.setText(model.company);
        }
    }

    //    Display 404 error if username not found
    private void display404(boolean userNotFound) {
        if (userNotFound) {
            tvUsername.setText("Not Found");
//            Hide other views and display 404 if user not found
            svContent.setVisibility(View.GONE);
            clLoading.setVisibility(View.GONE);
            llToolbar.setVisibility(View.GONE);
//            Load 404 image into view
            Glide.with(view).load(R.drawable.ic_404).fitCenter().into(ivUserNotFound);
            clUserNotFound.setVisibility(View.VISIBLE);
        } else {
//            Revert if username found
            clUserNotFound.setVisibility(View.GONE);
            displayContent(true);
        }
    }

    //    Display user profile content
    private void displayContent(boolean contentLoaded) {
        if (contentLoaded) {
//            Display profile and hide loader animation, 404 error views
            svContent.setVisibility(View.VISIBLE);
            llToolbar.setVisibility(View.VISIBLE);
            clUserNotFound.setVisibility(View.GONE);
            clLoading.setVisibility(View.GONE);
            avContentLoading.smoothToHide();
        } else {
//            Display loader and hide profile, 404 error views
            llToolbar.setVisibility(View.VISIBLE);
            clLoading.setVisibility(View.VISIBLE);
            svContent.setVisibility(View.GONE);
            clUserNotFound.setVisibility(View.GONE);
            avContentLoading.smoothToShow();
        }
    }

    //    Display the graph of languages received from user repository model
    private void displayGraphs(boolean graphsLoaded) {
        if (graphsLoaded) {
//            Display graphs and hide loader view
            clGraphsLoading.setVisibility(View.GONE);
            svContent.setVisibility(View.VISIBLE);
            avGraphsLoading.smoothToHide();
        } else {
//            Display loader and hide graph view
            clGraphsLoading.setVisibility(View.VISIBLE);
            svContent.setVisibility(View.GONE);
            avGraphsLoading.smoothToShow();
        }
    }

    //    Connect to repositorys ViewModel and get repository data for user after profile data received
    private void getUserReposData(String username) {
        GitReposViewModel gitReposViewModel = ViewModelProviders.of(this).get(GitReposViewModel.class);
        gitReposViewModel.getGitReposList(context, username).observe(this, data -> {
//            Display the graph and set values
            clGraphs.setVisibility(View.VISIBLE);
            reposListJson = data;
            reposList = new JSONParser().getReposFromJson(data);
            handleReposData(reposList);
            displayGraphs(true);
        });
    }

    //    Manage the repository data for user
    private void handleReposData(List<GitRepoModel> modelList) {
//        Load language data from repository model into Graph and display graph
        GraphDataParser graphDataParser = new GraphDataParser(context);
        PieData pieData = new PieData(graphDataParser.getPieData(modelList));
        BarData barData = new BarData(graphDataParser.getBarData(modelList));
        List<LegendEntry> barLegendEntries = graphDataParser.getTopReposNames(modelList);
        createPieGraph(pieData);
        createBarGraph(barData, barLegendEntries);
    }

    //    Create and Render the PieGraph
    private void createPieGraph(PieData data) {
        data.setValueFormatter(new PercentFormatter(langGraph));
        data.setValueTextSize(12f);
        data.setValueTextColor(ContextCompat.getColor(context, R.color.fontColor));
        langGraph.setData(data);
        langGraph.setExtraOffsets(4, 12, 4, 8);
        langGraph.setTransparentCircleAlpha(0);
        langGraph.setUsePercentValues(true);
        langGraph.getLegend().setEnabled(false);
        langGraph.getDescription().setEnabled(false);
//        langGraph.getDescription().setText("Languages");
//        langGraph.getDescription().setTextSize(14f);
//        langGraph.getDescription().setTextColor(ContextCompat.getColor(context, R.color.fontColor));
        langGraph.setHoleRadius(0f);
        langGraph.setEntryLabelColor(ContextCompat.getColor(context, R.color.fontColor));
        langGraph.animateXY(500, 500);
//        Refresh Graph
        langGraph.invalidate();
    }

    //    Create and Render the BarGraph
    private void createBarGraph(BarData barData, List<LegendEntry> barLegendEntries) {
//        barData.setBarWidth(16f);
        barData.setValueTextColor(ContextCompat.getColor(context, R.color.fontColor));
        starsGraph.setData(barData);
        starsGraph.setFitBars(true);
        starsGraph.setExtraOffsets(8, 40, 8, 8);
        starsGraph.getLegend().setEnabled(true);
        starsGraph.getLegend().setTextColor(ContextCompat.getColor(context, R.color.fontColor));
        starsGraph.animateXY(500, 500);
        starsGraph.getDescription().setEnabled(false);
        starsGraph.getAxis(YAxis.AxisDependency.LEFT).setTextColor(ContextCompat.getColor(context, R.color.fontColor));
        starsGraph.getAxis(YAxis.AxisDependency.RIGHT).setTextColor(ContextCompat.getColor(context, R.color.fontColor));
        starsGraph.getXAxis().setTextColor(ContextCompat.getColor(context, R.color.fontColor));
        starsGraph.getLegend().setCustom(barLegendEntries);
        starsGraph.getLegend().setWordWrapEnabled(true);
//        Refresh Graph
        starsGraph.invalidate();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = null;
        FragmentFollowers fragmentFollowers;
        switch (view.getId()) {
            case R.id.cv_repo_count:
                bundle = new Bundle();
                bundle.putString("GitReposListJson", reposListJson);
                FragmentRepos fragmentRepos = new FragmentRepos();
                fragmentRepos.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                fragmentTransaction.replace(R.id.main_activity_frame_layout, fragmentRepos);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bundle = null;
                break;
            case R.id.cv_followers_count:
                cvFollowersCount.setTransitionName("transition_followers_title");
                bundle = new Bundle();
                bundle.putString("Username", username);
                bundle.putString("FragmentToLoad", "Followers");
                fragmentFollowers = new FragmentFollowers();
                fragmentFollowers.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
//                Add Animations
                fragmentFollowers.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                fragmentFollowers.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                fragmentTransaction.addSharedElement(cvFollowersCount, "transition_followers_title");

                fragmentTransaction.replace(R.id.main_activity_frame_layout, fragmentFollowers);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bundle = null;
                fragmentFollowers = null;
                break;
            case R.id.cv_following_count:
                cvFollowingCount.setTransitionName("transition_followers_title");
                bundle = new Bundle();
                bundle.putString("Username", username);
                bundle.putString("FragmentToLoad", "Following");
                fragmentFollowers = new FragmentFollowers();
                fragmentFollowers.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
//                Add Animations
                fragmentFollowers.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                fragmentFollowers.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                fragmentTransaction.addSharedElement(cvFollowingCount, "transition_followers_title");

                fragmentTransaction.replace(R.id.main_activity_frame_layout, fragmentFollowers);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bundle = null;
                break;
        }
    }
}
