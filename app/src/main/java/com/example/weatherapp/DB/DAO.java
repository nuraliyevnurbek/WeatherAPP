package com.example.weatherapp.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {

    @Query("SELECT*FROM WeatherData WHERE id=:id")
    DataBaseForApp getById(int id);

    @Query("SELECT*FROM WeatherData")
    List<DataBaseForApp> getAll();

    @Insert
    void insertAll(DataBaseForApp... dataBaseForApps);

    @Query("DELETE FROM WeatherData WHERE id = :Id")
    void deleteByUserId(Integer Id);

    @Query("DELETE FROM WeatherData")
    void deleteAll();

    @Query("UPDATE WeatherData SET  location= :location,country=:country,main=:main, Temperature=:temp,Min=:min,Max=:max,wind=:wind,humidity=:humidity,pressure=:pressure WHERE id = :id")
    int updateData(int id,String location,String country,String main,String temp,String min,String max,String wind,String humidity,String pressure);

}
