package dev.jainchiranjeev.gitprofile.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jainchiranjeev.gitprofile.R;

public class FragmentAbout extends Fragment implements View.OnClickListener {

    @BindView(R.id.iv_about_app_icon)
    AppCompatImageView ivAboutAppIcon;
    @BindView(R.id.ib_so)
    AppCompatImageButton ibSo;
    @BindView(R.id.ib_github)
    AppCompatImageButton ibGithub;
    @BindView(R.id.ib_linkedin)
    AppCompatImageButton ibLinkedIn;
    @BindView(R.id.ib_twitter)
    AppCompatImageButton ibTwitter;
    @BindView(R.id.ib_website)
    AppCompatImageButton ibWebsite;
    @BindView(R.id.ib_send_email)
    AppCompatImageButton ibSendEmail;
    @BindView(R.id.ib_butterknife_github) AppCompatImageButton ibButterknifeGithub;
    @BindView(R.id.ib_butterknife_website) AppCompatImageButton ibButterKnifeWebsite;
    @BindView(R.id.ib_glide_github) AppCompatImageButton ibGlideGithub;
    @BindView(R.id.ib_glide_website) AppCompatImageButton ibGlideWebsite;
    @BindView(R.id.ib_avloading_github) AppCompatImageButton ibAvloadingGithub;
    @BindView(R.id.ib_avloading_website) AppCompatImageButton ibAvloadingWebsite;
    @BindView(R.id.ib_mpchart_github) AppCompatImageButton ibMpchartGithub;
    @BindView(R.id.ib_mpchart_website) AppCompatImageButton ibMpchartWebsite;

    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        context = getContext();

        Glide.with(view).load(R.drawable.ic_app_icon).fitCenter().into(ivAboutAppIcon);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(ibGithub);
        Glide.with(view).load(R.drawable.ic_linkedin).fitCenter().into(ibLinkedIn);
        Glide.with(view).load(R.drawable.ic_stackoverflow).fitCenter().into(ibSo);
        Glide.with(view).load(R.drawable.ic_twitter).fitCenter().into(ibTwitter);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(ibWebsite);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(ibWebsite);
        Glide.with(view).load(R.drawable.ic_email).fitCenter().into(ibSendEmail);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(ibButterknifeGithub);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(ibButterKnifeWebsite);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(ibGlideGithub);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(ibGlideWebsite);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(ibAvloadingGithub);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(ibAvloadingWebsite);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(ibMpchartGithub);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(ibMpchartWebsite);

        ibGithub.setOnClickListener(this);
        ibLinkedIn.setOnClickListener(this);
        ibSo.setOnClickListener(this);
        ibTwitter.setOnClickListener(this);
        ibWebsite.setOnClickListener(this);
        ibSendEmail.setOnClickListener(this);
        ibButterknifeGithub.setOnClickListener(this);
        ibButterKnifeWebsite.setOnClickListener(this);
        ibGlideGithub.setOnClickListener(this);
        ibGlideWebsite.setOnClickListener(this);
        ibAvloadingGithub.setOnClickListener(this);
        ibAvloadingWebsite.setOnClickListener(this);
        ibMpchartGithub.setOnClickListener(this);
        ibMpchartWebsite.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        CustomTabsIntent intent = builder.build();
        String url;
        switch (view.getId()) {
            case R.id.ib_linkedin:
                url = context.getString(R.string.url_linkedin);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_website:
                url = context.getString(R.string.url_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_so:
                url = context.getString(R.string.url_so);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_twitter:
                url = context.getString(R.string.url_twitter);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_github:
                url = context.getString(R.string.url_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_send_email:
                url = context.getString(R.string.url_email);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_butterknife_github:
                url = context.getString(R.string.url_butterknife_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_butterknife_website:
                url = context.getString(R.string.url_butterknife_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_glide_github:
                url = context.getString(R.string.url_glide_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_glide_website:
                url = context.getString(R.string.url_glide_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_avloading_github:
                url = context.getString(R.string.url_avloading_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_avloading_website:
                url = context.getString(R.string.url_avloading_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_mpchart_github:
                url = context.getString(R.string.url_mpchart_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_mpchart_website:
                url = context.getString(R.string.url_mpchart_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
        }
    }
}
