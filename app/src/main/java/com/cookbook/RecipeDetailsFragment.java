package com.cookbook;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookbook.Model.Recipe;
import com.squareup.picasso.Picasso;


public class RecipeDetailsFragment extends Fragment {
    Recipe recipe = new Recipe();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        TextView name = v.findViewById(R.id.recipe_name);
        TextView instructions = v.findViewById(R.id.recipe_instructions);
        TextView ingredients = v.findViewById(R.id.recipe_ingredients);
        ImageView image = v.findViewById(R.id.recipe_image);

        // Set text views to be multiline
        name.setSingleLine(false);
        instructions.setSingleLine(false);
        ingredients.setSingleLine(false);

        // Build recipe from arguments
        recipe.setName(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeName());
        recipe.setIngredients(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeIngredients());
        recipe.setInstructions(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeInstructions());
        recipe.setImageUrl(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getImageUrl());

        // inject values into page
        name.setText(recipe.getName());
        ingredients.setText(recipe.getIngredients());
        instructions.setText(recipe.getInstructions());
        if (recipe.getImageUrl() != null) {
            Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.placeholder).into(image);
        }

        return v;
    }
}