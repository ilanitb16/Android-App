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

import java.util.ArrayList;
import java.util.List;
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.FriendViewHolder> {
    private final Context context;
    private final FeedPage feedPage;
    private int TextColor;
    private final LayoutInflater mInflater;
    private List<User> users;

    public UsersAdapter(Context context, FeedPage feedPage) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.users = new ArrayList<>();
        this.feedPage = feedPage;
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
        User current = users.get(position);
        toggleTheme();
        holder.usernameTextView.setText(current.getUsername());
        holder.displayNameTextView.setText(current.getDisplayName());
        holder.profilePicImageView.setImageURI(Converters.fromString(current.getProfilePic()));
    }


    public void toggleTheme() {
        Boolean isDarkMode = FeedPage.isDarkMode;
            TextColor = isDarkMode ?
                    ContextCompat.getColor(context, R.color.white) :
                    ContextCompat.getColor(context, R.color.black);
    }

    public void addUsers(User users) {
        this.users.add(0, users);
        feedPage.refreshFeed();
        notifyDataSetChanged();
    }

    public void deleteUsers(User users) {
        this.users.remove(users);
        feedPage.refreshFeed();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        else return 0;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}