package com.example.facebook_iso.login;

import java.util.ArrayList;

public class logininfo_list {
    private static logininfo_list instance;
    private ArrayList<loginInfo> list;

    private logininfo_list() {
        list = new ArrayList<>();
    }

    public static synchronized logininfo_list getInstance() {
        if (instance == null) {
            instance = new logininfo_list();
        }
        return instance;
    }

    public void add(loginInfo loginInfo) {
        list.add(loginInfo);
    }

    public ArrayList<loginInfo> getlist() {
        return list;
    }
    public loginInfo find_by_user(String user){
        for (loginInfo info: this.list){
            if (user.equals(info.getUserName())){
                return info;
            }
        }
        return null;
    }
}
