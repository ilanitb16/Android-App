package com.example.facebook_iso;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebook_iso.adapters.FriendRequestAdapter;
import com.example.facebook_iso.login.Login_Page;

public class Requests extends AppCompatActivity
{
    private Button done;
    private TextView title;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        RecyclerView requestsList = findViewById(R.id.requestsList);
        FeedPage.friendRequestAdapter.setFriendRequests(FeedPage.owner.getFriends_request());
        requestsList.setAdapter(FeedPage.friendRequestAdapter);
        requestsList.setLayoutManager(new LinearLayoutManager(this));

        title = findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(this, R.color.blue));

        done = findViewById(R.id.done);
        done.setOnClickListener(v -> finish());

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        FeedPage.userViewModel.getUserRequests(FeedPage.owner.getUsername(), refreshLayout);

        refreshLayout.setOnRefreshListener(() ->
        {
            FeedPage.userViewModel.getUserRequests(FeedPage.owner.getUsername(), refreshLayout);
            refreshLayout.setRefreshing(false);
        });
    }
}
