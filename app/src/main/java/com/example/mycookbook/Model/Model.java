package com.example.mycookbook.Model;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    private Model() {
        String separator = System.getProperty("line.separator");

        for(int i=0;i<100;i++) {
            Recipe recipe = new Recipe();
            recipe.id = "" + i;
            recipe.name = "Pizza " + i;
            recipe.ingredients = "1. 1 x pizza" + separator + "2. 1 x oven";
            recipe.instructions = "1. Pre-heat oven to 220 degrees" + separator + "2. put pizza in oven" + separator + "3. wait 10minutes or until cheese starts to bubble" +
                    separator + "4. take pizza out of the oven" + separator + "5. eat";
            data.add(recipe);
        }
    }

    List<Recipe> data = new LinkedList<Recipe>();

    public List<Recipe> getAllStudents() {
        return data;
    }
}

