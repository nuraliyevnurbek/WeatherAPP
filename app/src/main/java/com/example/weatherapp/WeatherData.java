package com.example.weatherapp;

import com.example.weatherapp.WeatherInformation.MAIN;
import com.example.weatherapp.WeatherInformation.SYS;
import com.example.weatherapp.WeatherInformation.Weather;
import com.example.weatherapp.WeatherInformation.Wind;

import java.util.List;

public class WeatherData {

    List<Weather> weather;
    Wind wind;
    MAIN main;
    SYS sys;
    String name;

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public MAIN getMAIN() {
        return main;
    }

    public SYS getSys() { return sys; }

    public String getName() { return name; }
}
