package com.example.mycookbook.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;
import com.example.mycookbook.Model.Recipe;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder>{
    public List<Recipe> data;
    LayoutInflater inflater;
    public RecipesAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_row, parent,false);
        RecipeViewHolder holder = new RecipeViewHolder(view);
        holder.listener = listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = data.get(position);
        holder.bindData(recipe, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
