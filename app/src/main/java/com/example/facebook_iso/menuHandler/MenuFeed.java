package com.example.facebook_iso.menuHandler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.Requests;
import com.example.facebook_iso.adapters.FriendsAdapter;
import com.example.facebook_iso.adapters.UsersAdapter;
import com.example.facebook_iso.login.Login_Page;

public class MenuFeed {
    private final FeedPage feedPage;
    public static Activity activity;
    public static Boolean openNewPost;
    public static Boolean openFriendsList;

    public MenuFeed(FeedPage feedPage, Activity activity) {
        this.feedPage = feedPage;
        this.activity = activity;
        this.openNewPost = true;
        this.openFriendsList = false;
    }

    public void setupMenu(ImageButton homeButton, TextView homeText, ImageButton newPostButton,
                          TextView newPostText, ImageButton myAccountButton,
                          TextView myAccountText, ImageButton logOutButton,
                          TextView logOutText, Switch changeThemeSwitcher,
                          TextView changeThemeText, ImageButton friendButton,
                          TextView friendText,ImageButton friendReqButton, ImageButton deleteUser,
                          TextView DeleteText, TextView FriendReqText) {
        homeButton.setOnClickListener(v -> homeOperation());
        homeText.setOnClickListener(v -> homeOperation());
        newPostButton.setOnClickListener(v -> newPostOperation());
        newPostText.setOnClickListener(v -> newPostOperation());

        myAccountButton.setOnClickListener(v -> myAccountOperation());
        myAccountText.setOnClickListener(v -> myAccountOperation());
        logOutButton.setOnClickListener(v -> logOutOperation());
        logOutText.setOnClickListener(v -> logOutOperation());
        FriendReqText.setOnClickListener(v -> friendReqOperation(v));
        friendReqButton.setOnClickListener(v -> friendReqOperation(v));
        DeleteText.setOnClickListener(v ->{
            deleteOperation(v);
        });
        deleteUser.setOnClickListener(v ->{
            deleteOperation(v);
        });

        changeThemeSwitcher.setOnClickListener(v -> {
            changeThemeSwitcher.setChecked(FeedPage.isDarkMode);
            changeThemeOperation();
        });
        changeThemeText.setOnClickListener(v -> {
            changeThemeSwitcher.setChecked(FeedPage.isDarkMode);
            changeThemeOperation();
        });
        friendButton.setOnClickListener(v -> friendOperation());
        friendText.setOnClickListener(v -> friendOperation());
    }

    private void homeOperation() {
        feedPage.changeOpenMenu();
        FeedPage.lstPosts.smoothScrollToPosition(0);
    }
    private void deleteOperation(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(feedPage);
        builder.setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteAccount(v);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void deleteAccount(View v){
        notifyAll();
        Intent intent = new Intent(v.getContext(), Login_Page.class);
        v.getContext().startActivity(intent);
    }

    private void newPostOperation() {
        Intent intent = new Intent(MenuFeed.activity, New_post.class);
        MenuFeed.activity.startActivity(intent);
        FeedPage.lstPosts.smoothScrollToPosition(0);
        feedPage.changeOpenMenu();
    }

    private void myAccountOperation() {
        feedPage.changeOpenMenu();
        Intent intent = new Intent(activity, MyAccount.class);
        activity.startActivity(intent);
    }

    private void logOutOperation() {
        Intent intent = new Intent(activity, Login_Page.class);
        activity.startActivity(intent);
    }

    private void changeThemeOperation() {
        feedPage.toggleThemeOutside();
    }


    private void friendOperation() {
        feedPage.changeOpenMenu();
        feedPage.request = false;
        Intent intent = new Intent(activity, Friends.class);
        activity.startActivity(intent);
    }
    private void friendReqOperation(View view) {
        feedPage.changeOpenMenu();
        Intent intent = new Intent(activity, Requests.class);
        activity.startActivity(intent);
    }
}