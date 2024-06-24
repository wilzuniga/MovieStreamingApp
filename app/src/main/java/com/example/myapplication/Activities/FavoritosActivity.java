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

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adaptadores.FilmListAdapterHor;
import com.example.myapplication.Adaptadores.FilmListAdapterHorFav;
import com.example.myapplication.Dominio.Datum;
import com.example.myapplication.Dominio.FavMovieItem;

import com.example.myapplication.Dominio.ListFilm;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FavoritosActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private RecyclerView recyclerViewFavoriteMovies;
    private RequestQueue mRequestQueue;
    private TextView searchBar;
    private RecyclerView.Adapter adapterFavMovies;
    private OkHttpClient client = new OkHttpClient();

    private StringRequest mStringRequest;
    private ProgressBar loading;

    private String user;

    private ImageView Explorar, cartelera, Top;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();

        user = getIntent().getStringExtra("user");


        sendRequestSearchMovie();
        Explorar.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritosActivity.this, MainActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });
        cartelera.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritosActivity.this, CarteleraActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });
        Top.setOnClickListener(v -> {

            Intent intent = new Intent(FavoritosActivity.this, TopActivity.class);
            intent.putExtra("user", user );
            startActivity(intent);
        });


    }

    private void initView() {
        recyclerViewFavoriteMovies = findViewById(R.id.ViewFavs);
        recyclerViewFavoriteMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loading = findViewById(R.id.progressBarfav);
        Explorar = findViewById(R.id.eplorar);
        cartelera = findViewById(R.id.cartelera);
        Top = findViewById(R.id.top);

    }

    private void sendRequestSearchMovie() {
        loading.setVisibility(View.VISIBLE);

        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String userJsonString = userJSON.toString();

        RequestBody body = RequestBody.create(userJsonString, JSON);

        Request request = new Request.Builder()
                .url("https://api.cosomovies.xyz/api/favorites")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    loading.setVisibility(View.GONE);
                    Log.i("Errorijillo", "on error response:" + e.toString());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    loading.setVisibility(View.GONE);
                });

                String responseBody = response.body().string();
                Log.d("onResponse: ", responseBody);

                try {
                    // Verifica si el JSON es un objeto o un array
                    JsonElement jsonElement = JsonParser.parseString(responseBody);
                    if (jsonElement.isJsonArray()) {
                        Gson gson = new Gson();
                        FavMovieItem[] datumArray = gson.fromJson(responseBody, FavMovieItem[].class);

                        // Get the list of Datum objects from the ListFilm object
                        List<FavMovieItem> dataList = Arrays.asList(datumArray);

                        // Initialize and set the adapter
                        runOnUiThread(() -> {
                            adapterFavMovies = new FilmListAdapterHorFav(dataList, user);
                            recyclerViewFavoriteMovies.setAdapter(adapterFavMovies);
                        });
                    } else if (jsonElement.isJsonObject()) {
                        // Maneja el caso en que la respuesta sea un objeto JSON
                        Gson gson = new Gson();
                        FavMovieItem datum = gson.fromJson(responseBody, FavMovieItem.class);

                        List<FavMovieItem> dataList = Collections.singletonList(datum);

                        // Initialize and set the adapter
                        runOnUiThread(() -> {
                            adapterFavMovies = new FilmListAdapterHorFav(dataList, user);
                            recyclerViewFavoriteMovies.setAdapter(adapterFavMovies);
                        });
                    } else {
                        throw new IllegalStateException("Unexpected JSON type");
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("JsonSyntaxException", "Failed to parse JSON: " + e.getMessage());
                }
            }
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