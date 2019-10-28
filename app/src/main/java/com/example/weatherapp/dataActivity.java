package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.Update;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class dataActivity extends AppCompatActivity {

    @BindView(R.id.dataShowlayout2)
    LinearLayout data_Show_Layout;

    @BindView(R.id.location2)
    TextView locationName;
    @BindView(R.id.country2)
    TextView countryName;
    @BindView(R.id.weatherImage2)
    ImageView weatherIcon;
    @BindView(R.id.main2)
    TextView main;
    @BindView(R.id.windSpeed2)
    TextView windSpeed;
    @BindView(R.id.pressure2)
    TextView pressure;
    @BindView(R.id.humidity2)
    TextView humidity;
    @BindView(R.id.maxTemp2)
    TextView maxTemp;
    @BindView(R.id.Temp2)
    TextView Temp;
    @BindView(R.id.minTemp2)
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

    public int cityId;
    ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(dataActivity.this);
        cityId=getIntent().getIntExtra("id",0);
        database = Room.databaseBuilder(getApplicationContext(), DataBase.class, "WeatherData").build();
        (new getData()).execute(cityId);

    }

    class getData extends AsyncTask<Integer,Void, DataBaseForApp >{

        @Override
        protected DataBaseForApp doInBackground(Integer... integers) {
           return database.dao().getById(integers[0]);}


        @Override
        protected void onPostExecute(DataBaseForApp dataBaseForApp) {
            city=dataBaseForApp.location;
            locationName.setText(city);
            countryName.setText(dataBaseForApp.country);
            main.setText(dataBaseForApp.main);
            Temp.setText(dataBaseForApp.Temp);
            maxTemp.setText(dataBaseForApp.Max);
            minTemp.setText(dataBaseForApp.Min);
            humidity.setText(dataBaseForApp.humidity);
            windSpeed.setText(dataBaseForApp.wind);
            pressure.setText(dataBaseForApp.pressure);
            background(dataBaseForApp.main);

        }
    }

    public void background(String main){
        if (main.toLowerCase().equals("clear")) {
            data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny));
            weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunicon));
        }

        if (main.toLowerCase().equals("rain")) {
            data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainy));
            weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainicon));
        }

        if (main.toLowerCase().equals("snow") || main.toLowerCase().equals("drizzle")) {
            data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
            weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.snowicon));
        }

        if (main.toLowerCase().equals("clouds")) {
            data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudly));
            weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudicon));
        }

        if (main.toLowerCase().equals("fog") || main.toLowerCase().equals("mist")) {
            data_Show_Layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.fog));
            weatherIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.fogicon));
        }

    }


    @OnClick(R.id.Refresh)
    public void refresh(){
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

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

                        dataActivity.this.runOnUiThread(new Runnable() {
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

                                    if (read.getWeather().get(0).getMain().toLowerCase().equals("snow")) {
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

                                    (new Update()).execute(data);

                                } catch (Exception e) {

                                    e.printStackTrace();
                                }


                            }
                        });
                    }
                }
            });

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

        }
    }

    class Update extends AsyncTask<DataBaseForApp,Void,Void>{

        @Override
        protected Void doInBackground(DataBaseForApp... dataBaseForAppss) {
            database.dao().updateData(cityId,dataBaseForAppss[0].location,dataBaseForAppss[0].country,dataBaseForAppss[0].main, dataBaseForAppss[0].Temp, dataBaseForAppss[0].Min, dataBaseForAppss[0].Max,dataBaseForAppss[0].wind,dataBaseForAppss[0].humidity,dataBaseForAppss[0].pressure);
            return null;
        }
    }
}
