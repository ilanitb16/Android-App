package com.example.facebook_iso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private int textColor;
    private final LayoutInflater mInflater;
    private List<User> friends;

    public FriendsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        Login_Page.userViewModel.getUserFriends(FeedPage.owner);
        this.friends = FeedPage.owner.getFriends();

    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernameTextView;
        private final TextView displayNameTextView;
        private final ImageView profilePicImageView;

        private FriendViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            profilePicImageView = itemView.findViewById(R.id.profilePicImageView);
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

        holder.usernameTextView.setTextColor(textColor);
        holder.displayNameTextView.setTextColor(textColor);
    }

    private void toggleTheme() {
        boolean isDarkMode = FeedPage.isDarkMode;
        textColor = isDarkMode ?
                ContextCompat.getColor(context, R.color.white) :
                ContextCompat.getColor(context, R.color.black);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }
}