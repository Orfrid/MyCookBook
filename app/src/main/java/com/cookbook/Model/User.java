package com.cookbook.Model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String password;
    private Recipe[] favorites;
    private Recipe[] ownRecipes;

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

    public void setFavorites(Recipe[] favorites) {
        this.favorites = favorites;
    }

    public Recipe[] getFavorites() {
        return favorites;
    }

    public Recipe[] getOwnRecipes() {
        return ownRecipes;
    }

    public void setOwnRecipes(Recipe[] ownRecipes) {
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
    }
}
