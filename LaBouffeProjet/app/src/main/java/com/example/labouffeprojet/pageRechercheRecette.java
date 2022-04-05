package com.example.labouffeprojet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class pageRechercheRecette extends AppCompatActivity {

    private Retrofit retrofit;
    private RecipesAPIServices servicesRecipes;
    private int INTERNET_PERMISSIONS_CODE = 0;
    protected static final String KEY = "5e56a965bdbc49ecbaf5a0ebdde03666";
    private static final String API_BASE_URL = "https://api.spoonacular.com/recipes/";
    private ListView recipesList;
    private ArrayList<Recette> recettes = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_recherche_recette);
        //Check si on est connecter
        if (isNetworkAvailable()){
            Toast.makeText(pageRechercheRecette.this, "Connected", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(pageRechercheRecette.this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
        //verifier si on à les permissions Internet
        requestInternetsPermissions();


        setTitle("Recherche de recette");
        TextInputEditText input = (TextInputEditText) findViewById(R.id.search);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.nbRecette);
        recipesList = (ListView) findViewById(R.id.listRecherche);
        Button searchBtn = (Button) findViewById(R.id.rechercher);
        RecipesAdapter recipesAdapter = new RecipesAdapter(this, R.layout.item_recette, recettes);
        recipesList.setAdapter(recipesAdapter);





        this.retrofit = new Retrofit.Builder().
                baseUrl(pageRechercheRecette.API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        this.servicesRecipes = retrofit.create(RecipesAPIServices.class);




        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recette r = (Recette) parent.getItemAtPosition(position);
                int idRecipesPrecis = (int) r.getId();
                Intent intent = new Intent(pageRechercheRecette.this, DescriptionRecette.class);
                intent.putExtra("id", idRecipesPrecis);
                startActivity(intent);
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recettes.isEmpty()){
                    recettes = new ArrayList<>();
                    recipesAdapter.notifyDataSetChanged();
                }
                String recherche = input.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(radioId);
                int nbrecherche = 50;
                if (radioButton != null){
                    nbrecherche = Integer.parseInt(radioButton.getText().toString());
                }
                search(recipesAdapter,recherche, nbrecherche);

            }
        });


        Button leaveBtn = (Button) findViewById(R.id.button2);
        leaveBtn.setText("Quitter");
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void requestInternetsPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.INTERNET)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(pageRechercheRecette.this,
                                    new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSIONS_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(pageRechercheRecette.this,
                    new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTERNET_PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isNetworkAvailable(){
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return  networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException e){
            return false;
        }
    }

    public ArrayList<Recette> search(RecipesAdapter sm,String search, int nb){
        Call<JsonElement> appel = servicesRecipes.getRecipes(pageRechercheRecette.KEY,search,nb);

        appel.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    JsonElement contenu = response.body();

                    JsonObject jsonGlobal = contenu.getAsJsonObject();
                    int count = jsonGlobal.get("totalResults").getAsInt();
                    Toast.makeText(pageRechercheRecette.this, count + " recettes trouvé", Toast.LENGTH_LONG).show();

                    JsonArray listDeRecettes = jsonGlobal.getAsJsonArray("results");
                    for (int i = 0; i < listDeRecettes.size(); i++){
                        Recette r = null;
                        JsonObject uneRecette = listDeRecettes.get(i).getAsJsonObject();
                        int id = uneRecette.get("id").getAsInt();
                        String name = uneRecette.get("title").getAsString();
                        String image = uneRecette.get("image").getAsString();
                        String imageType = uneRecette.get("imageType").getAsString();

                        r = new Recette(id, name, 0, "carbs", "fat", image, imageType, "protein");
                        recettes.add(r);
                    }
                }else{
                    Toast.makeText(pageRechercheRecette.this, "Error : " + response.errorBody(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(pageRechercheRecette.this, "T'ES UNE MERDE", Toast.LENGTH_LONG).show();
            }
        });
        sm.notifyDataSetChanged();
        return recettes;
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
                Intent intent = new Intent(pageRechercheRecette.this, Credit.class);
                startActivity(intent);
                break;
            case R.id.quitter_menu:
                System.exit(0);
                break;
            case R.id.Accueil_menu:
                Intent intentMenu = new Intent(pageRechercheRecette.this, pageRechercheRecette.class);
                startActivity(intentMenu);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
