package com.example.facebook_iso.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {
    private final Context context;
    private int textColor, itemColor;
    private final LayoutInflater mInflater;
    private List<User> friends;

    public FriendsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        FeedPage.userViewModel.getUserFriends(FeedPage.owner);
        this.friends = FeedPage.owner.getFriends();

    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernameTextView;
        private final TextView displayNameTextView;
        private final ImageView profilePicImageView;

        private final LinearLayout linearLayout;

        private FriendViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.text_view_username);
            displayNameTextView = itemView.findViewById(R.id.text_view_name);
            profilePicImageView = itemView.findViewById(R.id.profile_image);
            linearLayout = itemView.findViewById(R.id.itemBackground);
        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        User current = friends.get(position);
        toggleTheme();
        holder.usernameTextView.setText(current.getUsername());
        holder.displayNameTextView.setText(current.getDisplayName());
        holder.profilePicImageView.setImageURI(Converters.fromString(current.getProfilePic()));

        holder.linearLayout.setBackgroundColor(itemColor);
        holder.usernameTextView.setTextColor(textColor);
        holder.displayNameTextView.setTextColor(textColor);

    }

    private void toggleTheme() {
        boolean isDarkMode = FeedPage.isDarkMode;
        textColor = isDarkMode ?
                ContextCompat.getColor(context, R.color.white) :
                ContextCompat.getColor(context, R.color.black);
;
        itemColor = !isDarkMode ?
                ContextCompat.getColor(context, R.color.BACKGROUND_POST_LIGHT) :
                ContextCompat.getColor(context, R.color.BACKGROUND_POST_DARK);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public List<User> getFriends() {
        return friends;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFriends(List<User> friends) {
        if(friends != null)
        {
            this.friends = friends;
            notifyDataSetChanged();
        }
    }
}