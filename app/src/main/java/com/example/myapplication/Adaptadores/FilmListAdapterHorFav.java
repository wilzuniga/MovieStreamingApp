package com.example.myapplication.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Activities.DetailActivity;
import com.example.myapplication.Dominio.Datum;
import com.example.myapplication.Dominio.FavMovieItem;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilmListAdapterHorFav extends RecyclerView.Adapter<FilmListAdapterHorFav.ViewHolder> {

    private List<FavMovieItem> items;
    private Context context;
    private String user;

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";


    public FilmListAdapterHorFav(List<FavMovieItem> items, String user) {
        this.items = items;
        this.user = user;
    }



    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_filmshor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        FavMovieItem datum = items.get(position);

        holder.titleTxt.setText(datum.getTitle());
        // Aquí configuramos las opciones para Glide
        RequestOptions requestOptions = new RequestOptions()
                .transforms(new CenterCrop(), new RoundedCorners(30));

        String posterUrl = BASE_IMAGE_URL + datum.getPosterPath();


        Glide.with(context)
                .load(posterUrl)
                .apply(requestOptions)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", datum.getId());
            intent.putExtra("user", user );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.MovieTitleTxt);
            pic = itemView.findViewById(R.id.piccs);
        }
    }
}
