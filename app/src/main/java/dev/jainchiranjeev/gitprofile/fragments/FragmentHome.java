package dev.jainchiranjeev.gitprofile.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.jainchiranjeev.gitprofile.R;

public class FragmentHome extends Fragment implements View.OnClickListener {

    @BindView(R.id.cl_fragment_home)
    ConstraintLayout clFragmentHome;
    @BindView(R.id.fab_submit_username)
    FloatingActionButton fabSubmitUsername;
    @BindView(R.id.et_username)
    AppCompatEditText etUsername;
    @BindView(R.id.iv_github_icon)
    AppCompatImageView ivGithubIcon;
    @BindView(R.id.iv_about_button)
    AppCompatImageView ivAboutButton;
    @BindView(R.id.tv_intro)
    MaterialTextView tvIntro;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    View view;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

//        Focus on Username Edittext
        etUsername.requestFocus();

//        Set Github Icon on FragmentHome
        Glide.with(view).load(R.drawable.ic_app_icon).fitCenter().into(ivGithubIcon);
        Glide.with(view).load(R.drawable.ic_about).fitCenter().into(ivAboutButton);

//        Handle Clicks
        fabSubmitUsername.setOnClickListener(this);
        ivAboutButton.setOnClickListener(this);

//        Listen to EditText for enter press
        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                If user pressed enter
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    username = etUsername.getText().toString();
                    if (username.equals(null) || username.length() == 0) {
//                        Display SnackBar if username field empty
                        Snackbar.make(view, "Please Enter a Username", Snackbar.LENGTH_SHORT).show();
                    } else {
//                        Load ProfileFragment if username entered
                        loadProfileFragment(username);
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            Handle submit button click
            case R.id.fab_submit_username:
                username = etUsername.getText().toString();
                if (username.equals(null) || username.length() == 0) {
//                    Display SnackBar if username field empty
                    Snackbar.make(view, "Please Enter a Username", Snackbar.LENGTH_SHORT).show();
                } else {
//                    Load ProfileFragment if username entered
                    loadProfileFragment(username);
                }
                break;
            case R.id.iv_about_button:
                FragmentAbout about = new FragmentAbout();
                fragmentTransaction = fragmentManager.beginTransaction();
//                Add animations
                about.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                about.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
                fragmentTransaction.addSharedElement(ivAboutButton, "transition_about_icon");
                fragmentTransaction.addSharedElement(ivGithubIcon, "transition_about_app_icon");
                fragmentTransaction.replace(R.id.main_activity_frame_layout, about);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }

    //    Load ProfileFragment
    private void loadProfileFragment(String username) {
//        Send username to ProfileFragment in bundle
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        FragmentProfile profile = new FragmentProfile();
        profile.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
//        Add animations
        profile.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
        profile.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
        setExitTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.basic_transition));
        fragmentTransaction.addSharedElement(fabSubmitUsername, "transition_profile_pic");
        fragmentTransaction.addSharedElement(tvIntro, "transition_profile_username");
        fragmentTransaction.replace(R.id.main_activity_frame_layout, profile);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
