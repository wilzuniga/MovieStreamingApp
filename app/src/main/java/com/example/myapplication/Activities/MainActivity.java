package com.example.myapplication.Activities;

import static java.lang.Character.getType;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.TextView;

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
import com.example.myapplication.Adaptadores.SliderAdapters;
import com.example.myapplication.Dominio.Datum;
import com.example.myapplication.Dominio.GenresItem;
import com.example.myapplication.Dominio.GenresResponse;
import com.example.myapplication.Dominio.ListFilm;
import com.example.myapplication.Dominio.SliderItems;


import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterTopMovies, adapterUpcomingMovies, adapterCategoryMovies;
    public  static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private TextView searchBar;
    private  RecyclerView recyclerViewTopMovies, recyclerViewUpcomingMovies, recyclerViewCategoryMovies;
    private RequestQueue mRequestQueue;
    private OkHttpClient client = new OkHttpClient();

    private StringRequest mStringRequest, mStringRequest2, mStringRequest3;
    private ProgressBar loading, loading2, loading3;

    private ImageView favs, cartelera, top ;
    private EditText searched;
    private String user;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        initView();
        user = getIntent().getStringExtra("user");
        System.out.println("user en el mainnnn e inveeded" + user);
        banners();
        sendRequestBestMovies();
        sendRequestProxMovies();
        sendRequestCategorias();

        EditText searched = findViewById(R.id.searchBar);
        searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivityVaria.class);
            intent.putExtra("user", user );
            intent.putExtra("searchText", searched.getText().toString());
            startActivity(intent);
        });

        favs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritosActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });

        cartelera.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CarteleraActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });

        top.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });



    }

    private void sendRequestBestMovies() {//hace el llenado de la seccion de mejores peliculas
        mRequestQueue = Volley.newRequestQueue(this);
        loading.setVisibility(View.VISIBLE);
        mStringRequest= new StringRequest(Request.Method.GET, "https://api.cosomovies.xyz/api/utils/toprated", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loading.setVisibility(View.GONE);

                // Assuming response is an array of Datum objects
                Datum[] datumArray = gson.fromJson(response, Datum[].class);

                // Convert array to List<Datum> if needed
                List<Datum> items = Arrays.asList(datumArray);

                Intent in = getIntent();
                String user = in.getStringExtra("user");
                System.out.println("user en el main" + user);

                // Set the adapter with the list of Datum objects
                adapterTopMovies = new FilmListAdapter(items, user);
                recyclerViewTopMovies.setAdapter(adapterTopMovies);
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


    private void sendRequestProxMovies() {//hace el llenado de la seccion de proximas peliculas
        mRequestQueue = Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);
        mStringRequest3= new StringRequest(Request.Method.GET, "https://api.cosomovies.xyz/api/utils/upcoming", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loading3.setVisibility(View.GONE);

                // Assuming response is an array of Datum objects
                Datum[] datumArray = gson.fromJson(response, Datum[].class);

                // Convert array to List<Datum> if needed
                List<Datum> items = Arrays.asList(datumArray);

                Intent in = getIntent();
                String user = in.getStringExtra("user");
                System.out.println(user);

                // Set the adapter with the list of Datum objects
                adapterUpcomingMovies = new FilmListAdapter(items, user);
                recyclerViewUpcomingMovies.setAdapter(adapterUpcomingMovies);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading3.setVisibility(View.GONE);
                Log.i("Errorijillo", "on error response:" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest3);
    }

    private void sendRequestCategorias() {//hace el llenado de la seccion de etiquetas de categoria
        mRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        mStringRequest2= new StringRequest(Request.Method.GET, "https://api.cosomovies.xyz/api/utils/genres", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                loading2.setVisibility(View.GONE);

                // Parse the JSON response into a GenresResponse object
                GenresResponse genresResponse = gson.fromJson(response, GenresResponse.class);

                // Get the list of GenresItem objects from the GenresResponse object
                List<GenresItem> catList = genresResponse.getGenres();

                // Set the adapter with the list of GenresItem objects
                adapterCategoryMovies = new CategoryListAdapter(catList);
                recyclerViewCategoryMovies.setAdapter(adapterCategoryMovies);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading2.setVisibility(View.GONE);
                Log.i("Errorijillo", "on error response:" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest2);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPagerSlider);
        recyclerViewTopMovies = findViewById(R.id.view1);
        recyclerViewTopMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpcomingMovies = findViewById(R.id.view3);
        recyclerViewUpcomingMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategoryMovies = findViewById(R.id.view2);
        recyclerViewCategoryMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loading = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);
        loading3 = findViewById(R.id.progressBar3);
        searchBar = findViewById(R.id.searchBar);
        favs = findViewById(R.id.favview);
        cartelera = findViewById(R.id.cartelera);
        top = findViewById(R.id.top);
    }

    private void banners() {
        //ACA SE GENERARAN UNA CANTIDAD N DE PELICULAS ALEATORIAS PARA QUE ESTEN EN EL BANER, SE DEBERA PONER EL BANER EN DONDE ESTAN LOS QUE DICEN WIDE
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide));//WIDE
        sliderItems.add(new SliderItems(R.drawable.wide1));//WIDE
        sliderItems.add(new SliderItems(R.drawable.wide7));//WIDE-PRUEBA CON UNO ESTRECHO


        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }



    //oculta la barra de navegacion y la barra de estado
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