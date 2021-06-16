package com.example.showcountry;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private Button delete,reload;
    private TextView noInternet;
    private Adapter adapter;
    private Dialog loadingDialog;
    List<MainData> mainDataList = new ArrayList<>();
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        name, capital, flag(display image in app), region,subregion, population, borders & languages.
//        Search by region: Africa, Americas, Asia, Europe, Oceania
//        https://restcountries.eu/rest/v2/region/{region}
//        https://restcountries.eu/rest/v2/region/europe

        //........................ loading dialog start ......................//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_layout);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //........................ loading dialog start ......................//


        noInternet = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.cycle_view);
        delete = findViewById(R.id.button);
        reload = findViewById(R.id.button2);
        requestQueue = Server.getmInstance(this).getRequestQueue();

        //Initialize Linear Layout Manager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);


        //Initialize database
        database = RoomDB.getInstance(this);
        //Store data value in data list
        mainDataList = database.mainDao().getAll();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                fetchData();
            } else{
                Toast.makeText(MainActivity.this, "old list:  "+String.valueOf(mainDataList.size()), Toast.LENGTH_SHORT).show();
                adapter = new Adapter(mainDataList, MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        reload.setOnClickListener(v ->{
            ConnectivityManager connectivityManager2 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo2 = connectivityManager2.getActiveNetworkInfo();

            if (networkInfo2 != null && networkInfo2.isConnected()) {
                fetchData();
            } else{
                Toast.makeText(this, "Please connect to the Internet and try again", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //delete all data from database
                database.mainDao().reset(mainDataList);
                mainDataList.clear();
                //mainDataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, String.valueOf(mainDataList.size()), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void fetchData() {
        //clearing all data before fetching new set of data
        loadingDialog.show();
        database.mainDao().reset(mainDataList);
        mainDataList.clear();


        String url = "https://restcountries.eu/rest/v2/region/asia";
        //getting data from url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String countryName = jsonObject.getString("name");
                        String capital = jsonObject.getString("capital");
                        String flag = jsonObject.getString("flag");
                        String region = jsonObject.getString("region");
                        String subRegion = jsonObject.getString("subregion");
                        String population = jsonObject.get("population").toString();

                        JSONArray border_raw = jsonObject.getJSONArray("borders");

                        StringWriter border = new StringWriter();
                        border.append(border_raw.getString(0));
                        for (int j = 1; j < border_raw.length();j++){
                            border.append(", "+border_raw.getString(j));
                        }

                        String bb = String.valueOf(border);
                        //Toast.makeText(MainActivity.this, bb, Toast.LENGTH_SHORT).show();

                        JSONArray lan = jsonObject.getJSONArray("languages");
                        StringWriter lang = new StringWriter();

                        JSONObject ll = lan.getJSONObject(0);
                        lang.append(ll.getString("name"));

                        for (int k = 1; k < lan.length();k++){
                            ll = lan.getJSONObject(k);
                            lang.append(", "+ll.getString("name"));
                        }

                        String language = String.valueOf(lang);
                        //Toast.makeText(MainActivity.this, language, Toast.LENGTH_SHORT).show();

                        MainData data = new MainData();
                        //Set data to the data
                        data.setName(countryName);
                        data.setCapital(capital);
                        data.setFlag(flag);
                        data.setRegion(region);
                        data.setSubregion(subRegion);
                        data.setPopulation(population);
                        data.setBorder(bb);
                        data.setLanguage(language);
                        //Insert data in database
                        database.mainDao().insert(data);

                    } catch (JSONException e) {
                        loadingDialog.dismiss();
                        e.printStackTrace();
                    }
                }
                mainDataList.addAll(database.mainDao().getAll());
                adapter = new Adapter(mainDataList, MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkInternetConnection((int) (1000 - SystemClock.elapsedRealtime() % 1000));
    }

    private void checkInternetConnection(long delay) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (networkInfo != null && networkInfo.isConnected()) {
                noInternet.setVisibility(View.GONE);
            } else {
                noInternet.setVisibility(View.VISIBLE);
            }
            checkInternetConnection(delay);
        }, delay);
    }
}