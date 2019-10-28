package com.example.weatherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.weatherapp.DB.DataBase;
import com.example.weatherapp.DB.DataBaseForApp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapterForApp.OnDataClickListener, RecyclerViewAdapterForApp.OnLongDataClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.cityName)
    EditText cityName;

    DataBase appDataBase;
    int a = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appDataBase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "WeatherData").build();
        (new LoadAsyncTask()).execute();
        recyclerView.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onClickData(int id) {

        Intent intent = new Intent(MainActivity.this, dataActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }



    @Override
    public void onLongClickData(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        (new DeleteAsyncTask()).execute(id);
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    class LoadAsyncTask extends AsyncTask<Void, Void, List<DataBaseForApp>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<DataBaseForApp> doInBackground(Void... voids) {
            return appDataBase.dao().getAll();
        }

        @Override
        protected void onPostExecute(List<DataBaseForApp> dataBaseForApps) {
            super.onPostExecute(dataBaseForApps);

            if (!dataBaseForApps.isEmpty()) {
                 RecyclerViewAdapterForApp adapter = new RecyclerViewAdapterForApp(MainActivity.this, dataBaseForApps, MainActivity.this, MainActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    class DeleteAsyncTask extends AsyncTask<Integer, Void, List<DataBaseForApp>> {
        @Override
        protected List<DataBaseForApp> doInBackground(Integer... integers) {
            appDataBase.dao().deleteByUserId(integers[0]);
            return appDataBase.dao().getAll();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(List<DataBaseForApp> dataBaseForApps) {
            super.onPostExecute(dataBaseForApps);

            if (!dataBaseForApps.isEmpty()) {
                RecyclerViewAdapterForApp adapter = new RecyclerViewAdapterForApp(MainActivity.this, dataBaseForApps, MainActivity.this, MainActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    class ClearAsyncTask extends AsyncTask<Void, Void, List<DataBaseForApp>> {

        @Override
        protected List<DataBaseForApp> doInBackground(Void... voids) {
            appDataBase.dao().deleteAll();
            return appDataBase.dao().getAll();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(List<DataBaseForApp> dataBaseForApps) {
            super.onPostExecute(dataBaseForApps);
            RecyclerViewAdapterForApp adapter = new RecyclerViewAdapterForApp(MainActivity.this, dataBaseForApps, MainActivity.this, MainActivity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);

        }
    }


    @OnClick(R.id.resultButton)
    public void show() {


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            OkHttpClient client = new OkHttpClient();
            String url = "https://openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22";
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

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WeatherData read;
                                Gson gson = new Gson();
                                String text;
                                try {
                                    read = gson.fromJson(myResponse, WeatherData.class);
                                    text = read.getWind().getSpeed() + "  " + read.getMAIN().getTemp() + "  " + read.getWeather().get(0).getMain();

                                    Intent i = new Intent(MainActivity.this, DataShow.class);
                                    i.putExtra("cityName", cityName.getText().toString());
                                    startActivityForResult(i, 1);

                                } catch (Exception e) {

                                    Toast.makeText(MainActivity.this, "Wrong location!", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }


                            }
                        });
                    } else {
                        MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "Wrong location!", Toast.LENGTH_SHORT).show());
                    }
                }
            });

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                (new LoadAsyncTask()).execute();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @OnClick(R.id.historyButton)
    public void openHistory() {

        if (a == 1) {
            recyclerView.setVisibility(View.VISIBLE);
            a = 0;
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            a = 1;
        }

    }

    @OnLongClick(R.id.historyButton)
    public void clear() {


        new AlertDialog.Builder(this)
                .setTitle("Clear history")
                .setMessage("Are you sure you want to clear histry?")
                .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        (new ClearAsyncTask()).execute();
                    }
                }).show();
    }

}
