package com.cookbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookbook.Model.CurrentUser;
import com.cookbook.Model.Model;
import com.cookbook.Model.ModelFireBase;
import com.cookbook.Model.Recipe;
import com.squareup.picasso.Picasso;


public class EditRecipe extends Fragment {
    Recipe recipe = new Recipe();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        TextView name = v.findViewById(R.id.editRecipeName);
        TextView ingredients = v.findViewById(R.id.editIngredients);
        TextView instructions = v.findViewById(R.id.editInstructions);
        ImageView image = v.findViewById(R.id.editRecipeImage);
        Button saveBtn = v.findViewById(R.id.editSaveButton);
        Button deleteBtn = v.findViewById(R.id.deleteRecipeButton);

        // Set text views to be multiline
        name.setSingleLine(false);
        instructions.setSingleLine(false);
        ingredients.setSingleLine(false);

        // Build recipe from arguments
        recipe.setName(EditRecipeArgs.fromBundle(getArguments()).getRecipeName());
        recipe.setIngredients(EditRecipeArgs.fromBundle(getArguments()).getRecipeIngredients());
        recipe.setInstructions(EditRecipeArgs.fromBundle(getArguments()).getRecipeInstructions());
        recipe.setImageUrl(EditRecipeArgs.fromBundle(getArguments()).getImageUrl());
        recipe.setUser(CurrentUser.instance.getUser().getName());

        // inject values into page
        name.setText(recipe.getName());
        ingredients.setText(recipe.getIngredients());
        instructions.setText(recipe.getInstructions());
        if (recipe.getImageUrl() != null) {
            Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.placeholder).into(image);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipe.setIngredients(ingredients.getText().toString());
                recipe.setInstructions(instructions.getText().toString());
                Model.instance.editRecipe(recipe, new Model.AddRecipeListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(v).popBackStack();
                    }
                });
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance.deleteRecipe(recipe, new Model.AddRecipeListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(v).popBackStack();
                    }
                });
            }
        });

        return v;
    }
}