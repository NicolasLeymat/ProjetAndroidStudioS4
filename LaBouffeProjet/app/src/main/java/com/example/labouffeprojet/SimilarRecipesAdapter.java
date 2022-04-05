package com.example.labouffeprojet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SimilarRecipesAdapter extends ArrayAdapter<Recette> {
    private ArrayList<Recette> recipesList;
    private Context mContext;
    private int mRessources;
    private static LayoutInflater inflater = null;

    public SimilarRecipesAdapter(Context context, int ressources, ArrayList<Recette> list){
        super(context,ressources, list);
        this.mContext = context;
        this.mRessources = ressources;
        this.recipesList = list;
        this.inflater =LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        String name = getItem(position).getName();
        String serving = "Pour " + getItem(position).getServing() + " personnes";
        String prepTime = getItem(position).getReadyInMinutes() + " minutes";

        convertView = inflater.inflate(mRessources,null);


        TextView nameText = (TextView) convertView.findViewById(R.id.SimName);
        TextView servingText = (TextView) convertView.findViewById(R.id.Serving);
        TextView prepTimeText = (TextView) convertView.findViewById(R.id.readyInMin);

        nameText.setText(name);
        servingText.setText(serving);
        prepTimeText.setText(prepTime);

        return convertView;
    }
}
