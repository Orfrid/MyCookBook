package com.cookbook.Model;

import com.cookbook.MyApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public final static Model instance = new Model();

    ModelFireBase modelFirebase = new ModelFireBase();
    ModelSql modelSql = new ModelSql();

    private Model() {
    }

    public interface Listener<T> {
        void onComplete(T result);
    }

    LiveData<List<Recipe>> recipesList;
    LiveData<List<Recipe>> userRecipesList;

    public LiveData<List<Recipe>> getAllRecipes() {
        if (recipesList == null)
        {
            recipesList = modelSql.getAllRecipes();
            refreshAllRecipes(null);
        }
        return recipesList;
    }

    public LiveData<List<Recipe>> getCurrentUserRecipes() {
        userRecipesList = modelSql.getUserRecipes();
        return userRecipesList;
    }

    public interface GetAllRecipesListener{
        void onComplete();
    }

    public void refreshAllRecipes(final GetAllRecipesListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated",0);
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllRecipes(lastUpdated, new ModelFireBase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> result) {
                //3. insert the new updates to the local db
                long lastU = 0;
                for (Recipe r: result) {
                    modelSql.addRecipe(r,null);
                    if(r.getLastUpdated() > lastU) {
                        lastU = r.getLastUpdated();
                    }
                }
                //4. update the last updated time
                sp.edit().putLong("lastUpdated", lastU).apply();

                //5. return the updates data to the listeners
                if(listener != null){
                    listener.onComplete();
                }
            }
        });
    }

    public interface GetRecipeListener {
        void onComplete(Recipe recipe);
    }

    public interface GetUserListener {
        void onComplete(User user);
    }

    public void getRecipe(String id, GetRecipeListener listener) {
        modelFirebase.getRecipe(id, listener);
    }

    public interface AddRecipeListener {
        void onComplete();
    }

    public interface AddUserListener {
        void onComplete();
    }

    public void addRecipe(final Recipe recipe, final AddRecipeListener listener) {
        modelFirebase.addRecipe(recipe, new AddRecipeListener() {
            @Override
            public void onComplete() {
                refreshAllRecipes(new GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });
    }

    public void editRecipe(final Recipe recipe, final AddRecipeListener listener) {
        modelFirebase.editRecipe(recipe, new AddRecipeListener() {
            @Override
            public void onComplete() {
                refreshAllRecipes(new GetAllRecipesListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
            }
        });
    }

    public void addUser(final User user, final AddUserListener listener) {
        modelFirebase.addUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    public void getUser(final String name, final GetUserListener listener) {
        modelFirebase.getUser(name, new GetUserListener() {
            @Override
            public void onComplete(User user) {
                listener.onComplete(user);
            }
        });
    }

    public interface UpdateRecipeListener extends AddRecipeListener { }
    public void updateRecipe(final Recipe recipe, final UpdateRecipeListener listener) {
        modelFirebase.updateRecipe(recipe, listener);
    }

    interface DeleteListener extends AddRecipeListener { }
    public void deleteRecipe(Recipe recipe, DeleteListener listener) {
        modelFirebase.delete(recipe, listener);
    }

    public interface UploadImageListener extends Listener<String>{ }
    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }
}

