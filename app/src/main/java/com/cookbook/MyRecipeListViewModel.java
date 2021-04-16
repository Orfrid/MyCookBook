package com.cookbook;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cookbook.Model.ModelFireBase;
import com.cookbook.Model.Recipe;
import com.cookbook.Model.Model;

import java.util.List;

public class MyRecipeListViewModel extends ViewModel {
    private LiveData<List<Recipe>> rpList;

    public MyRecipeListViewModel() {
        rpList = Model.instance.getCurrentUserRecipes();
    }
    public void setRpList(LiveData<List<Recipe>> rpList) {
        this.rpList = rpList;
    }

    LiveData<List<Recipe>> getList(){
        return rpList;
    }
}
