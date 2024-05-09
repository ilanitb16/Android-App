package com.iso.facebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.iso.facebook.Fragments.Home;
import com.iso.facebook.auth.LoginScreen;
import com.iso.facebook.common.BottomSheet.BottomSheetUtils;
import com.iso.facebook.common.BottomSheetDialog;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.common.keys;
import com.iso.facebook.entities.CurrentUser;
import com.iso.facebook.menu_pages.CreatePost;
import com.iso.facebook.menu_pages.MyAccount;

public class FeedScreen extends AppCompatActivity{

    public static CurrentUser currentUser;
    private CardView home, createPost, myAccount;
    public static DrawerLayout drawerLayout;
    ConstraintLayout logout;
    ImageView close;
    Toolbar toolbar;
    @SuppressLint({"StaticFieldLeak", "MissingInflatedId"})

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_screen);
        home = findViewById(R.id.home);
        createPost = findViewById(R.id.create_post);
        myAccount = findViewById(R.id.my_account);
        close = findViewById(R.id.close);
        logout = findViewById(R.id.logout_button);
        currentUser = SharedPreferencesManager.getObject(FeedScreen.this, keys.currentUser, CurrentUser.class);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_menu,
                R.string.close_menu);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        createPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FeedScreen.this, CreatePost.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(FeedScreen.this, MyAccount.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BottomSheetUtils.showBottomSheet(getSupportFragmentManager(),
                        "Logout",
                        "Are you sure to logout",
                        "Cancel",
                        "Logout",
                        R.drawable.logout_blue,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                logout();
                            }
                        });

                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });
    }

    void logout()
    {
        UIToast.showToast(FeedScreen.this, "Logged out");
        SharedPreferencesManager.clearAllPreferences(FeedScreen.this);
        startActivity(new Intent(FeedScreen.this, LoginScreen.class));
        finish();
    }
}
