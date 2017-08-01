package cel.dev.restaurants.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public abstract class Restaurant {

    public static final int NOT_SAVED_ID = -1;

    private int id;
    private String name;
    private float rating;
    private BudgetType[] budgetTypes;
    private double latitude, longitude;
    private FoodType[] foodTypes;

    public Restaurant(String name, float rating, BudgetType[] budgetTypes, double lat, double lon, FoodType[] foodTypes) {
        this.id = -1;
        this.name = name;
        this.rating = rating;
        this.budgetTypes = budgetTypes;
        this.foodTypes = foodTypes;
        this.latitude = lat;
        this.longitude = lon;
    }

    public abstract void injectImageOntoImageView(ImageView imageView);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public BudgetType[] getBudgetTypes() {
        return budgetTypes;
    }

    public void setBudgetTypes(BudgetType[] budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public FoodType[] getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(FoodType[] foodTypes) {
        this.foodTypes = foodTypes;
    }
}
