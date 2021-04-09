package com.cookbook.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelSql {
    public LiveData<List<Recipe>> getAllRecipes(){
        return AppLocalDb.db.recipeDao().getAllRecipes();
    }

    public interface GetAllRecipesListener{
        void onComplete(LiveData<List<Recipe>> data);
    }

    public void addRecipe(final Recipe recipe, final Model.AddRecipeListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.recipeDao().insertAll(recipe);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}