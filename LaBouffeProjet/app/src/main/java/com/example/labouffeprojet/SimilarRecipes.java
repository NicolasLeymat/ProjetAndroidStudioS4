package com.example.labouffeprojet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SimilarRecipes extends AppCompatActivity {

    private static final String API_BASE_URL = "https://api.spoonacular.com/recipes/";
    private Retrofit retrofit;
    private RecipesAPIServices recipesAPIServices;
    private String id = "";
    private ListView similar_Recipes_List;
    private ArrayList<Recette> recettes = new ArrayList<>();
    private int idRecipes;
    private RecipesAPIServices servicesRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.similar_recipes_layout);
        idRecipes = getIntent().getExtras().getInt("id");
        Button searchbtn = (Button) findViewById(R.id.rSm);
        setTitle("Recette similaire");
        similar_Recipes_List = (ListView) findViewById(R.id.similar_recipes_list);
        SimilarRecipesAdapter similarRecipesAdapter = new SimilarRecipesAdapter(this, R.layout.list_recipes, recettes);
        similar_Recipes_List.setAdapter(similarRecipesAdapter);

        //retrofit constructor
        this.retrofit = new Retrofit.Builder().
                baseUrl(API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        this.servicesRecipes = retrofit.create(RecipesAPIServices.class);




        similar_Recipes_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recette r = (Recette) parent.getItemAtPosition(position);
                int idRecipesPrecis = (int) r.getId();
                //Toast.makeText(SimilarRecipes.this, idRecipesPrecis, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SimilarRecipes.this, DescriptionRecette.class);
                intent.putExtra("id", idRecipesPrecis);
                startActivity(intent);
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recettes.isEmpty()){
                    recettes = new ArrayList<>();
                    similarRecipesAdapter.notifyDataSetChanged();
                }
                search(similarRecipesAdapter,idRecipes);
            }
        });

        Button leaveBtn = (Button) findViewById(R.id.leavebtn);
        leaveBtn.setText("Quitter");
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimilarRecipes.this, pageRechercheRecette.class);
                startActivity(intent);
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.monmenu, menu);
        return true;
    }

    public ArrayList<Recette> search(SimilarRecipesAdapter sm,int nb){
        Call<JsonElement> appel = servicesRecipes.getSimilarRecipes(nb, pageRechercheRecette.KEY);

        appel.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    JsonElement contenu = response.body();


                    JsonArray listDeRecettes = contenu.getAsJsonArray();
                    for (int i = 0; i < listDeRecettes.size(); i++){
                        Recette r = null;
                        JsonObject uneRecette = listDeRecettes.get(i).getAsJsonObject();
                        int id = uneRecette.get("id").getAsInt();
                        String name = uneRecette.get("title").getAsString();
                        int readyInMinutes = uneRecette.get("readyInMinutes").getAsInt();
                        int servings = uneRecette.get("servings").getAsInt();

                        r = new Recette(id, name, readyInMinutes, servings);
                        recettes.add(r);

                    }
                }else{
                    Toast.makeText(SimilarRecipes.this, "Error : " + response.errorBody(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(SimilarRecipes.this, "T'ES UNE MERDE", Toast.LENGTH_LONG).show();
            }
        });
        sm.notifyDataSetChanged();
        return recettes;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Credit_menu:
                Intent intent = new Intent(SimilarRecipes.this, Credit.class);
                startActivity(intent);
                break;
            case R.id.quitter_menu:
                System.exit(0);
                break;
            case R.id.Accueil_menu:
                Intent intentMenu = new Intent(SimilarRecipes.this, pageRechercheRecette.class);
                startActivity(intentMenu);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}