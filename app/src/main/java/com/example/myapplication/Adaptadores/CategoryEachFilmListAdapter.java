package com.example.myapplication.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Dominio.GenresItem;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryEachFilmListAdapter extends RecyclerView.Adapter<CategoryEachFilmListAdapter.viewHolder>{

    List<String> items;
    Context context;

    public CategoryEachFilmListAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public CategoryEachFilmListAdapter.viewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.viewholder_category, parent, false);
        return new viewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(CategoryEachFilmListAdapter.viewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        public viewHolder(@NotNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.TitleTxt);
        }
    }

}
