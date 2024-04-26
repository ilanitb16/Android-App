package com.example.facebook_iso.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.MyApplication;
import com.example.facebook_iso.R;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;
import com.example.facebook_iso.login.SignupPage;
import com.example.facebook_iso.login.current_user;
import com.example.facebook_iso.menuHandler.Friends;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    Retrofit retrofit;
    UserServiceAPI userServiceAPI;

    public UserAPI() {

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userServiceAPI = retrofit.create(UserServiceAPI.class);
    }

    public void signUp(User user, Context context) {
        try {
            ProgressDialogManager.showProgressDialog(context, "Signing Up", "Please wait...");
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", user.getUsername());
            requestBodyJson.put("password", user.getPassword());
            requestBodyJson.put("displayName", user.getDisplayName());
            requestBodyJson.put("profilePic", user.getProfilePic());
            Object jsonParser = JsonParser.parseString(requestBodyJson.toString());
            Log.d("Api call signUp", "Started");

            Call<JsonObject> call = userServiceAPI.signUp(jsonParser);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JsonObject jsonObject = response.body();
                            if (jsonObject != null) {
                                if (jsonObject.has("insertedId")) {
                                    String insertedId = jsonObject.get("insertedId").getAsString();
                                    Log.d("Api call signUp", "User signed up successfully with ID: " + insertedId);
//                                    signIn(user.getUsername(), user.getPassword());
                                    ((Activity) context).finish();
                                } else if (jsonObject.has("message")) {
                                    String message = jsonObject.get("message").getAsString();
                                    Log.d("Api call signUp", "User already exists: " + message);
                                    Toast.makeText(FeedPage.context, "User Already Exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.d("Api call signUp", "Unsuccessful response: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("Api call signUp", "Unsuccessful response: " + response.message());
                    }
                    ProgressDialogManager.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Api call signUp", "Failed to sign up: " + t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Api call signUp", "Failed to sign up: " + e.getMessage());
        }
    }



    public void signIn(String username, String password) {

        try {
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", username);
            requestBodyJson.put("password", password);

            Object jsonParser = JsonParser.parseString(requestBodyJson.toString());


            Call<JsonObject> call = userServiceAPI.signIn(jsonParser);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("Api call UserAPI", "Enqueue onResponse: " + response.toString());

                    if (response.isSuccessful()) {
                        try {
                            JsonObject jsonObject = response.body();

                            if (jsonObject != null) {
                                JsonObject userObject = jsonObject.getAsJsonObject("user");

                                String userId = userObject.get("_id").getAsString();
                                String username = userObject.get("username").getAsString();
                                String displayName = userObject.get("displayName").getAsString();
                                String profilePicBase64 = userObject.get("profilePic").getAsString();
                                String profilePicString = profilePicBase64.substring(profilePicBase64.indexOf(',') + 1);
                                String profilePic = Converters.base64ToString(profilePicString);


                                String token = jsonObject.get("token").getAsString();
                                User user = new User(username, profilePic, displayName);
                                user.setToken(token);
                                if (userObject.has("friends")) {
                                    JsonArray friendsArray = userObject.getAsJsonArray("friends");
                                    List<User> friends = new ArrayList<>();
                                    for (JsonElement friendElement : friendsArray) {
                                        JsonObject friendObject = friendElement.getAsJsonObject();
                                        String friendUsername = friendObject.has("username") ? friendObject.get("username").getAsString() : "";
                                        String friendProfilePic = friendObject.has("profilePic") ? friendObject.get("profilePic").getAsString() : "";
                                        String friendDisplayName = friendObject.has("displayName") ? friendObject.get("displayName").getAsString() : "";
                                        User friend = new User(friendUsername, friendProfilePic, friendDisplayName);
                                        friends.add(friend);
                                    }
                                    user.setFriends(friends);
                                }
                                if (userObject.has("friendsRequest")) {
                                    JsonArray friendsArray = userObject.getAsJsonArray("friendsRequest");
                                    List<User> friendsRequest = new ArrayList<>();
                                    for (JsonElement friendElement : friendsArray) {
                                        JsonObject friendObject = friendElement.getAsJsonObject();
                                        String friendUsername = friendObject.has("username") ? friendObject.get("username").getAsString() : "";
                                        String friendProfilePicBase64 = friendObject.has("profilePic") ? friendObject.get("profilePic").getAsString() : "";
                                        String friendProfilePicString = friendProfilePicBase64.substring(friendProfilePicBase64.indexOf(',') + 1);
                                        String friendProfilePic = Converters.base64ToString(friendProfilePicString);
                                        String friendDisplayName = friendObject.has("displayName") ? friendObject.get("displayName").getAsString() : "";
                                        User friend = new User(friendUsername, friendProfilePic, friendDisplayName);
                                        friendsRequest.add(friend);
                                    }
                                    user.setFriends_request(friendsRequest);
                                }
                                FeedPage.owner = user;
                                current_user.getInstance().set_CurrentUser(FeedPage.owner);
                                Intent feedPageActivity = new Intent(Login_Page.context, FeedPage.class);
                                feedPageActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                UIToast.showToast(Login_Page.context, "Logged In");
                                ProgressDialogManager.dismissProgressDialog();
                                Login_Page.context.startActivity(feedPageActivity);
                                ((Activity) Login_Page.context).finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        UIToast.showToast(Login_Page.context, "Enqueue onFailure: " + response.body());
                        ProgressDialogManager.dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("Api call UserAPI", "Enqueue onFailure: " + t.toString());
                    UIToast.showToast(Login_Page.context, "Enqueue onFailure: " + t);
                    ProgressDialogManager.dismissProgressDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void updateUser(User user) {
        try {
            String token = user.getToken();
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", user.getUsername());
            requestBodyJson.put("password", user.getPassword());
            requestBodyJson.put("displayName", user.getDisplayName());
            requestBodyJson.put("profilePic", user.getProfilePic());
            Log.d("Api call UserAPI", "Sign Up before Request Body: " + requestBodyJson.toString());
            Object jsonParser = JsonParser.parseString(requestBodyJson.toString());

            Call<JsonObject> call = userServiceAPI.updateUser(user.getUsername(), jsonParser, "Bearer " + token);
            Log.d("Api call UserAPI", "Sign Up after Request Body: " + requestBodyJson.toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JsonObject jsonObject = response.body();
                            if (jsonObject != null) {
                                if (jsonObject.has("modifiedCount")) {
                                    Toast.makeText(FeedPage.feedAdapter.getContext(), "user updated", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } finally {

                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUser(String username, Post post, CompletableFuture<Void> completion, Boolean isLast) {
        Call<JsonObject> call = userServiceAPI.getUser(username, "Bearer " + FeedPage.owner.getToken());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        String username = jsonObject.get("username").getAsString();
                        String displayName = jsonObject.get("displayName").getAsString();
                        String profilePicBase64 = jsonObject.get("profilePic").getAsString();
                        String profilePicString = profilePicBase64.substring(profilePicBase64.indexOf(',') + 1);
                        String profilePic = Converters.base64ToString(profilePicString);

                        User user = new User(username, profilePic, displayName);
                        FeedPage.postsViewModel.getUserPosts(user);
                        if (jsonObject.has("friendsRequest")) {
                            JsonArray friendsArray = jsonObject.getAsJsonArray("friendsRequest");
                            List<User> friendsRequest = new ArrayList<>();
                            for (JsonElement friendElement : friendsArray) {
                                JsonObject friendObject = friendElement.getAsJsonObject();
                                String friendUsername = friendObject.has("username") ? friendObject.get("username").getAsString() : "";
                                String friendProfilePicBase64 = friendObject.has("profilePic") ? friendObject.get("profilePic").getAsString() : "";
                                String friendProfilePicString = friendProfilePicBase64.substring(friendProfilePicBase64.indexOf(',') + 1);
                                String friendProfilePic = Converters.base64ToString(friendProfilePicString);
                                String friendDisplayName = friendObject.has("displayName") ? friendObject.get("displayName").getAsString() : "";
                                User friend = new User(friendUsername, friendProfilePic, friendDisplayName);
                                friendsRequest.add(friend);
                            }
                            user.setFriends_request(friendsRequest);
                        }
                        post.setUser(user);
                        if(isLast)
                        {
                            completion.complete(null);
                        }
                    }
                } else {
                    completion.completeExceptionally(new Exception("Failed to fetch user data"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                completion.completeExceptionally(t); // Complete exceptionally if there's a failure
            }
        });
    }


    public void getUserRequests(String username, SwipeRefreshLayout swipeRefreshLayout) {
        Call<JsonObject> call = userServiceAPI.getUser(username, "Bearer " + FeedPage.owner.getToken());
        Log.d("Api call", "getUserRequests: Request URL: " + call.request().url()); // Debug print with tag
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Api call", "getUserRequests: Response received"); // Debug print with tag
                if (response.isSuccessful()) {
                    Log.d("Api call", "getUserRequests: Response is successful"); // Debug print with tag
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        String username = jsonObject.get("username").getAsString();
                        String displayName = jsonObject.get("displayName").getAsString();
                        String profilePicBase64 = jsonObject.get("profilePic").getAsString();
                        String profilePicString = profilePicBase64.substring(profilePicBase64.indexOf(',') + 1);
                        String profilePic = Converters.base64ToString(profilePicString);

                        User user = new User(username, profilePic, displayName);
                        FeedPage.postsViewModel.getUserPosts(user);
                        if (jsonObject.has("friendsRequest")) {
                            JsonArray friendsArray = jsonObject.getAsJsonArray("friendsRequest");
                            List<User> friendsRequest = new ArrayList<>();
                            for (JsonElement friendElement : friendsArray) {
                                JsonObject friendObject = friendElement.getAsJsonObject();
                                String friendUsername = friendObject.has("username") ? friendObject.get("username").getAsString() : "";
                                String friendProfilePicBase64 = friendObject.has("profilePic") ? friendObject.get("profilePic").getAsString() : "";
                                String friendProfilePicString = friendProfilePicBase64.substring(friendProfilePicBase64.indexOf(',') + 1);
                                String friendProfilePic = Converters.base64ToString(friendProfilePicString);
                                String friendDisplayName = friendObject.has("displayName") ? friendObject.get("displayName").getAsString() : "";
                                User friend = new User(friendUsername, friendProfilePic, friendDisplayName);
                                friendsRequest.add(friend);
                            }
                            user.setFriends_request(friendsRequest);
                            FeedPage.friendRequestAdapter.setFriendRequests(user.getFriends_request());
                        }
                    }
                } else {
                    Log.d("Api call", "getUserRequests: Response is not successful: " + response.code()); // Debug print with tag
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Api call", "getUserRequests: Request failed: " + t.getMessage()); // Error print with tag
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void deleteUser(User user) {
        ProgressDialogManager.showProgressDialog(FeedPage.context, "Deleting User", "Please wait...");

        String username = user.getUsername();
        Log.d("Api call DeleteUserDebug", "Deleting user: " + username);

        Call<JsonObject> call = userServiceAPI.deleteUser(username, "Bearer " + user.getToken());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Api call DeleteUserDebug", "User deletion API call successful");

                    JsonObject jsonObject = response.body();
                    if (jsonObject.has("insertedId")) {
                        Intent LoginPageIntent = new Intent(FeedPage.context, Login_Page.class);
                        FeedPage.context.startActivity(LoginPageIntent);
                        Toast.makeText(FeedPage.feedAdapter.getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                    }
                    UIToast.showToast(FeedPage.context, "User account deleted");
                    FeedPage.context.startActivity(new Intent(FeedPage.context, Login_Page.class));
                } else {
                    Log.d("Api call DeleteUserDebug", "User deletion API call unsuccessful: " + response.toString());
                    UIToast.showToast(FeedPage.context, "User deletion unsuccessful: " + response.message());
                }
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ProgressDialogManager.dismissProgressDialog();
                Log.e("Api call DeleteUserDebug", "Failed to delete user. Error: " + t.getMessage());
                UIToast.showToast(FeedPage.context, "User deletion unsuccessful: " + t.getMessage());
            }
        });
    }


    public void getUserFriends(User user) {
        String token = user.getToken();
        Log.d("Api call GetUserFriendsDebug", "Fetching friends for user: " + user.getUsername());
        Log.d("Api call GetUserFriendsDebug", "User token: " + token);

        Call<JsonObject> call = userServiceAPI.getUserFriends(user.getUsername(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Api call GetUserFriendsDebug", "Get user friends API call successful");
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        if (jsonObject.has("friends")) {
                            Log.d("Api call GetUserFriendsDebug", "Parsing friend list" + jsonObject.getAsJsonArray("friends"));
                            JsonArray friendsArray = jsonObject.getAsJsonArray("friends");
                            List<User> friends = new ArrayList<>();
                            for (JsonElement friendElement : friendsArray) {
                                JsonObject friendObject = friendElement.getAsJsonObject();
                                String friendUsername = friendObject.has("username") ? friendObject.get("username").getAsString() : "";
                                String friendProfilePicBase64 = friendObject.has("profilePic") ? friendObject.get("profilePic").getAsString() : "";
                                String friendProfilePicString = friendProfilePicBase64.substring(friendProfilePicBase64.indexOf(',') + 1);
                                String friendProfilePic = Converters.base64ToString(friendProfilePicString);
                                String friendDisplayName = friendObject.has("displayName") ? friendObject.get("displayName").getAsString() : "";
                                User friend = new User(friendUsername, friendProfilePic, friendDisplayName);
                                friends.add(friend);
                            }
                            user.setFriends(friends);
                            FeedPage.friendsAdapter.setFriends(friends);
                        } else {
                            Log.d("Api call GetUserFriendsDebug", "No 'friends' field found in response JSON");
                        }
                    } else {
                        Log.d("Api call GetUserFriendsDebug", "Response body is null");
                    }
                } else {
                    Log.d("Api call GetUserFriendsDebug", "Get user friends API call unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Api call GetUserFriendsDebug", "Failed to fetch user friends. Error: " + t.getMessage());
            }
        });
    }


    public void deleteFriendsRequest(User friend) {
        String token = FeedPage.owner.getToken();
        Log.d("Api call DELETE_FRIENDS_REQUEST", "Token: " + token); // Debug print with tag
        Call<JsonObject> call = userServiceAPI.deleteFriendsRequest(FeedPage.owner.getUsername(), friend.getUsername(), "Bearer " + token);
        Log.d("Api call DELETE_FRIENDS_REQUEST", "Request URL: " + call.request().url()); // Debug print with tag
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Api call DELETE_FRIENDS_REQUEST", "Response received"); // Debug print with tag
                if (response.isSuccessful()) {
                    Log.d("Api call DELETE_FRIENDS_REQUEST", "Response is successful"); // Debug print with tag
                    try {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            Log.d("Api call DELETE_FRIENDS_REQUEST", "Response body: " + jsonObject.toString()); // Debug print with tag
                            if (jsonObject.has("deletedCount")) {
                                Log.d("Api call DELETE_FRIENDS_REQUEST", "Friend request deleted"); // Debug print with tag
                                Toast.makeText(FeedPage.feedAdapter.getContext(), "friend request deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } finally {
                        // Add any necessary cleanup code here
                    }
                } else {
                    Log.d("Api call DELETE_FRIENDS_REQUEST", "Response is not successful: " + response.code()); // Debug print with tag
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Api call DELETE_FRIENDS_REQUEST", "Request failed: " + t.getMessage()); // Error print with tag
                // Add any necessary error handling code here
            }
        });
    }


    public void approveFriendsRequest(User friend) {
        String token = FeedPage.owner.getToken();
        System.out.println("Api call AToken: " + token); // Debug print
        Call<JsonObject> call = userServiceAPI.approveFriendsRequest(FeedPage.owner.getUsername(), friend.getUsername(), "Bearer " + token);
        System.out.println("Api call ARequest URL: " + call.request().url()); // Debug print
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("Api call AResponse received"); // Debug print
                if (response.isSuccessful()) {
                    System.out.println("Api call AResponse is successful"); // Debug print
                    try {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            System.out.println("Api call AResponse body: " + jsonObject.toString()); // Debug print
                            if (jsonObject.has("modifiedCount")) {
                                System.out.println("Api call AFriend request approved"); // Debug print
                                Toast.makeText(FeedPage.feedAdapter.getContext(), "friend request approved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } finally {
                        // Add any necessary cleanup code here
                    }
                } else {
                    System.out.println("Api call AResponse is not successful: " + response.code()); // Debug print
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Api call ARequest failed: " + t.getMessage()); // Debug print
                // Add any necessary error handling code here
            }
        });
    }

}
