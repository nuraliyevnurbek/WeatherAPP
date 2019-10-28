package com.example.weatherapp.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = DataBaseForApp.class, version = 1,exportSchema = true)
public abstract class DataBase extends RoomDatabase {
    abstract public DAO dao();
}
