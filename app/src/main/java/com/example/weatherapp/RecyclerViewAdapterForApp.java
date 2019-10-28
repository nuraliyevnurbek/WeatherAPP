package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.DB.DataBaseForApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class RecyclerViewAdapterForApp extends RecyclerView.Adapter<RecyclerViewAdapterForApp.MyViewHolder> {

    private List<DataBaseForApp> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private OnDataClickListener onDataClickListener;
    private OnLongDataClickListener onLongDataClickListener;



    public RecyclerViewAdapterForApp(Context context, List<DataBaseForApp> list,OnDataClickListener onDataClickListener,OnLongDataClickListener onLongDataClickListener) {
        this.data = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.onDataClickListener=onDataClickListener;
        this.onLongDataClickListener=onLongDataClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterForApp.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycle_view_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterForApp.MyViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cityLocation)
        TextView location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);




        }

        void bind(DataBaseForApp dataBaseForApp){
            location.setText(dataBaseForApp.location);
        }

        @OnClick(R.id.dataLine)
        public void onClickData() {
            onDataClickListener.onClickData(data.get(getAdapterPosition()).id);
        }

        @OnLongClick(R.id.dataLine)
        public void onLongClickData() {
            onLongDataClickListener.onLongClickData(data.get(getAdapterPosition()).id);
        }


    }

    public interface OnDataClickListener {
        void onClickData(int id);
    }

    public interface OnLongDataClickListener {
        void onLongClickData(int id);
    }


}
