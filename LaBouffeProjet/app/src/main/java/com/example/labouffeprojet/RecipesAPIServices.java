package com.example.labouffeprojet;


import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipesAPIServices{
        @GET("{idSim}/similar")
        Call<JsonElement> getSimilarRecipes(@Path("idSim") int identity, @Query("apiKey") String key);

        @GET("{idInf}/information")
        Call<JsonElement> getRecipesInformation( @Path("idInf") int recipesId, @Query("apiKey") String key);

        @GET("complexSearch")
        Call<JsonElement> getRecipes(@Query("apiKey") String key, @Query("query") String recherche, @Query("number") int nb);

        @GET("{idSum}/summary")
        Call<JsonElement> getRecipesSummary(@Query("apiKey") String key, @Path("idSum") int recipesId);
}