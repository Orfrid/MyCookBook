package com.example.mycookbook;

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
import android.widget.TextView;

import com.example.mycookbook.Model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

        instructions.setSingleLine(false);
        recipe.name = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeName();
        recipe.ingredients = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeIngredients();
        recipe.instructions = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeInstructions();
        recipe.ingredients = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeIngredients();

        name.setText(recipe.name);
        ingredients.setText(recipe.ingredients);
        instructions.setText(recipe.instructions);
        return v;
    }
}