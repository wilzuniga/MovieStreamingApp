package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adaptadores.CategoryListAdapter;
import com.example.myapplication.Adaptadores.FilmListAdapter;
import com.example.myapplication.Adaptadores.FilmListAdapterHor;
import com.example.myapplication.Adaptadores.FilmListAdapterHorSearch;
import com.example.myapplication.Adaptadores.SliderAdapters;
import com.example.myapplication.Dominio.Datum;
import com.example.myapplication.Dominio.GenresItem;
import com.example.myapplication.Dominio.ListFilm;
import com.example.myapplication.Dominio.SearchMovieItem;
import com.example.myapplication.Dominio.SliderItems;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ActivityVaria extends AppCompatActivity {

    private RecyclerView.Adapter adapterSearchedMovies;
    private RecyclerView recyclerViewSearchedMovies;
    private RequestQueue mRequestQueue;
    private ImageView backImg;
    private StringRequest mStringRequest;
    private ProgressBar loading;

    private void sendRequestSearchMovie() {
        Intent intent = getIntent();
        String searchText = intent.getStringExtra("searchText");
        mRequestQueue = Volley.newRequestQueue(this);
        loading.setVisibility(View.VISIBLE);
        System.out.println(searchText);
        mStringRequest = new StringRequest(Request.Method.GET, "https://api.cosomovies.xyz/api/utils/search/movie/" + searchText, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("holi2");
                Gson gson = new Gson();
                loading.setVisibility(View.GONE);
                Log.d("onResponse: ", response);

                // Parse the JSON response into a ListFilm object
                SearchMovieItem[] datumArray = gson.fromJson(response, SearchMovieItem[].class);

                // Get the list of Datum objects from the ListFilm object
                List<SearchMovieItem> dataList = Arrays.asList(datumArray);

                // Initialize and set the adapter
                adapterSearchedMovies = new FilmListAdapterHorSearch(dataList);
                recyclerViewSearchedMovies.setAdapter(adapterSearchedMovies);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_varia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        sendRequestSearchMovie();


    }

    private void initView() {
        recyclerViewSearchedMovies = findViewById(R.id.recyclerViewSearchedMovies);
        recyclerViewSearchedMovies.setHasFixedSize(true);
        recyclerViewSearchedMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loading = findViewById(R.id.progressBar1);
        backImg = findViewById(R.id.backImg);
        backImg.setOnClickListener(v -> {
            finish();
        });
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