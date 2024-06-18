package com.example.myapplication.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
//setLayoutManager
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.Adaptadores.ActorsListAdapter;
import com.example.myapplication.Adaptadores.CategoryEachFilmListAdapter;
import com.example.myapplication.Dominio.MovieItem;
import com.example.myapplication.R;
import com.google.gson.Gson;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;


public class DetailActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressBar;
    private TextView titleTxt, movieRateTxt, movieReleaseYearTxt, movieSummaryTxt, movieTimeTxt, movieActorsInfoTxt, movieCountryTxt;
    private int idFilm;
    private ImageView pic2, backImg, favImg;
    private RecyclerView.Adapter adapterActorList, adapterGenreList;
    private RecyclerView recyclerViewActorList, recyclerViewGenreList;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idFilm = getIntent().getIntExtra("id", 0);
        initView();
        sendRequest();

    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        progressBar.setVisibility(android.view.View.VISIBLE);
        scrollView.setVisibility(android.view.View.GONE);

        mStringRequest= new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies/" + idFilm, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                progressBar.setVisibility(android.view.View.GONE);
                scrollView.setVisibility(android.view.View.VISIBLE);

                MovieItem item= gson.fromJson(response, MovieItem.class);
                Glide.with(DetailActivity.this)
                        .load(item.getPoster())
                        .into(pic2);

                titleTxt.setText(item.getTitle());
                movieRateTxt.setText(item.getImdbRating());
                movieReleaseYearTxt.setText(item.getYear());
                movieSummaryTxt.setText(item.getPlot());
                movieTimeTxt.setText(item.getRuntime());
                movieActorsInfoTxt.setText(item.getActors());
                movieCountryTxt.setText(item.getCountry());
                if(item.getImages() != null){
                    adapterActorList = new ActorsListAdapter(item.getImages());
                    recyclerViewActorList.setAdapter(adapterActorList);
                }
                if(item.getGenres() != null) {
                    adapterGenreList = new CategoryEachFilmListAdapter(item.getGenres());
                    recyclerViewGenreList.setAdapter(adapterGenreList);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(android.view.View.GONE);
                android.util.Log.i("Errorijillo", "on error response:" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void initView() {
        titleTxt = findViewById(R.id.movieNameTxt);
        progressBar = findViewById(R.id.progressBarDetail);
        scrollView = findViewById(R.id.scrollView2);
        pic2 = findViewById(R.id.picDetail);
        movieRateTxt = findViewById(R.id.movieStar);
        movieReleaseYearTxt = findViewById(R.id.movieReleaseYear);
        movieSummaryTxt = findViewById(R.id.movieSummary);
        movieTimeTxt = findViewById(R.id.movieTime);
        movieActorsInfoTxt = findViewById(R.id.MovieActorInfo);
        movieCountryTxt = findViewById(R.id.movieCountry);
        backImg = findViewById(R.id.backImg);
        favImg = findViewById(R.id.FavIcon);
        recyclerViewActorList = findViewById(R.id.ActorRecyclerView);
        recyclerViewGenreList = findViewById(R.id.genreView);
        recyclerViewActorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGenreList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        progressBar = findViewById(R.id.progressBarDetail);
        backImg.setOnClickListener(v -> finish());
        favImg.setOnClickListener(v -> finish());//REEMPLAZAR CON METODO PARA AGREGAR O QUITAR PELICULA DE FAVORITOS. DARLE UN TINTE(android:tint="")AL ELEMENTO CUANDO ya este o no este en favoritos



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