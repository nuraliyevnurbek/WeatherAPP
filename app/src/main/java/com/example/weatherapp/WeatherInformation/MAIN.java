package com.example.weatherapp.WeatherInformation;

import java.io.Serializable;

public class MAIN implements Serializable {

    double temp;
    double pressure;
    double humidity;
    double temp_min;
    double temp_max;



    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }
}
