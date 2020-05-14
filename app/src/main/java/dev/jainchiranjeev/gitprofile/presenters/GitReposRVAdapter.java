package dev.jainchiranjeev.gitprofile.presenters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.List;

import dev.jainchiranjeev.gitprofile.R;
import dev.jainchiranjeev.gitprofile.models.GitRepoModel;
import dev.jainchiranjeev.gitprofile.utils.JSONParser;

public class GitReposRVAdapter extends RecyclerView.Adapter<GitReposRVAdapter.ViewHolder> {

    private List<GitRepoModel> reposList;
    private GitRepoModel repo;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Context context;
    private String reposListJson;
    SimpleDateFormat format;

    public GitReposRVAdapter(Context context, String reposListJson) {
        this.context = context;
        this.reposListJson = reposListJson;
        reposList = new JSONParser().getReposFromJson(reposListJson);
        format = new SimpleDateFormat("dd MMM yyyy");
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvRepoName,tvRepoDesc,tvUpdateDate,tvStarCount,tvWatchersCount;
        ImageButton ibRepoUrl;
        ImageView ivWatcherIcon, ivStarIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRepoName = (MaterialTextView) itemView.findViewById(R.id.tv_repo_name);
            tvRepoDesc = (MaterialTextView) itemView.findViewById(R.id.tv_repo_desc);
            tvUpdateDate = (MaterialTextView) itemView.findViewById(R.id.tv_update_date);
            tvStarCount = (MaterialTextView) itemView.findViewById(R.id.tv_star_count);
            tvWatchersCount = (MaterialTextView) itemView.findViewById(R.id.tv_watchers_count);
            ibRepoUrl = (AppCompatImageButton) itemView.findViewById(R.id.ib_repo_url);
            ivWatcherIcon = (AppCompatImageView) itemView.findViewById(R.id.iv_watcher_icon);
            ivStarIcon = (AppCompatImageView) itemView.findViewById(R.id.iv_star_icon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View repoItem = inflater.inflate(R.layout.rv_item_git_repo, parent, false);

        ViewHolder viewHolder = new ViewHolder(repoItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView).load(R.drawable.ic_launch).into(holder.ibRepoUrl);
        Glide.with(holder.itemView).load(R.drawable.ic_star).fitCenter().into(holder.ivStarIcon);
        Glide.with(holder.itemView).load(R.drawable.ic_eye).fitCenter().into(holder.ivWatcherIcon);

        repo = reposList.get(position);
        holder.tvRepoName.setText(repo.name);
        holder.tvRepoDesc.setText(repo.description);
        holder.tvWatchersCount.setText(repo.watchersCount.toString());
        holder.tvStarCount.setText(repo.stargazersCount.toString());
        holder.tvUpdateDate.setText(format.format(repo.updatedAt));

        repo = null;
    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }
}
