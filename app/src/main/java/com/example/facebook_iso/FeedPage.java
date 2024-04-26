package com.example.facebook_iso;

import
        androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.facebook_iso.adapters.FriendRequestAdapter;
import com.example.facebook_iso.adapters.FriendsAdapter;
import com.example.facebook_iso.adapters.UsersAdapter;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.editHandler.DataSaver;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.entities.Post;

import java.util.ArrayList;
import java.util.List;

import com.example.facebook_iso.menuHandler.MenuFeed;
import com.example.facebook_iso.viewmodels.PostsViewModel;

public class FeedPage extends AppCompatActivity {
    public static User owner;
    public static Boolean picbtn;
    public static Boolean request;
    public static RecyclerView lstPosts;
    public static PostsListAdapter adapter;
    public static Context context;
    private MenuFeed menuFeed;
    private List<Post> posts;
    public static List<User> users;
    public static Boolean isDarkMode;
    public static Boolean openMenu;
    private ImageButton homeButton;
    private ImageButton deleteUser;
    private ImageButton friendReqButton;
    private TextView homeText;
    private ImageButton newPostButton;
    private TextView newPostText;
    private ImageButton myAccountButton;
    private TextView myAccountText;
    private ImageButton logOutButton;
    private TextView logOutText;
    private TextView DeleteText;
    private TextView FriendReqText;
    private Switch changeThemeSwitcher;
    public static PostsViewModel postsViewModel;
    private TextView changeThemeText;
    private ImageButton friendButton;
    private TextView friendText;
    public static UsersAdapter usersAdapter;
    public static FriendRequestAdapter friendRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_page);
        DataSaver.getInstance().setFeedPage(this);

        setMenu();

        setFirstPosts();

        refreshFeed();
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            postsViewModel.reload();
            refreshLayout.setRefreshing(false);

        });

        postsViewModel.get().observe(this, posts0 -> {
            adapter.setPosts(posts0);
            refreshFeed();
        });
    }

    private void setFirstPosts() {
        if (isDarkMode == null) {
            isDarkMode = true;
            toggleTheme();
        }
        request = false;
        usersAdapter = new UsersAdapter(this, this);
        friendRequestAdapter = new FriendRequestAdapter(this);

        context = this;
        lstPosts = findViewById(R.id.lstPosts);

        adapter = new PostsListAdapter(context, this);

        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        postsViewModel = new ViewModelProvider(this).get(PostsViewModel.class);

        posts = new ArrayList<>();
        users = new ArrayList<>();


        usersAdapter.setUsers(users);
        adapter.setPosts(posts);

        postsViewModel.getUserPosts(owner);
    }


    public void refreshFeed() {
        lstPosts = findViewById(R.id.lstPosts);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));


        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> openMenu());

        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> refreshFeed());
    }


    private void openMenu() {
        changeOpenMenu();
        menuFeed = new MenuFeed(this, this);
        menuFeed.setupMenu(homeButton, homeText, newPostButton, newPostText,
                myAccountButton, myAccountText, logOutButton, logOutText,
                changeThemeSwitcher, changeThemeText, friendButton,
                friendText,friendReqButton,deleteUser,DeleteText,FriendReqText);
    }

    public void changeOpenMenu() {
        openMenu = !openMenu;
        LinearLayout menuFeed = findViewById(R.id.menuFeed);
        if (openMenu) {
            menuFeed.setVisibility(View.GONE);
        } else {
            menuFeed.setVisibility(View.VISIBLE);
        }
    }

    private void setMenu() {
        homeButton = findViewById(R.id.HomeButton);
        homeText = findViewById(R.id.HomeText);
        newPostButton = findViewById(R.id.NewPostButton);
        newPostText = findViewById(R.id.NewPostText);
        myAccountButton = findViewById(R.id.MyAccountButton);
        myAccountText = findViewById(R.id.MyAccountText);
        logOutButton = findViewById(R.id.LogOutButton);
        logOutText = findViewById(R.id.LogOutText);
        changeThemeSwitcher = findViewById(R.id.ChangeThemeSwitcher);
        changeThemeText = findViewById(R.id.ChangeThemeText);
        friendButton = findViewById(R.id.FriendButton);
        friendText = findViewById(R.id.FriendText);
        friendReqButton = findViewById(R.id.FriendReqButton);
        deleteUser = findViewById(R.id.btnDeleteUser);
        DeleteText = findViewById(R.id.DeleteText);
        FriendReqText = findViewById(R.id.FriendReqText);
        openMenu = false;
    }


    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        changeOpenMenu();
        changeThemeSwitcher.setChecked(isDarkMode);
        int textColor = isDarkMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.black);
        int backgroundColor = isDarkMode ? ContextCompat.getColor(this, R.color.BACKGROUND_FEED_DARK) : ContextCompat.getColor(this, R.color.BACKGROUND_FEED_LIGHT);
        int backgroundTopColor = isDarkMode ? ContextCompat.getColor(this, R.color.BACKGROUND_POST_DARK) : ContextCompat.getColor(this, R.color.BACKGROUND_POST_LIGHT);
        homeText.setTextColor(textColor);
        newPostText.setTextColor(textColor);
        myAccountText.setTextColor(textColor);
        logOutText.setTextColor(textColor);
        friendText.setTextColor(textColor);
        DeleteText.setTextColor(textColor);
        FriendReqText.setTextColor(textColor);
        changeThemeText.setTextColor(textColor);
        findViewById(R.id.topMenu).setBackgroundColor(backgroundTopColor);
        findViewById(R.id.lstPosts).setBackgroundColor(backgroundColor);
        findViewById(R.id.menuFeed).setBackgroundColor(backgroundColor);
    }

    public void toggleThemeOutside() {
        toggleTheme();
        refreshFeed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DataSaver.getInstance().setFeedPage(this);

    }
}

