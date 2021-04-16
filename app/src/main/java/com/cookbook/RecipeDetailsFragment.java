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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookbook.Model.CurrentUser;
import com.cookbook.Model.Model;
import com.cookbook.Model.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;


public class RecipeDetailsFragment extends Fragment {
    Recipe recipe = new Recipe();
    CheckBox favBtn;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        TextView name = v.findViewById(R.id.recipe_name);
        TextView instructions = v.findViewById(R.id.recipe_instructions);
        TextView ingredients = v.findViewById(R.id.recipe_ingredients);
        ImageView image = v.findViewById(R.id.recipe_image);
        favBtn = v.findViewById(R.id.favorite_btn);

        // Set text views to be multiline
        name.setSingleLine(false);
        instructions.setSingleLine(false);
        ingredients.setSingleLine(false);

        // Build recipe from arguments
        recipe.setName(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeName());
        recipe.setIngredients(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeIngredients());
        recipe.setInstructions(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipeInstructions());
        recipe.setImageUrl(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getImageUrl());
        recipe.setUser(RecipeDetailsFragmentArgs.fromBundle(getArguments()).getUser());

        // inject values into page
        name.setText(recipe.getName());
        ingredients.setText(recipe.getIngredients());
        instructions.setText(recipe.getInstructions());
        if (recipe.getImageUrl() != null) {
            Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.placeholder).into(image);
        }
        if(CurrentUser.instance.getUser().getFavorites() != null &&
                CurrentUser.instance.getUser().getFavorites().size() > 0) {
            for (String fav : CurrentUser.instance.getUser().getFavorites()) {
                if (fav.equals(recipe.getName())) {
                    favBtn.setChecked(true);
                }
            }
        }

        favBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(CurrentUser.instance.getUser().getName() == null) {
                    favBtn.setChecked(!isChecked);
                    Snackbar mySnackbar = Snackbar.make(v, "Please Login", 1000);
                    mySnackbar.show();
                } else {
                    if (isChecked) {
                        addToFavorites();
                    } else {
                        removeFromFavorites();
                    }
                }
            }
        });
        return v;
    }

    void addToFavorites() {
        CurrentUser.instance.getUser().getFavorites().add(recipe.getName());
        Model.instance.editUser(CurrentUser.instance.getUser(), new Model.AddUserListener() {
            @Override
            public void onComplete() {
                favBtn.setChecked(true);
            }
        });
    }

    void removeFromFavorites() {
        if(CurrentUser.instance.getUser().getFavorites().contains(recipe.getName())) {
            CurrentUser.instance.getUser().getFavorites().remove(recipe.getName());
            Model.instance.editUser(CurrentUser.instance.getUser(), new Model.AddUserListener() {
                @Override
                public void onComplete() {
                    favBtn.setChecked(false);
                }
            });
        }
    }
}