package com.example.showcountry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    public Adapter(List<MainData> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Adapter.Viewholder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View ViewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_info, parent, false);
        return new Viewholder(ViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull Adapter.Viewholder holder, int position) {

        //Initialize main data
        MainData data = dataList.get(position);
        //initializing database
        database = RoomDB.getInstance(context);

        String name = data.getName();
        String flag = data.getFlag();
        String capital = data.getCapital();
        String region = data.getRegion();
        String subRegion = data.getSubregion();
        String population = data.getPopulation();
        String border = data.getBorder();
        String language = data.getLanguage();

        holder.setData(name, flag, capital, region, subRegion, population, border, language);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

//        name, capital, flag(display image in app), region,
//        subregion, population, borders & languages

        private TextView cr_name, cr_capital, cr_region, cr_sub_region, cr_population, cr_border, cr_language;
        private ImageView cr_flag;

        public Viewholder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            cr_flag = itemView.findViewById(R.id.flag);
            cr_name = itemView.findViewById(R.id.name);
            cr_capital = itemView.findViewById(R.id.capital);
            cr_region = itemView.findViewById(R.id.region);
            cr_sub_region = itemView.findViewById(R.id.sub_region);
            cr_population = itemView.findViewById(R.id.population);
            cr_border = itemView.findViewById(R.id.border);
            cr_language = itemView.findViewById(R.id.language);
        }

        @SuppressLint("SetTextI18n")
        private void setData(String name, String flag, String capital, String region, String subRegion, String population, String border, String language) {

            cr_name.setText(name);
            Utils.fetchSvg(itemView.getContext(), flag, cr_flag);
            cr_capital.setText("Capital:- " + capital);
            cr_region.setText("Region:- " + region);
            cr_sub_region.setText("Sub Region:- " + subRegion);
            cr_population.setText("Population:- " + population);
            cr_border.setText("Border:- " + border);
            cr_language.setText("Language:- " + language);

        }

    }

}

