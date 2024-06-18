package com.iso.facebook.menu_pages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.iso.facebook.R;
import com.iso.facebook.adapters.MyPostAdapter;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import static com.iso.facebook.FeedScreen.currentUser;

public class UserPosts extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private MyPostAdapter postAdapter;
    private List<Post> postList;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        recyclerView = findViewById(R.id.myFeed_recycler_view);
        postList = new ArrayList<>();
        postAdapter = new MyPostAdapter(UserPosts.this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserPosts.this));
        recyclerView.setAdapter(postAdapter);
        back_button = findViewById(R.id.back_button);
        fetchPosts();

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(() -> {
            fetchPosts();
            refreshLayout.setRefreshing(false);
        });

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    void fetchPosts() {
        ProgressDialogManager.showProgressDialog(UserPosts.this, "Getting Posts", "Please wait...");
        new ApiService(UserPosts.this).get(endPoints.getUserPosts +currentUser.getUsername()+ "/posts", currentUser.getToken(), new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(JSONArray response)
            {
                postList.clear();
                for(int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        postList.add(Post.fromJson(String.valueOf(response.getJSONObject(i))));
                    }
                    catch (JSONException e)
                    {
                        UIToast.showToast(UserPosts.this, e.getMessage());
                        ProgressDialogManager.dismissProgressDialog();
                    }
                }
                postAdapter.notifyDataSetChanged();
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onError(String errorMessage)
            {
                UIToast.showToast(UserPosts.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
}