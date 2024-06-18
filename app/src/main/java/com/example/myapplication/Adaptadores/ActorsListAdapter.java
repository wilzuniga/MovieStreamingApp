package com.example.myapplication.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;

public class ActorsListAdapter extends RecyclerView.Adapter<ActorsListAdapter.ViewHolder> {
    List<String> images;
    Context context;

    public ActorsListAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public ActorsListAdapter.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_actors, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ActorsListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(images.get(position))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        public ViewHolder(android.view.View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.itemImages);
        }
    }
}
