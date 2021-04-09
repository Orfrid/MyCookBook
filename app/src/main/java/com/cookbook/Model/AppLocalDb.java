package com.cookbook.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.cookbook.MyApplication;

@Database(entities = {Recipe.class}, version = 4)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}