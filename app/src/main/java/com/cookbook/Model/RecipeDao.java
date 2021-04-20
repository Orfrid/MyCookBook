package com.cookbook.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("select * from Recipe")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("select * from Recipe where user =:userName")
    LiveData<List<Recipe>> getCurrentUserRecipes(String userName);

    @Query("select * from Recipe where name IN (:names)")
    LiveData<List<Recipe>> getCurrentUserFavorites(List<String> names);

    @Query("delete from Recipe where name not in (:names)")
    void removeDeletedRecipes(List<String> names);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Recipe... recipes);

    @Delete
    void delete(Recipe recipe);
}
