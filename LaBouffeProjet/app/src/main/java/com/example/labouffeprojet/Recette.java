package com.example.labouffeprojet;


import android.net.Uri;

import java.util.List;

public class Recette{

	private String image;
	private int id;
    private String name;
    private int calories;
    private String carbs;
    private String fat;
    private String protein;
	private int readyInMinutes;
	private int serving;
	private String imageType;
	private String sourceUrl;
	private String description;

    public Recette(int id, String name, int calories,String carbs,String fat,String image,String imageType,String protein){
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.image = image;
        this.protein = protein;
		this.imageType = imageType;
    }

	public Recette(int id,String name, int readyInMinutes, int servings){
		this.id = id;
		this.name = name;
		this.imageType = imageType;
		this.readyInMinutes = readyInMinutes;
		this.serving = servings;
		this.sourceUrl = sourceUrl;
    }

    public Recette(int id, String name, String desc)
	{
		this.id = id;
		this.description = desc;
	}

	public String getDescription(){return  this.description;}

    public int getReadyInMinutes(){
    	return this.readyInMinutes;
	}

	public int getServing() {
		return serving;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public String getCarbs() {
		return carbs;
	}

	public void setCarbs(String carbs) {
		this.carbs = carbs;
	}

	public String getFat() {
		return fat;
	}

	public void setFat(String fat) {
		this.fat = fat;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String imageLink) {
		this.image = imageLink;
	}

	public String getProtein() {
		return protein;
	}

	public void setProtein(String protein) {
		this.protein = protein;
	}

	@Override
	public String toString() {
		return "Recette{" +
				"image='" + image + '\'' +
				", id=" + id +
				", name='" + name + '\'' +
				", calories=" + calories +
				", carbs='" + carbs + '\'' +
				", fat='" + fat + '\'' +
				", protein='" + protein + '\'' +
				", readyInMinutes=" + readyInMinutes +
				", serving=" + serving +
				", imageType='" + imageType + '\'' +
				", sourceUrl='" + sourceUrl + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}

