package com.cookbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.cookbook.Model.CurrentUser;
import com.cookbook.Model.Model;
import com.cookbook.Model.ModelSql;
import com.cookbook.Model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;


public class profile extends Fragment {
    ListView list;
    MyRecipeListViewModel viewModel;
    ProgressBar progBar;
    MyAdapter adapter;
    SwipeRefreshLayout sref;
    Button logOutBtn;
    View v;
    public profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(this).get(MyRecipeListViewModel.class);

        list = v.findViewById(R.id.myrecipesslist_list);

        progBar = v.findViewById(R.id.myrecipeslist_progress);
        progBar.setVisibility(View.INVISIBLE);

        sref = v.findViewById(R.id.myrecipeslist_swipe);

        logOutBtn = v.findViewById(R.id.logout_btn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                NavController nav = Navigation.findNavController(v);
                NavDirections a = profileDirections.actionNavigationProfileToLogin();
                nav.navigate(a);
            }
        });

        sref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sref.setRefreshing(true);
                reloadData();
            }
        });

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe clickedRecipe = viewModel.getList().getValue().get(i);
                clickedRecipe.getImageUrl();
                HomeFragmentDirections.ActionNavigationHomeToRecipeDetails action = HomeFragmentDirections.actionNavigationHomeToRecipeDetails(clickedRecipe.getName(),
                        clickedRecipe.getIngredients(),
                        clickedRecipe.getInstructions(),
                        clickedRecipe.getImageUrl());

                Navigation.findNavController(v).navigate(action);
            }
        });

        viewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(CurrentUser.instance.getUser() == null || CurrentUser.instance.getUser().getName() == null) {
            NavController nav = Navigation.findNavController(v);
            NavDirections a = profileDirections.actionNavigationProfileToLogin();
            nav.navigate(a);
        }
    }

    void reloadData() {
        progBar.setVisibility(View.VISIBLE);

        Model.instance.refreshAllRecipes(new Model.GetAllRecipesListener() {
            @Override
            public void onComplete() {
                progBar.setVisibility(View.INVISIBLE);
                sref.setRefreshing(false);
            }
        });
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if (viewModel.getList().getValue() == null){
                return 0;
            }
            return viewModel.getList().getValue().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.list_row, null);
            }
            TextView name = view.findViewById(R.id.list_row_name);
            TextView desc = view.findViewById(R.id.recipe_description_tv);
            ImageView imv = view.findViewById(R.id.list_row_image);
            Recipe rp = viewModel.getList().getValue().get(i);
            name.setText(rp.getName());
            desc.setText(rp.getIngredients());
            imv.setImageResource(R.drawable.placeholder);

            if (rp.getImageUrl() != null) {
                Picasso.get().load(rp.getImageUrl()).placeholder(R.drawable.placeholder).into(imv);
            }
            return view;
        }
    }

    private void logout() {
        CurrentUser.instance.getUser().setName(null);
    }
}