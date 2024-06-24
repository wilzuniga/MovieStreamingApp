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
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private List<GenresItem> items;
    private Context context;

    public CategoryListAdapter(List<GenresItem> items) {
        this.items = items;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        GenresItem item = items.get(position);
        holder.TitleTxt.setText(item.getName());

        // Handle item click here if needed
        holder.itemView.setOnClickListener(v -> {
            // Handle click action
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            TitleTxt = itemView.findViewById(R.id.TitleTxt); // Adjust to your TextView ID
        }
    }
}
