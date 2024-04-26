package com.example.facebook_iso.menuHandler;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.adapters.FriendsAdapter;
import com.example.facebook_iso.entities.User;

public class Friends extends AppCompatActivity {
    private FriendsAdapter friendsAdapter;
    private Button done;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        RecyclerView friendsList = findViewById(R.id.friendsList);

        friendsAdapter = new FriendsAdapter(FeedPage.context);
        friendsList.setAdapter(friendsAdapter);
        friendsList.setLayoutManager(new LinearLayoutManager(this));

        title = findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(this, R.color.blue));

        done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            finish();
        });
    }
}