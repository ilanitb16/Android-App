package com.example.facebook_iso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.api_manager.api_manager;
import com.example.facebook_iso.api_manager.api_service;
import com.example.facebook_iso.api_manager.constants;
import com.example.facebook_iso.common.CurrentUserManager;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.ResponseHandler;
import com.example.facebook_iso.common.SharedPreferencesManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.entities.UserClass;
import com.example.facebook_iso.menuHandler.MenuHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

import okhttp3.Response;
import okhttp3.internal.Util;


public class Feed_Page extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView lstPosts;
    private PostsListAdapter adapter;
    private MenuHandler menuHandler;
    private List<Post> posts;

    private String user_name;
    private Uri user_photo;
    private String userFullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_page);
        UserClass currentUser = Objects.requireNonNull(CurrentUserManager.getInstance(Feed_Page.this).getCurrentUser()).getUser();
        user_name = "@" + currentUser.getUsername();
        user_photo = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.facebook_photo);
        userFullName = currentUser.getDisplayName();

        lstPosts = findViewById(R.id.lstPosts);
        refreshLayout = findViewById(R.id.refreshLayout);
        adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);

        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        posts = new ArrayList<>();
        getPosts(false);

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                getPosts(true);
            }
        });
        adapter.setUserName(user_name);
        adapter.setUserPhoto(user_photo);

    }

    private void getPosts(Boolean isRefresh) {
        try {
            if(!isRefresh)
            {
                ProgressDialogManager.showProgressDialog(Feed_Page.this, "Getting Posts", "Please wait...");
            }
            InputStream inputStream = getAssets().open("allPosts.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String bearerToken = Objects.requireNonNull(SharedPreferencesManager.getObject(Feed_Page.this, keys.currentUser, User.class)).getToken();
            String userName = Objects.requireNonNull(SharedPreferencesManager.getObject(Feed_Page.this, keys.currentUser, User.class)).getUser().getUsername();
            String endpoint = constants.getPosts + "/" + userName + "/posts";
            new api_service(Feed_Page.this).get(endpoint, bearerToken, new api_service.ApiCallback()
            {
                @Override
                public void onSuccess(JSONObject response)
                {

                }

                @Override
                public void onSuccess(JSONArray response) {
                    try {
                        int max = response.length();
                        if (max == 0) {
                            UIToast.showToast(Feed_Page.this, "No posts available");
                        } else {
                            posts.clear();
                            for (int i = 0; i < max; i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.optString("_id", "");
                                String title = jsonObject.optString("title", "");
                                String username = "@" + jsonObject.optString("username", "");
                                String description = jsonObject.optString("description", "");
                                String date = jsonObject.optString("create_date", "");
                                String img = jsonObject.optString("img", "");
                                String profilePic = jsonObject.optString("profilePic", "");
                                Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + img);
                                Uri imageAuthorUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + profilePic);
                                posts.add(new Post(id, title, username, imageAuthorUri, description, date, imageUri, lstPosts, adapter));
                            }
                            adapter.setPosts(posts);
                            UIToast.showToast(Feed_Page.this, "Posts refreshed");
                        }
                    } catch (JSONException e) {
                        UIToast.showToast(Feed_Page.this, e.getMessage());
                    }
                    ProgressDialogManager.dismissProgressDialog();
                    refreshLayout.setRefreshing(false);
                }


                @Override
                public void onError(String errorMessage)
                {
                    UIToast.showToast(Feed_Page.this, errorMessage);
                    ProgressDialogManager.dismissProgressDialog();
                    refreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "getPosts: error " + e);
            ProgressDialogManager.dismissProgressDialog();
            refreshLayout.setRefreshing(false);
        }
    }

    private void showPopupMenu(View view) {
        @SuppressLint("InflateParams") View menuView = getLayoutInflater().inflate(R.layout.menu, null);
        PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(view);
        menuHandler = new MenuHandler(menuView, lstPosts, adapter, user_name, popupWindow, userFullName, user_photo);
    }
}
