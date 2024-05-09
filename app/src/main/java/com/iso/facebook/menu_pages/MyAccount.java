package com.iso.facebook.menu_pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import static com.iso.facebook.FeedScreen.currentUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iso.facebook.R;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.keys;
import com.iso.facebook.entities.CurrentUser;

public class MyAccount extends AppCompatActivity
{

    private TextView name, username;
    private ImageView profile_picture;
    private ImageButton back_button;
    private CardView edit_profile, my_posts, friends_requests, delete_account;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        profile_picture = findViewById(R.id.profile_picture);
        edit_profile = findViewById(R.id.edit_profile);
        my_posts = findViewById(R.id.my_posts);
        friends_requests = findViewById(R.id.friends);
        delete_account = findViewById(R.id.delete_account);
        back_button = findViewById(R.id.back_button);
        setCurrentUser();
        edit_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAccount.this, EditProfile.class));
            }
        });
        my_posts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAccount.this, UserPosts.class));
            }
        });
        friends_requests.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MyAccount.this, FriendsPage.class));
            }
        });
        delete_account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
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

    void setCurrentUser()
    {
        currentUser = SharedPreferencesManager.getObject(MyAccount.this, keys.currentUser, CurrentUser.class);
        assert currentUser != null;
        name.setText(currentUser.getDisplayName());
        username.setText(currentUser.getUsername());
        profile_picture.setImageURI(currentUser.getProfilePic(MyAccount.this));
    }

    @Override
    protected void onResume()
    {
        setCurrentUser();
        super.onResume();
    }
}