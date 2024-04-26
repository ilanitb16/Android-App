package com.example.facebook_iso.api;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.MyApplication;
import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;
import com.example.facebook_iso.login.current_user;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void signUp(User user) {
        try {
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", user.getUsername());
            requestBodyJson.put("password", user.getPassword());
            requestBodyJson.put("displayName", user.getDisplayName());
            requestBodyJson.put("profilePic", user.getProfilePic());
            Object jsonParser = JsonParser.parseString(requestBodyJson.toString());

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
                                    signIn(user.getUsername(), user.getPassword());
                                } else if (jsonObject.has("message")) {
                                    String message = jsonObject.get("message").getAsString();
                                    Toast.makeText(MyApplication.context, "User Already Exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("UserAPI", "signUp Enqueue onFailure: " + t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
                    Log.d("UserAPI", "Enqueue onResponse: " + response.toString());

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
                                Login_Page.context.startActivity(feedPageActivity);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("UserAPI", "Enqueue onFailure: " + t.toString());
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
            Log.d("UserAPI", "Sign Up before Request Body: " + requestBodyJson.toString());
            Object jsonParser = JsonParser.parseString(requestBodyJson.toString());

            Call<JsonObject> call = userServiceAPI.updateUser(user.getUsername(), jsonParser, "Bearer " + token);
            Log.d("UserAPI", "Sign Up after Request Body: " + requestBodyJson.toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JsonObject jsonObject = response.body();
                            if (jsonObject != null) {
                                if (jsonObject.has("modifiedCount")) {
                                    Toast.makeText(FeedPage.adapter.getContext(), "user updated", Toast.LENGTH_SHORT).show();
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

    public void getUser(String username, Post post) {
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

                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void deleteUser(User user) {
        String username = user.getUsername();
        Call<JsonObject> call = userServiceAPI.deleteUser(username, "Bearer " + FeedPage.owner.getToken());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject.has("insertedId")) {
                        Intent LoginPageIntent = new Intent(FeedPage.context, Login_Page.class);
                        FeedPage.context.startActivity(LoginPageIntent);
                        Toast.makeText(FeedPage.adapter.getContext(), "user deleted", Toast.LENGTH_SHORT).show();
                    }

                } else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void getUserFriends(User user) {
        String token = user.getToken();
        Call<JsonObject> call = userServiceAPI.getUserFriends(user.getUsername(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        if (jsonObject.has("friends")) {
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
                        }

                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void deleteFriendsRequest(User friend) {
        String token = FeedPage.owner.getToken();
        Call<JsonObject> call = userServiceAPI.deleteFriendsRequest(FeedPage.owner.getUsername(), friend.getUsername(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            if (jsonObject.has("deletedCount")) {
                                Toast.makeText(FeedPage.adapter.getContext(), "friend request deleted", Toast.LENGTH_SHORT).show();
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
    }

    public void approveFriendsRequest(User friend) {
        String token = FeedPage.owner.getToken();
        Call<JsonObject> call = userServiceAPI.approveFriendsRequest(FeedPage.owner.getUsername(), friend.getUsername(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            if (jsonObject.has("modifiedCount")) {
                                Toast.makeText(FeedPage.adapter.getContext(), "friend request approved", Toast.LENGTH_SHORT).show();
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
    }
}
