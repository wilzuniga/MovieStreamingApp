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
import com.example.myapplication.Dominio.ListFilm;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

public class FilmListAdapterHor extends RecyclerView.Adapter<FilmListAdapterHor.viewHolder>{

    ListFilm items;
    Context context;

    public FilmListAdapterHor(ListFilm items) {
        this.items = items;
    }

    @Override
    public FilmListAdapterHor.viewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.viewholder_filmshor, parent, false);
        return new viewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(FilmListAdapterHor.viewHolder holder, int position) {
        holder.titleTxt.setText(items.getData().get(position).getTitle());
        holder.moviecat.setText(items.getData().get(position).getGenres().get(0));
        holder.movieYeartx.setText(items.getData().get(position).getYear());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load(items.getData().get(position).getPoster())
                .apply(requestOptions)
                .into(holder.pic);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("id", items.getData().get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.getData().size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, movieYeartx, moviecat;
        ImageView pic;


        public viewHolder(@NotNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.MovieTitleTxt);
            pic = itemView.findViewById(R.id.piccs);
            movieYeartx = itemView.findViewById(R.id.MovieYearTxt);
            moviecat = itemView.findViewById(R.id.MovieCategoryTxt);
        }
    }

}
