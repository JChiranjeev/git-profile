package dev.jainchiranjeev.gitprofile.presenters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import dev.jainchiranjeev.gitprofile.R;
import dev.jainchiranjeev.gitprofile.fragments.FragmentProfile;
import dev.jainchiranjeev.gitprofile.models.GitFollowersModel;
import dev.jainchiranjeev.gitprofile.utils.JSONParser;

public class GitFollowersRVAdapter extends RecyclerView.Adapter<GitFollowersRVAdapter.ViewHolder> {

    private List<GitFollowersModel> followersList;
    private GitFollowersModel follower;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Context context;
    private String followersListJson;
    private String username = null;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatImageView ivProfilePic;
        MaterialTextView tvFollowerUsername;
        AppCompatImageButton ibOpenProfile;
        MaterialCardView cvFollower;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = (AppCompatImageView) itemView.findViewById(R.id.iv_profile_pic);
            tvFollowerUsername = (MaterialTextView) itemView.findViewById(R.id.tv_follower_username);
            ibOpenProfile = (AppCompatImageButton) itemView.findViewById(R.id.ib_open_profile);
            cvFollower = (MaterialCardView) itemView.findViewById(R.id.cv_follower);

            cvFollower.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.cv_follower:
                    int position = getAdapterPosition();
                    username = followersList.get(position).login;
                    if(username != null) {
//                    Send username to ProfileFragment in bundle
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        FragmentProfile profile = new FragmentProfile();
                        profile.setArguments(bundle);
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.main_activity_frame_layout, profile);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    break;
            }
        }
    }

    public GitFollowersRVAdapter(Context context, String followersListJson) {
        this.context = context;
        this.followersList = new JSONParser().getFollowersFromJson(followersListJson);
        manager = ((FragmentActivity)context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View followerView = inflater.inflate(R.layout.rv_item_follower, parent, false);

        ViewHolder viewHolder = new ViewHolder(followerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        follower = followersList.get(position);
        try {
            Glide.with(holder.itemView).load(new URL(follower.avatarUrl)).fitCenter().into(holder.ivProfilePic);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Glide.with(holder.itemView).load(R.drawable.ic_next).fitCenter().into(holder.ibOpenProfile);
        holder.tvFollowerUsername.setText(follower.login);
    }

    @Override
    public int getItemCount() {
        return this.followersList.size();
    }
}
