package com.example.labouffeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescriptionRecette extends AppCompatActivity {

    private Retrofit retrofit;
    private RecipesAPIServices servicesRecipes;
    private static final String API_BASE_URL = "https://api.spoonacular.com/recipes/";
    private int idRecipes = 0;
    private Recette r = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_recette_layout);
        setTitle("Description de la recette");

        TextView summary = (TextView) findViewById(R.id.Summary);
        TextView nom = (TextView) findViewById(R.id.size);


        idRecipes = getIntent().getExtras().getInt("id");
        Toast.makeText(DescriptionRecette.this, "" + idRecipes, Toast.LENGTH_SHORT).show();

        //retrofit constructor
        this.retrofit = new Retrofit.Builder().
                baseUrl(API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        this.servicesRecipes = retrofit.create(RecipesAPIServices.class);

        Call<JsonElement> appel = servicesRecipes.getRecipesInformation(idRecipes, pageRechercheRecette.KEY);

        appel.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){

                    JsonElement contenu = response.body();

                    JsonObject uneRecette = contenu.getAsJsonObject();

                    int id = uneRecette.get("id").getAsInt();

                    String name = uneRecette.get("title").getAsString();
                    String desc = uneRecette.get("summary").getAsString();
                    nom.setText(name);
                    summary.setText(desc);

                }else{
                    Toast.makeText(DescriptionRecette.this, "Error" + response.errorBody(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(DescriptionRecette.this, "Dommage c'est rate", Toast.LENGTH_LONG).show();
            }

        });



        Button bouton = (Button) findViewById(R.id.page_similaire);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DescriptionRecette.this, SimilarRecipes.class);
                intent.putExtra("id", idRecipes);
                startActivity(intent);
            }
        });

        Button leaveBtn = (Button) findViewById(R.id.leavebtn);
        leaveBtn.setText("Quitter");
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DescriptionRecette.this, pageRechercheRecette.class);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Credit_menu:
                Intent intent = new Intent(DescriptionRecette.this, Credit.class);
                startActivity(intent);
                break;
            case R.id.quitter_menu:
                System.exit(0);
                break;
            case R.id.Accueil_menu:
                Intent intentMenu = new Intent(DescriptionRecette.this, pageRechercheRecette.class);
                startActivity(intentMenu);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
