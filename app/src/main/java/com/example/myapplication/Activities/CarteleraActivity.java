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
import com.example.myapplication.Dominio.ListFilm;

import com.example.myapplication.R;
import com.google.gson.Gson;

public class CarteleraActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCartelera;
    private RequestQueue mRequestQueue;
    private TextView searchBar;
    private RecyclerView.Adapter adapterFavMovies;

    private StringRequest mStringRequest;
    private ProgressBar loading;

    private ImageView Explorar, Fav, Top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cartelera);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        sendRequestCartelera();
        Explorar.setOnClickListener(v -> {
            startActivity(new Intent(CarteleraActivity.this, MainActivity.class));
        });
        Fav.setOnClickListener(v -> {
            startActivity(new Intent(CarteleraActivity.this, FavoritosActivity.class));
        });
        Top.setOnClickListener(v -> {
            startActivity(new Intent(CarteleraActivity.this, TopActivity.class));
        });
        searchBar.setOnClickListener(v -> {
            startActivity(new Intent(CarteleraActivity.this, ActivityVaria.class));
        });


    }


    private void initView() {
        recyclerViewCartelera = findViewById(R.id.ViewFavs);
        recyclerViewCartelera.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loading = findViewById(R.id.progressBarfav);
        Explorar = findViewById(R.id.eplorar);
        Fav = findViewById(R.id.favoritos);
        searchBar = findViewById(R.id.searchBar);
        Top = findViewById(R.id.top);

    }

    private void sendRequestCartelera(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading.setVisibility(View.VISIBLE);
        /*En la siguiente linea se llama una lista de peliculas desde el api que estes utilizando,
         * el cambio que tenes que hacer aca va a ser poner el link del api de walther y llamar
         * el campo de texto en el cual se escribe en el MAINACTIVITY*/
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=7", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Gson gson = new Gson();
                ListFilm results = gson.fromJson(response, ListFilm.class);;
                System.out.println(results.getData().get(0).getTitle());
                adapterFavMovies = new FilmListAdapterHor(results);
                recyclerViewCartelera.setAdapter(adapterFavMovies);
                System.out.println("Se ejecuto el onResponse");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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