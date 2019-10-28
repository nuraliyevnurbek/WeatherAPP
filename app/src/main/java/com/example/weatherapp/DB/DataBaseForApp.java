package com.example.weatherapp.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WeatherData")
public class DataBaseForApp {


    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "main")
    public String main;


    @ColumnInfo(name = "Temperature")
    public String Temp;

    @ColumnInfo(name = "Max")
    public String Max;

    @ColumnInfo(name = "Min")
    public String Min;

    @ColumnInfo(name = "wind")
    public String wind;

    @ColumnInfo(name = "humidity")
    public String humidity;

    @ColumnInfo(name = "pressure")
    public String pressure;




}
