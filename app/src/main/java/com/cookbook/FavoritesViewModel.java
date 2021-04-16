package com.cookbook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cookbook.Model.Model;
import com.cookbook.Model.Recipe;

import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private LiveData<List<Recipe>> rpList;

    public FavoritesViewModel() {
        rpList = Model.instance.getUserFavorites();
    }
    public void setRpList(LiveData<List<Recipe>> rpList) {
        this.rpList = rpList;
    }

    LiveData<List<Recipe>> getList() {
        return rpList;
    }
}
