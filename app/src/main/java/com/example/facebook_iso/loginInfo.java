package com.example.facebook_iso;

import android.net.Uri;

public class loginInfo {
    private String userName;
    private String password;
    private String name;
    private Boolean New;
    private Uri profilePic;

    loginInfo(String userName, String password){
        this.userName=userName;
        this.password=password;
        this.New = false;
    }

    loginInfo(String name, String userName, String password){
        this.name=name;
        this.userName=userName;
        this.password=password;
        this.New = false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getNew() {
        return New;
    }

    public void setNew(Boolean New) {
        this.New = New;
    }

    public Uri getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = profilePic;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
