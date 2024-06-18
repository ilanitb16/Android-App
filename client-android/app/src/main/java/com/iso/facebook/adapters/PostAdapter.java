package com.iso.facebook.adapters;

import static com.iso.facebook.FeedScreen.currentUser;
import static com.iso.facebook.FeedScreen.currentUserFriends;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.iso.facebook.R;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.BottomSheet.BottomSheetUtils;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.entities.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        if(currentUserFriends.stream()
                .anyMatch(request -> request.getUsername().equals(post.getUsername())))
        {
            Log.d("checking", post.getUsername());
            holder.feedMenuButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.feedMenuButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    BottomSheetUtils.showBottomSheet(((AppCompatActivity) context).getSupportFragmentManager(), "Friend Request", "Do you want to send request", "No", "Yes", R.drawable.add_friend, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            ProgressDialogManager.showProgressDialog(context, "Sending Request", "Please wait...");
                            new ApiService(context).post(endPoints.request+post.getUsername() +"/friends", null, currentUser.getToken(), new ApiService.ApiCallback()
                            {
                                @Override
                                public void onSuccess(JSONObject response)
                                {
                                    if(response.has("modifiedCount"))
                                    {
                                        UIToast.showToast(context, "Friend Request Send");
                                    }
                                    else
                                    {
                                        UIToast.showToast(context, "Request already sent");
                                    }
                                    ProgressDialogManager.dismissProgressDialog();
                                }

                                @Override
                                public void onSuccess(JSONArray response)
                                {
                                    ProgressDialogManager.dismissProgressDialog();
                                }

                                @Override
                                public void onError(String errorMessage)
                                {
                                    UIToast.showToast(context, errorMessage);
                                    ProgressDialogManager.dismissProgressDialog();
                                }
                            });
                        }
                    });
                }
            });
        }

        // Set data to views
        holder.feedPersonProfile.setImageURI(post.getProfilePic(context));
        holder.feedPersonName.setText(post.getUsername());
        holder.feedTitle.setText(post.getTitle());
        holder.feedDescription.setText(post.getDescription());
        holder.feedDate.setText(post.getDate());
        holder.feedTime.setText(post.getTime());
        holder.feedImage.setImageURI(post.getImg(context));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        CircleImageView feedPersonProfile;
        TextView feedPersonName;
        ImageView feedMenuButton;
        ImageView feedImage;
        TextView feedTitle;
        TextView feedDescription;
        TextView feedDate;
        TextView feedTime;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            feedPersonProfile = itemView.findViewById(R.id.feed_person_profile);
            feedPersonName = itemView.findViewById(R.id.feed_person_name);
            feedMenuButton = itemView.findViewById(R.id.add_friend);
            feedImage = itemView.findViewById(R.id.feed_image);
            feedTitle = itemView.findViewById(R.id.feed_title);
            feedDescription = itemView.findViewById(R.id.feed_description);
            feedDate = itemView.findViewById(R.id.feed_date);
            feedTime = itemView.findViewById(R.id.feed_time);
        }
    }
}
