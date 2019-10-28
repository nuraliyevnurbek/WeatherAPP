package com.example.weatherapp.WeatherInformation;

import java.io.Serializable;

public class Weather implements Serializable {
    int id;
    String main;
    String description;

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }
}
