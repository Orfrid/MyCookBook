package com.cookbook.Model;

import com.cookbook.MyApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
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
    LiveData<List<Recipe>> userFavoritesList;

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

    public LiveData<List<Recipe>> getUserFavorites() {
        userFavoritesList = modelSql.getUserFavorites();
        return userFavoritesList;
    }

    public interface GetAllRecipesListener{
        void onComplete();
    }

    int completedTasks;

    public void refreshAllRecipes(final GetAllRecipesListener listener) {
        //1. get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);
        completedTasks = 0;
        //2. get all updated record from firebase from the last update date
        modelFirebase.getAllRecipes(lastUpdated, new ModelFireBase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> result) {
                //3. insert the new updates to the local db
                long lastU = 0;
                for (Recipe r : result) {
                    modelSql.addRecipe(r, null);
                    if (r.getLastUpdated() > lastU) {
                        lastU = r.getLastUpdated();
                    }
                }
                //4. update the last updated time
                sp.edit().putLong("lastUpdated", lastU).apply();

                //5. return the updates data to the listeners
                completedTasks++;
                if (listener != null && completedTasks == 2) {
                    listener.onComplete();
                }
            }
        });

        modelFirebase.getAllRecipes(0, new ModelFireBase.GetAllRecipesListener() {
            @Override
            public void onComplete(List<Recipe> list) {
                List<String> names = new ArrayList<String>();
                for (Recipe r : list) {
                    names.add(r.getName());
                    modelSql.cleanDeletedRecipes(names, new DeleteListener() {
                        @Override
                        public void onComplete() {
                            completedTasks++;
                            if (listener != null && completedTasks == 2) {
                                listener.onComplete();
                            }
                        }
                    });
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

    public void deleteRecipe(final Recipe recipe, final AddRecipeListener listener) {
        modelSql.deleteRecipe(recipe, new DeleteListener() {
            @Override
            public void onComplete() {
                modelFirebase.deleteRecipe(recipe, new DeleteListener() {
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

    public void editUser(final User user, final AddUserListener listener) {
        modelFirebase.editUser(user, new AddUserListener() {
            @Override
            public void onComplete() {
                listener.onComplete();
            }
        });
    }

    interface DeleteListener extends AddRecipeListener { }

    public interface UploadImageListener extends Listener<String>{ }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }
}

