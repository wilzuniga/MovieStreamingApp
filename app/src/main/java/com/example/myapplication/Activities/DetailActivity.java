package com.example.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adaptadores.CategoryEachFilmListAdapter;
import com.example.myapplication.Dominio.Actor;
import com.example.myapplication.Dominio.MovieItem;
import com.example.myapplication.R;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    Context context = this; // 'this' here refers to the enclosing Activity context

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";

    private OkHttpClient client = new OkHttpClient();
    private String user; // Assuming you have the user information from LoginActivity
    private Integer idFilm;
    private ProgressBar progressBar;
    private NestedScrollView scrollView;
    private ImageView pic2, backImg, favImg;
    private TextView titleTxt, movieRateTxt, movieReleaseYearTxt, movieSummaryTxt, movieTimeTxt, movieActorsInfoTxt, movieCountryTxt;
    private RecyclerView recyclerViewActorList, recyclerViewGenreList;
    private CategoryEachFilmListAdapter adapterGenreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        // Initialize views
        initView();

        // Get the user and idFilm from the Intent or saved state
        user = getIntent().getStringExtra("user");
        idFilm = getIntent().getIntExtra("id", 0);
        System.out.println("user: " + user);
        System.out.println("idFilm: " + idFilm);

        // Send the request
        sendRequest();
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
        movieCountryTxt = findViewById(R.id.movieCountry);
        backImg = findViewById(R.id.backImg);
        favImg = findViewById(R.id.FavIcon);
        recyclerViewGenreList = findViewById(R.id.genreView);
        recyclerViewGenreList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        progressBar = findViewById(R.id.progressBarDetail);
        backImg.setOnClickListener(v -> salidaIntent());
        favImg.setOnClickListener(v -> sendRequestFav());//REEMPLAZAR CON METODO PARA AGREGAR O QUITAR PELICULA DE FAVORITOS. DARLE UN TINTE(android:tint="")AL ELEMENTO CUANDO ya este o no este en favoritos



    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        // Crear el JSON body
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("user", user);
            System.out.println("coso entra");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String userJsonString = userJSON.toString();
        Log.d("JSON", "userJsonString: " + userJsonString);


        RequestBody body = RequestBody.create(userJsonString, JSON);

        // Crear la solicitud POST
        Request request = new Request.Builder()
                .url("https://api.cosomovies.xyz/api/utils/search/id/" + idFilm)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("onfailure");
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Log.i("Errorijillo", "on error response:" + e.toString());
                    Toast.makeText(DetailActivity.this, "Error loading details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                });

                if (response.isSuccessful()) {
                    System.out.println("onsuccess");
                    String responseBody = response.body().string();
                    System.out.println(responseBody);
                    Gson gson = new Gson();
                    MovieItem item = gson.fromJson(responseBody, MovieItem.class);

                    runOnUiThread(() -> {
                        String PosterUrl = BASE_IMAGE_URL + item.getPosterPath();
                        Glide.with(DetailActivity.this)
                                .load(PosterUrl)
                                .into(pic2);

                        titleTxt.setText(item.getTitle());
                        movieRateTxt.setText(Integer.toString(item.getScore()));
                        movieReleaseYearTxt.setText(item.getReleaseDate());
                        movieSummaryTxt.setText(item.getOverview());
                        movieTimeTxt.setText(Integer.toString(item.getRuntime()));
                        movieCountryTxt.setText(item.getCountry().get(0));
                        if(item.isFav() == false){
                            favImg.setColorFilter(ContextCompat.getColor(context  , R.color.white), PorterDuff.Mode.SRC_IN);
                        }else{
                            favImg.setColorFilter(ContextCompat.getColor(context  , R.color.orange), PorterDuff.Mode.SRC_IN);
                        }


                        if (item.getGenres() != null) {
                            adapterGenreList = new CategoryEachFilmListAdapter(item.getGenres());
                            recyclerViewGenreList.setAdapter(adapterGenreList);
                        }

                    });
                } else {
                    System.out.println("onfailure2");
                    System.out.println(request.toString());
                    System.out.println(request.body().toString());
                    System.out.println(response.toString());
                    System.out.println(response.body().string());
                    runOnUiThread(() -> Toast.makeText(DetailActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show());
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

    private void sendRequestFav() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        // Crear el JSON body
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //verificar si la pelicula esta en favoritos
        String userJsonString = userJSON.toString();
        Log.d("JSON", "userJsonString: " + userJsonString);

        RequestBody body = RequestBody.create(userJsonString, JSON);

        // Crear la solicitud POST
        Request request = new Request.Builder()
                .url("https://api.cosomovies.xyz/api/utils/search/id/" + idFilm)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("onfailure");
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Log.i("Errorijillo", "on error response:" + e.toString());
                    Toast.makeText(DetailActivity.this, "Error adding to favorites", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                });

                if (response.isSuccessful()) {
                    System.out.println("onsuccess");
                    String responseBody = response.body().string();
                    System.out.println(responseBody);
                    Gson gson = new Gson();
                    MovieItem item = gson.fromJson(responseBody, MovieItem.class);

                    runOnUiThread(() -> {
                        if(item.isFav() == false){
                            sendRequestAddFav();
                        }else{
                            sendRequestRemoveFav();
                        }
                    });
                } else {
                    System.out.println("onfailure2");
                    System.out.println(request.toString());
                    System.out.println(request.body().toString());
                    System.out.println(response.toString());
                    System.out.println(response.body().string());
                    runOnUiThread(() -> Toast.makeText(DetailActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show());
                }
            }
        });
        }



    private void sendRequestAddFav() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        // Crear el JSON body
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("user", user);
            userJSON.put("id", idFilm);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String userJsonString = userJSON.toString();
        Log.d("JSON", "userJsonString: " + userJsonString);

        RequestBody body = RequestBody.create(userJsonString, JSON);

        // Crear la solicitud POST
        Request request = new Request.Builder()
                .url("https://api.cosomovies.xyz/api/favorites")
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("onfailure");
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Log.i("Errorijillo", "on error response:" + e.toString());
                    Toast.makeText(DetailActivity.this, "Error adding to favorites", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                });

                if (response.isSuccessful()) {
                    System.out.println("onsuccess");
                    String responseBody = response.body().string();
                    System.out.println(responseBody);
                    Gson gson = new Gson();
                    MovieItem item = gson.fromJson(responseBody, MovieItem.class);

                    runOnUiThread(() -> {
                        if(favImg.getColorFilter().equals(R.color.orange)){
                            favImg.setColorFilter(ContextCompat.getColor(context  , R.color.white), PorterDuff.Mode.SRC_IN);
                        }else{
                            favImg.setColorFilter(ContextCompat.getColor(context  , R.color.orange), PorterDuff.Mode.SRC_IN);
                        }
                    });
                } else {
                    System.out.println("onfailure2");
                    System.out.println(request.toString());
                    System.out.println(request.body().toString());
                    System.out.println(response.toString());
                    System.out.println(response.body().string());
                    runOnUiThread(() -> Toast.makeText(DetailActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show());
                }
            }
        });
        }

        private void sendRequestRemoveFav() {
            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);

            // Crear el JSON body
            JSONObject userJSON = new JSONObject();
            try {
                userJSON.put("user", user);
                userJSON.put("id", idFilm);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String userJsonString = userJSON.toString();
            Log.d("JSON", "userJsonString: " + userJsonString);

            RequestBody body = RequestBody.create(userJsonString, JSON);

            // Crear la solicitud POST
            Request request = new Request.Builder()
                    .url("https://api.cosomovies.xyz/api/favorites")
                    .delete(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    System.out.println("onfailure");
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Errorijillo", "on error response:" + e.toString());
                        Toast.makeText(DetailActivity.this, "Error removing from favorites", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    });

                    if (response.isSuccessful()) {
                        System.out.println("onsuccess");
                        String responseBody = response.body().string();
                        System.out.println(responseBody);
                        Gson gson = new Gson();
                        MovieItem item = gson.fromJson(responseBody, MovieItem.class);

                        runOnUiThread(() -> {
                                favImg.setColorFilter(ContextCompat.getColor(context  , R.color.white), PorterDuff.Mode.SRC_IN);

                        });
                    } else {
                        System.out.println("onfailure2");
                        System.out.println(request.toString());
                        System.out.println(request.body().toString());
                        System.out.println(response.toString());
                        System.out.println(response.body().string());
                        runOnUiThread(() -> Toast.makeText(DetailActivity.this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }

        private void salidaIntent(){
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

