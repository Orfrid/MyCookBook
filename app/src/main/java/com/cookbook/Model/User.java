package com.cookbook.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String password;
    private List<Recipe> favorites;
    private List<Recipe> ownRecipes;;

    public User() {
        this.favorites = new ArrayList<Recipe>();
        this.ownRecipes = new ArrayList<Recipe>();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFavorites(List<Recipe> favorites) {
        this.favorites = favorites;
    }

    public List<Recipe> getFavorites() {
        return favorites;
    }

    public List<Recipe> getOwnRecipes() {
        return ownRecipes;
    }

    public void setOwnRecipes(List<Recipe> ownRecipes) {
        this.ownRecipes = ownRecipes;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("password", password);
        return result;
    }

    public void fromMap(Map<String, Object> map){
        name = (String)map.get("name");
        password = (String)map.get("password");
        favorites = (List<Recipe>) map.get("favorites");
        ownRecipes = (List<Recipe>) map.get("ownRecipes");
    }
}
