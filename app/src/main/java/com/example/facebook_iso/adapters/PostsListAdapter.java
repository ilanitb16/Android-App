package com.example.facebook_iso.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;

import com.example.facebook_iso.UserInfoActivity;
import com.example.facebook_iso.editHandler.ThreeDots;
import com.example.facebook_iso.entities.Comment;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.menuHandler.MenuFeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
    private CommentAdapter commentAdapter;
    private String commentInsert;
    private int backgroundColor;
    private int TextColor;
    private final Context context;
    private final FeedPage feedPage;
    private final LayoutInflater mInflater;
    private List<Post> posts;
    private boolean openComment;
    public static RecyclerView commentsRecyclerView;
    public static User author;

    public PostsListAdapter(Context context, FeedPage feedPage) {
        this.mInflater = LayoutInflater.from(context);
        this.commentInsert = "";
        this.context = context;
        this.feedPage = feedPage;
        this.openComment = false;
    }

    public CommentAdapter getCommentAdapter() {
        return commentAdapter;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final View post;
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


    public PostsListAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostsListAdapter.PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);

            if (current.getUser() != null) {
                holder.tvAuthor.setText(current.getUser().getUsername());
                holder.tvDate.setText(current.getCreateDate());
                holder.tvTitle.setText(current.getTitle());
                holder.tvDescription.setText(current.getDescription());
                holder.tvLikes.setText(String.valueOf(current.getLikes()));
                holder.ivImg.setImageURI(Converters.fromString(current.getImg()));
                holder.ivAuthorPhoto.setImageURI(Converters.fromString(current.getUser().getProfilePic()));


                holder.ivAuthorPhoto.setOnClickListener(v -> {
                    FeedPage.picbtn = true;
                    author = current.getUser();
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    context.startActivity(intent);
                });


                toggleTheme();
                holder.tvAuthor.setTextColor(TextColor);
                holder.tvDate.setTextColor(TextColor);
                holder.tvTitle.setTextColor(TextColor);
                holder.tvDescription.setTextColor(TextColor);
                holder.tvLikes.setTextColor(TextColor);
                holder.post.setBackgroundColor(backgroundColor);
                holder.commentInput.setTextColor(TextColor);

                CommentAdapter commentAdapter = new CommentAdapter(current, this);
                commentsRecyclerView.setAdapter(commentAdapter);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                commentAdapter.setComments(current.getComments());

                commentAdapter.setPostLayout(holder.itemView);
                holder.likeButton.setOnClickListener(v -> toggleLike(current));
                holder.shareButton.setOnClickListener(v -> toggleShare(v));
                holder.commentButton.setOnClickListener(v -> toggleComment(holder.itemView));
                holder.threeDots.setOnClickListener(v -> toggleThreeDots(v, current));
                holder.postCommentButton.setOnClickListener(v -> {
                    EditText commentInput = holder.itemView.findViewById(R.id.commentInput);
                    String commentText = commentInput.getText().toString();
                    if (!commentText.isEmpty()) {
                        Comment newComment = new Comment(FeedPage.owner, commentText);
                        commentAdapter.addComment(newComment);
                        commentInput.setText("");
                    }
                });
                if (current.getUser().equals(FeedPage.owner)) {
                    holder.threeDots.setVisibility(View.VISIBLE);
                } else {
                    holder.threeDots.setVisibility(View.GONE);
                }

                FeedPage.userViewModel.getUserFriends(FeedPage.owner);
                boolean isFriend = current.getUser().equals(FeedPage.owner);
                for (User friend : FeedPage.owner.getFriends()) {
                    if (friend.getUsername().equals(current.getUser().getUsername())) {
                        isFriend = true;
                        break;
                    }
                }

                LinearLayout isFriendTrue = holder.itemView.findViewById(R.id.isFriend);
                LinearLayout isFriendFalse = holder.itemView.findViewById(R.id.isNotFriend);
                if (isFriend) {
                    isFriendTrue.setVisibility(View.VISIBLE);
                    isFriendFalse.setVisibility(View.GONE);
                } else {
                    isFriendTrue.setVisibility(View.GONE);
                    isFriendFalse.setVisibility(View.VISIBLE);
                }
                holder.addFriend.setOnClickListener(v -> {
                    User friend = current.getUser();
                    if (friend != null) {
                        if (FeedPage.owner.findFriend(friend)) {
                            Toast.makeText(context, "Already a friend", Toast.LENGTH_SHORT).show();
                        } else if (friend.findRequest(FeedPage.owner.getUsername()) != null) {
                            Toast.makeText(context, "Friend request already sent", Toast.LENGTH_SHORT).show();
                        } else if (FeedPage.owner.getUsername().equals(current.getUser().getUsername())) {
                            Toast.makeText(context, "You can't be your own friend", Toast.LENGTH_SHORT).show();
                        } else {
                            friend.sendFriendRequest(FeedPage.owner);
                            FeedPage.postsViewModel.friendsRequest(current.getUser());
                            notifyDataSetChanged();
                        }
                    }
                    else {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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


    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<Post> allPosts) {
        posts = allPosts;
        notifyDataSetChanged();
    }


    public void addPost(Post post) {
        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }
        this.posts.add(0, post);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        else return 0;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Context getContext() {
        return context;
    }

    public void refreshComments() {
        commentsRecyclerView.setAdapter(getCommentAdapter());
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(MenuFeed.activity));
    }

    public void toggleComment(View postLayout) {
        openComment = !openComment;
        LinearLayout commentsSection = postLayout.findViewById(R.id.commentsSection);
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
}