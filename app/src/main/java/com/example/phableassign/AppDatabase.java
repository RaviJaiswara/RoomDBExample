package com.example.phableassign;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.phableassign.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
