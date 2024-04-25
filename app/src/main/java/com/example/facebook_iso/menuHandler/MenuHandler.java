package com.example.facebook_iso.menuHandler;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.example.facebook_iso.Feed_Page;
import com.example.facebook_iso.Login_Page;
import com.example.facebook_iso.NewPost;
import com.example.facebook_iso.R;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.api_manager.api_service;
import com.example.facebook_iso.api_manager.constants;
import com.example.facebook_iso.common.CurrentUserManager;
import com.example.facebook_iso.common.SharedPreferencesManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.UserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MenuHandler {

    private final View menuView;
    private final RecyclerView lstPosts;
    private PostsListAdapter adapter;
    private final String user_name;
    private PopupWindow popupWindow;
    private boolean isNightMode;
    private String userFullName;
    private Uri user_photo;

    public MenuHandler(View menuView, RecyclerView lstPosts, PostsListAdapter adapter, String user_name, PopupWindow popupWindow, String userFullName , Uri user_photo) {
        this.menuView = menuView;
        this.lstPosts = lstPosts;
        this.adapter = adapter;
        this.user_name = user_name;
        this.popupWindow = popupWindow;
        this.userFullName = userFullName;
        this.user_photo = user_photo;
        setupMenu();
    }

    private void setupMenu() {
        // Find the buttons and text views in the menu layout
        ImageButton homeButton = menuView.findViewById(R.id.HomeButton);
        TextView homeText = menuView.findViewById(R.id.HomeText);
        ImageButton newPostButton = menuView.findViewById(R.id.NewPostButton);
        TextView newPostText = menuView.findViewById(R.id.NewPostText);
        ImageButton myAccountButton = menuView.findViewById(R.id.MyAccountButton);
        TextView myAccountText = menuView.findViewById(R.id.MyAccountText);
        ImageButton logOutButton = menuView.findViewById(R.id.LogOutButton);
        TextView logOutText = menuView.findViewById(R.id.LogOutText);
        Switch ChangeThemeSwitcher = menuView.findViewById(R.id.ChangeThemeSwitcher);
        TextView ChangeThemeText = menuView.findViewById(R.id.ChangeThemeText);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            this.isNightMode = false;
        } else {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                isNightMode = true;
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isNightMode = false;
            }
        }


        // Set click listeners for the buttons and text views
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeOperation();
            }
        });

        homeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeOperation();
            }
        });

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPostOperation(v);
            }
        });

        newPostText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPostOperation(v);
            }
        });

        myAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccountOperation(v);
            }
        });

        myAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccountOperation(v);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutOperation(v);
            }
        });
        logOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logOutOperation(v);
            }
        });

        ChangeThemeSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeThemeSwitcher.setChecked(isNightMode); // Set the switcher's checked state
                changeThemeOperation();
            }
        });
        ChangeThemeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeThemeSwitcher.setChecked(isNightMode); // Set the switcher's checked state
                changeThemeOperation();
            }
        });


    }

    private void homeOperation() {
        popupWindow.dismiss();
        lstPosts.smoothScrollToPosition(0);
    }

    private void newPostOperation(View v) {
        popupWindow.dismiss();
        lstPosts.smoothScrollToPosition(0);
        NewPost newPost = new NewPost(adapter, lstPosts, v, user_name, user_photo);
    }

    private void myAccountOperation(View view) {
        popupWindow.dismiss();
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.myaccount, null);
        PopupWindow popupWindowNewPost = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0] + view.getWidth() / 2 - popupWindowNewPost.getWidth() / 2;
        int y = view.getHeight() * 5;
        popupWindowNewPost.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, x, y);
        TextView userName0 = view.findViewById(R.id.userName);
        ImageView userPhoto0 = view.findViewById(R.id.userPhoto);
        TextView userFullName0 = view.findViewById(R.id.userFullName);
        userName0.setText(user_name);
        userPhoto0.setImageURI(user_photo);
        userFullName0.setText(userFullName);
    }

    private void logOutOperation(View v) {
        SharedPreferencesManager.clearAllPreferences(v.getContext());
        Intent intent = new Intent(v.getContext(), Login_Page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
        ((Activity) v.getContext()).finish();
    }


    private void changeThemeOperation() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isNightMode = false;

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
}
}