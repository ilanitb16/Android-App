package com.example.facebook_iso;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.adapters.OwnPostsListAdapter;
import com.example.facebook_iso.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class OwnPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_own_posts);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Post> userPosts = getUserPosts();

        OwnPostsListAdapter adapter = new OwnPostsListAdapter(this);
        adapter.setOwnPosts(userPosts);
        recyclerView.setAdapter(adapter);*/
    }
}