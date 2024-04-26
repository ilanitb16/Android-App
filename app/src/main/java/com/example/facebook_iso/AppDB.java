package com.example.facebook_iso;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;


@Database(entities = {User.class,Post.class}, version = 29)
@TypeConverters(Converters.class)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PostDao postDao();
}
