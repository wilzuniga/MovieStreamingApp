package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adaptadores.FilmListAdapterHor;
import com.example.myapplication.Dominio.Datum;
import com.example.myapplication.Dominio.ListFilm;

import com.example.myapplication.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class TopActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTopMovies;
    private RequestQueue mRequestQueue;
    private TextView searchBar;
    private RecyclerView.Adapter adaptertopmovies;

    private StringRequest mStringRequest;
    private ProgressBar loading;
    private String user;
    private ImageView Explorar, Fav, cartelera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = getIntent().getStringExtra("user");
        initView();
        sendRequestTop();
        Explorar.setOnClickListener(v -> {
            Intent intent = new Intent(TopActivity.this, MainActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });
        Fav.setOnClickListener(v -> {

            Intent intent = new Intent(TopActivity.this, FavoritosActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });
        cartelera.setOnClickListener(v -> {

            Intent intent = new Intent(TopActivity.this, CarteleraActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });



    }

    private void initView() {
        recyclerViewTopMovies = findViewById(R.id.ViewFavs);
        recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loading = findViewById(R.id.progressBarfav);
        Explorar = findViewById(R.id.eplorar);
        Fav = findViewById(R.id.favoritos);
        cartelera = findViewById(R.id.cartelera);

    }

    private void sendRequestTop(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading.setVisibility(View.VISIBLE);
        System.out.println("holi");
        mStringRequest = new StringRequest(Request.Method.GET, "https://api.cosomovies.xyz/api/utils/toprated", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("holi2");
                Gson gson = new Gson();
                loading.setVisibility(View.GONE);

                // Parse the JSON response into a ListFilm object
                Datum[] datumArray = gson.fromJson(response, Datum[].class);

                // Get the list of Datum objects from the ListFilm object
                List<Datum> dataList = Arrays.asList(datumArray);

                Intent in = getIntent();
                String user = in.getStringExtra("user");

                // Initialize and set the adapter
                adaptertopmovies = new FilmListAdapterHor(dataList, user);
                recyclerViewTopMovies.setAdapter(adaptertopmovies);
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Log.i("Errorijillo", "on error response:" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}