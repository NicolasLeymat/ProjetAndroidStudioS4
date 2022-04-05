package com.example.labouffeprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.labouffeprojet.R;
import com.example.labouffeprojet.Recette;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends ArrayAdapter<Recette> {
    private ArrayList<Recette> recipesList;
    private Context mContext;
    private int mRessources;
    private static LayoutInflater inflater = null;

    public RecipesAdapter(Context context, int ressources, ArrayList<Recette> list){
        super(context,ressources,list);
        this.mContext = context;
        this.mRessources = ressources;
        this.recipesList = list;
        this.inflater =LayoutInflater.from(mContext);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        String name = getItem(position).getName();
        String calories = getItem(position).getCalories() + " Calories";
        String carbs = getItem(position).getCarbs();
        String fat = getItem(position).getFat();
        String protein = getItem(position).getProtein();

        convertView = inflater.inflate(mRessources,null);

        TextView nameText = (TextView) convertView.findViewById(R.id.Nom_Recette_item);
        TextView caloriesText = (TextView) convertView.findViewById(R.id.calories_item);
        TextView carbsText = (TextView) convertView.findViewById(R.id.glucide_item);
        TextView fatText = (TextView) convertView.findViewById(R.id.gras_item);
        TextView proteinText = (TextView) convertView.findViewById(R.id.proteine_item);
        ImageView recipesImage = (ImageView) convertView.findViewById(R.id.photo_item);

        nameText.setText(name);
        caloriesText.setText(calories);
        carbsText.setText(carbs);
        fatText.setText(fat);
        proteinText.setText(protein);
        
        return convertView;
    }
}