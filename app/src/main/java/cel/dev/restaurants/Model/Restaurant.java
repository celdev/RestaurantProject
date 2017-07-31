package cel.dev.restaurants.Model;

import android.content.Context;
import android.graphics.Bitmap;

public abstract class Restaurant {

    public static final int NOT_SAVED_ID = -1;

    private int id;
    private String name;
    private int rating;
    private BudgetType[] budgetTypes;
    private String latitude, longitude;
    private FoodType[] foodTypes;

    public Restaurant(String name, int rating, BudgetType[] budgetTypes, String latitude, String longitude, FoodType[] foodTypes) {
        this.id = -1;
        this.name = name;
        this.rating = rating;
        this.budgetTypes = budgetTypes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foodTypes = foodTypes;
    }

    public abstract Bitmap getRestaurantImage(Context context);

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public BudgetType[] getBudgetTypes() {
        return budgetTypes;
    }

    public void setBudgetTypes(BudgetType[] budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public FoodType[] getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(FoodType[] foodTypes) {
        this.foodTypes = foodTypes;
    }
}
