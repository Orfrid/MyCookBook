package com.example.mycookbook.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycookbook.R;
import com.example.mycookbook.Model.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder {
    public RecipesAdapter.OnItemClickListener listener;
    TextView recipeName;
    ImageView recipeImage;
    int position;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeName = itemView.findViewById(R.id.list_row_name);
        recipeImage = itemView.findViewById(R.id.list_row_image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    public void bindData(Recipe recipe, int position) {
        recipeName.setText(recipe.name);
        this.position = position;
    }
}
