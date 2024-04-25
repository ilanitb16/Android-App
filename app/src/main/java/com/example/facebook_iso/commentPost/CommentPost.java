package com.example.facebook_iso.commentPost;

import android.view.View;
import android.widget.EditText;

import com.example.facebook_iso.R;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class CommentPost {
    private Post post;
    private PostsListAdapter adapter;
    private List<String> comments;

    private View postLayout;

    public CommentPost(Post post, PostsListAdapter adapter) {
        this.post = post;
        this.adapter = adapter;
        this.postLayout = post.getPostLayout();
        this.comments = new ArrayList<>();
    }


    public void addComment(String newComment) {
        comments.add(newComment);
        adapter.notifyDataSetChanged();
    }

    public void editComment(int index, String editedComment) {
        comments.set(index, editedComment);
        adapter.notifyDataSetChanged();
    }

    public void deleteComment(int index) {
        comments.remove(index);
        adapter.notifyDataSetChanged();
    }
    public String getComment(View view) {
        EditText commentInput = postLayout.findViewById(R.id.commentInput);
        return commentInput.getText().toString();
    }
    public List<String> getComments() {
        return comments;
    }

    public void onEditClick(String oldComment, String newComment) {
        for (int i = comments.size() - 1; i >= 0 ; i--) {
            if (oldComment.equals(comments.get(i))){
                editComment(i, newComment);
                break;
            }
        }
    }

    public void onDeleteClick(String deleteComment) {
        for (int i = comments.size() - 1; i >= 0 ; i--) {
            if (deleteComment.equals(comments.get(i))){
                deleteComment(i);
                break;
            }
        }
    }
}