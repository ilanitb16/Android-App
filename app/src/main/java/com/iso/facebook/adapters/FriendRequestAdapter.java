package com.iso.facebook.adapters;

import static com.iso.facebook.FeedScreen.currentUser;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iso.facebook.R;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.common.keys;
import com.iso.facebook.convertors.Convertors;
import com.iso.facebook.entities.User;
import com.iso.facebook.entities.User.FriendRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private List<FriendRequest> friendRequestList;
    private Context context;

    public FriendRequestAdapter(Context context, List<FriendRequest> friendRequestList) {
        this.context = context;
        this.friendRequestList = friendRequestList;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequest friendRequest = friendRequestList.get(position);
        holder.bind(friendRequest);
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicImageView;
        TextView displayNameTextView;
        Button approveButton, rejectButton;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicImageView = itemView.findViewById(R.id.profile_image);
            displayNameTextView = itemView.findViewById(R.id.friend_name);
            approveButton = itemView.findViewById(R.id.accept_button);
            rejectButton = itemView.findViewById(R.id.reject_button);
        }

        public void bind(FriendRequest friendRequest) {
            displayNameTextView.setText(friendRequest.getDisplayName());
            profilePicImageView.setImageURI(friendRequest.getProfilePic());

            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ProgressDialogManager.showProgressDialog(context, "Approving Request", "Please wait...");
                        new ApiService(context).patch(endPoints.request+currentUser.get_id() +"/friends" + friendRequest.getUsername(), currentUser.getToken(), new ApiService.ApiCallback()
                        {
                            @Override
                            public void onSuccess(JSONObject response)
                            {
                                ProgressDialogManager.dismissProgressDialog();
                                UIToast.showToast(context, "Request Approved");
                                removeItem(position);
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
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ProgressDialogManager.showProgressDialog(context, "Rejecting Request", "Please wait...");
                        new ApiService(context).delete(endPoints.request+currentUser.get_id() +"/friends" + friendRequest.getUsername(), currentUser.getToken(), new ApiService.ApiCallback()
                        {
                            @Override
                            public void onSuccess(JSONObject response)
                            {
                                ProgressDialogManager.dismissProgressDialog();
                                UIToast.showToast(context, "Request Rejected");
                                removeItem(position);
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
                }
            });
        }
        private void removeItem(int position) {
            friendRequestList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
