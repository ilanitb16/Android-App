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
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.auth.LoginScreen;
import com.iso.facebook.common.BottomSheet.BottomSheetUtils;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.common.keys;
import com.iso.facebook.entities.CurrentUser;

import org.json.JSONArray;
import org.json.JSONObject;

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
                BottomSheetUtils.showBottomSheet(getSupportFragmentManager(),
                        "Are you sure you want to delete your account?",
                        "If you delete your account, all Friends, and posts will be gone forever :(",
                        "Cancel",
                        "Delete Account",
                        R.drawable.logout_blue,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ProgressDialogManager.showProgressDialog(MyAccount.this, "Deleting Your account", "Please wait...");
                                new ApiService(MyAccount.this).delete(endPoints.request+currentUser.get_id(), currentUser.getToken(), new ApiService.ApiCallback()
                                {
                                    @Override
                                    public void onSuccess(JSONObject response)
                                    {
                                        ProgressDialogManager.dismissProgressDialog();
                                        UIToast.showToast(MyAccount.this, "Account Deleted");
                                        Intent intent = new Intent(MyAccount.this, LoginScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        MyAccount.this.startActivity(intent);
                                    }

                                    @Override
                                    public void onSuccess(JSONArray response)
                                    {
                                        ProgressDialogManager.dismissProgressDialog();
                                    }

                                    @Override
                                    public void onError(String errorMessage)
                                    {
                                        UIToast.showToast(MyAccount.this, errorMessage);
                                        ProgressDialogManager.dismissProgressDialog();
                                    }
                                });
                            }
                        });
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