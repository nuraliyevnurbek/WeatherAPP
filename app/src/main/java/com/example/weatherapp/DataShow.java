package com.example.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.weatherapp.DB.DataBase;
import com.example.weatherapp.DB.DataBaseForApp;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DataShow extends AppCompatActivity {

    @BindView(R.id.dataShowlayout)
    LinearLayout data_Show_Layout;

    @BindView(R.id.location)
    TextView locationName;
    @BindView(R.id.country)
    TextView countryName;
    @BindView(R.id.weatherImage)
    ImageView weatherIcon;
    @BindView(R.id.main)
    TextView main;
    @BindView(R.id.windSpeed)
    TextView windSpeed;
    @BindView(R.id.pressure)
    TextView pressure;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.maxTemp)
    TextView maxTemp;
    @BindView(R.id.Temp)
    TextView Temp;
    @BindView(R.id.minTemp)
    TextView minTemp;

    DataBase database;
    public String city;
    public String country;
    public String mainData;
    public String temp;
    public String maxtemp;
    public String mintemp;
    public String humidityy;
    public String pressuree;
    public String wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_data_show);
        ButterKnife.bind(DataShow.this);
        city = getIntent().getStringExtra("cityName").toUpperCase();

        database = Room.databaseBuilder(getApplicationContext(), DataBase.class, "WeatherData").build();

        OkHttpClient client = new OkHttpClient();
        String url = "https://openweathermap.org/data/2.5/weather?q=" + city + "&appid=b6907d289e10d714a6e88b30761fae22";
        final Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    DataShow.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            WeatherData read;
                            Gson gson = new Gson();
                            String text;
                            try {
                                read = gson.fromJson(myResponse, WeatherData.class);
                                if (read.getWeather().get(0).getMain().toLowerCase().equals("clear")) {
                                    data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny));
                                    weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunicon));
                                }

                                if (read.getWeather().get(0).getMain().toLowerCase().equals("rain")) {
                                    data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainy));
                                    weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainicon));
                                }

                                if (read.getWeather().get(0).getMain().toLowerCase().equals("snow") || read.getWeather().get(0).getMain().toLowerCase().equals("drizzle")) {
                                    data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
                                    weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.snowicon));
                                }

                                if (read.getWeather().get(0).getMain().toLowerCase().equals("clouds")) {
                                    data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudly));
                                    weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudicon));
                                }

                                if (read.getWeather().get(0).getMain().toLowerCase().equals("fog") || read.getWeather().get(0).getMain().toLowerCase().equals("mist")) {
                                    data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.fog));
                                    weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.fogicon));
                                }

                                country=read.getSys().getCountry();
                                mainData=read.getWeather().get(0).getMain();
                                wind=read.getWind().getSpeed() + "m/s";
                                humidityy=read.getMAIN().getHumidity() + "%";
                                pressuree=read.getMAIN().getPressure() + "Pa";
                                mintemp=read.getMAIN().getTemp_min() + " C";
                                maxtemp=read.getMAIN().getTemp_max() + " C";
                                temp=read.getMAIN().getTemp() + " C";

                                locationName.setText(city);
                                countryName.setText(country);
                                main.setText(mainData);
                                windSpeed.setText(wind);
                                humidity.setText(humidityy);
                                pressure.setText(pressuree);
                                minTemp.setText(mintemp);
                                maxTemp.setText(maxtemp);
                                Temp.setText(temp);

                            } catch (Exception e) {

                                e.printStackTrace();
                            }


                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.saveButton)
    public void save() {
        DataBaseForApp data = new DataBaseForApp();
        data.location = city;
        data.country=country;
        data.main=mainData;
        data.wind=wind;
        data.Temp=temp;
        data.Max=maxtemp;
        data.Min=mintemp;
        data.pressure=pressuree;
        data.humidity=humidityy;

        (new Save()).execute(data);
    }

    @OnClick(R.id.quiteButton)
    public void quit(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",1);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    class Save extends AsyncTask<DataBaseForApp, Void, String> {


        @Override
        protected String doInBackground(DataBaseForApp... dataBaseForApps) {
            database.dao().insertAll(dataBaseForApps[0]);
            return "success";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            Toast.makeText(DataShow.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
