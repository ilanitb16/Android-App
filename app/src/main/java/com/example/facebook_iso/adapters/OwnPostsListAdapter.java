package com.example.facebook_iso.adapters;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.editHandler.ThreeDots;
import com.example.facebook_iso.entities.Comment;
import com.example.facebook_iso.entities.Post;
import java.util.ArrayList;
import java.util.List;

public class OwnPostsListAdapter extends RecyclerView.Adapter<OwnPostsListAdapter.PostViewHolder> {
    private final Context context;
    private List<Post> ownPosts;
    private CommentAdapter commentAdapter;
    private final String commentInsert;
    private int backgroundColor;
    private int TextColor;
    private boolean openComment;
    private final LayoutInflater mInflater;
    public static RecyclerView commentsRecyclerView;

    public OwnPostsListAdapter(Context context) {
        this.context = context;
        this.openComment = false;
        this.mInflater = LayoutInflater.from(context);
        this.commentInsert = "";
        this.ownPosts = new ArrayList<>();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvDate;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvLikes;
        private final ImageView ivImg;
        private final ImageView ivAuthorPhoto;
        private final ImageView likeButton;
        private final ImageView shareButton;
        private final ImageView commentButton;
        private final ImageView threeDots;
        private final Button postCommentButton;
        private final EditText commentInput;
        private final ImageView addFriend;
        private final View post;

        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            ivImg = itemView.findViewById(R.id.ivImg);
            ivAuthorPhoto = itemView.findViewById(R.id.ivAuthorPhoto);
            likeButton = itemView.findViewById(R.id.likeButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            threeDots = itemView.findViewById(R.id.threeDots);
            postCommentButton = itemView.findViewById(R.id.postCommentButton);
            post = itemView.findViewById(R.id.POST);
            commentInput = itemView.findViewById(R.id.commentInput);
            addFriend = itemView.findViewById(R.id.addFriend);
            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
        }
    }
    public OwnPostsListAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new OwnPostsListAdapter.PostViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        final Post current = ownPosts.get(position);
        holder.tvAuthor.setText(current.getUser().getUsername());
        holder.tvDate.setText(current.getCreateDate());
        holder.tvTitle.setText(current.getTitle());
        holder.tvDescription.setText(current.getDescription());
        holder.tvLikes.setText(String.valueOf(current.getLikes()));
        holder.ivImg.setImageURI(Converters.fromString(current.getImg()));
        holder.ivAuthorPhoto.setImageURI(Converters.fromString(current.getUser().getProfilePic()));
        toggleTheme();
        commentAdapter = new CommentAdapter(current, FeedPage.feedAdapter);
        holder.tvAuthor.setTextColor(TextColor);
        holder.tvDate.setTextColor(TextColor);
        holder.tvTitle.setTextColor(TextColor);
        holder.tvDescription.setTextColor(TextColor);
        holder.tvLikes.setTextColor(TextColor);
        holder.post.setBackgroundColor(backgroundColor);
        LinearLayout isFriendTrue = holder.itemView.findViewById(R.id.isFriend);
        LinearLayout isFriendFalse = holder.itemView.findViewById(R.id.isNotFriend);
        isFriendTrue.setVisibility(View.VISIBLE);
        isFriendFalse.setVisibility(View.GONE);
        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        commentAdapter.setComments(current.getComments());
        commentAdapter.setPostLayout(holder.itemView);
        refreshComments(holder);
        holder.postCommentButton.setOnClickListener(v -> {
            EditText commentInput = holder.itemView.findViewById(R.id.commentInput);
            String commentText = commentInput.getText().toString();
            if (!commentText.isEmpty()) {
                Comment newComment = new Comment(FeedPage.owner, commentText);
                commentAdapter.addComment(newComment);
                commentInput.setText("");
            }
        });

        holder.commentButton.setOnClickListener(v -> toggleComment(holder));
        holder.likeButton.setOnClickListener(v -> toggleLike(current));
        holder.shareButton.setOnClickListener(v -> toggleShare(v));
        holder.threeDots.setOnClickListener(v -> toggleThreeDots(v, current));
        if (current.getUser().equals(FeedPage.owner)) {
            holder.threeDots.setVisibility(View.VISIBLE);
        } else {
            holder.threeDots.setVisibility(View.GONE);
        }
    }
    public void toggleTheme() {
        Boolean isDarkMode = FeedPage.isDarkMode;
        backgroundColor = isDarkMode ?
                ContextCompat.getColor(context, R.color.BACKGROUND_POST_DARK) :
                ContextCompat.getColor(context, R.color.BACKGROUND_POST_LIGHT);
        TextColor = isDarkMode ?
                ContextCompat.getColor(context, R.color.white) :
                ContextCompat.getColor(context, R.color.black);
    }

    @Override
    public int getItemCount() {
        return ownPosts.size();
    }

    public void setOwnPosts(List<Post> allPosts) {
        ownPosts = allPosts;
        notifyDataSetChanged();
    }
    public CommentAdapter getCommentAdapter() {
        return commentAdapter;
    }
    public void refreshComments(PostViewHolder holder) {
        commentsRecyclerView = holder.itemView.findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setAdapter(getCommentAdapter());
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void toggleComment(PostViewHolder holder) {
        openComment = !openComment;
        LinearLayout commentsSection = holder.itemView.findViewById(R.id.commentsSection);
        if (openComment) {
            commentsSection.setVisibility(View.VISIBLE);
        } else {
            commentsSection.setVisibility(View.GONE);
        }
    }


    public void toggleLike(Post current) {
        boolean isLiked = !current.isLiked();
        if (isLiked) {
            current.setLikes(current.getLikes() + 1);
        } else {
            current.setLikes(current.getLikes() - 1);
        }
        if (FeedPage.feedAdapter != null) {
            int position = FeedPage.feedAdapter.getPosts().indexOf(current);
            if (position != RecyclerView.NO_POSITION) {
                FeedPage.feedAdapter.notifyItemChanged(position);
            }
        }
    }

    public void toggleShare(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        View menuView = LayoutInflater.from(view.getContext()).inflate(R.layout.sharebutton, null);
        PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }


    public void toggleThreeDots(View view, Post current) {
        View menuView = LayoutInflater.from(view.getContext()).inflate(R.layout.threedots, null);
        PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(view);
        current.setThreeDots(new ThreeDots(menuView, current, popupWindow));
    }

    public List<Post> getPosts() {
        return ownPosts;
    }

    public void setPosts(List<Post> allPosts) {
        this.ownPosts = allPosts;
    }
}