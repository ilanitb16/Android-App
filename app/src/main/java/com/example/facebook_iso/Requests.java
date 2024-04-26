package com.example.facebook_iso;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.adapters.FriendRequestAdapter;
import com.example.facebook_iso.login.Login_Page;

public class Requests extends AppCompatActivity {
    private FriendRequestAdapter friendRequestAdapter;
    private Button done;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        RecyclerView requestsList = findViewById(R.id.requestsList);
        friendRequestAdapter = FeedPage.friendRequestAdapter;
        friendRequestAdapter.setFriendRequests(FeedPage.owner.getFriends_request());
        requestsList.setAdapter(friendRequestAdapter);
        requestsList.setLayoutManager(new LinearLayoutManager(this));

        title = findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(this, R.color.blue));

        done = findViewById(R.id.done);
        done.setOnClickListener(v -> finish());
    }
}
