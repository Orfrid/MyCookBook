package com.example.mycookbook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycookbook.Adapters.RecipesAdapter;
import com.example.mycookbook.Model.Model;
import com.example.mycookbook.Model.Recipe;

import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView list;
    List<Recipe> data;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home2, container, false);
        list = v.findViewById(R.id.home_recepies_rv);
        list.hasFixedSize();

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), layoutManager.getOrientation());

        list.setLayoutManager(layoutManager);
        list.addItemDecoration(dividerItemDecoration);

        data = Model.instance.getAllStudents();

        RecipesAdapter adapter = new RecipesAdapter(getLayoutInflater());
        adapter.data = data;
        list.setAdapter(adapter);

        adapter.setOnClickListener(new RecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Recipe clickedRecipe = data.get(position);
                Log.d("TAG","row was clicked " + clickedRecipe.instructions);
                HomeFragmentDirections.ActionNavigationHomeToRecipeDetails action = HomeFragmentDirections.actionNavigationHomeToRecipeDetails(clickedRecipe.name,clickedRecipe.ingredients,clickedRecipe.instructions);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }

}