package cel.dev.restaurants.model;

import android.widget.ImageView;

import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.db.RestaurantCRUD;

public abstract class Restaurant {

    public static final long NOT_SAVED_ID = -1;

    private long id;
    private String name;
    private float rating;
    private BudgetType[] budgetTypes;
    private double latitude, longitude;
    private KitchenType[] kitchenTypes;
    private boolean favorite;

    public Restaurant(String name, float rating, BudgetType[] budgetTypes, double lat, double lon, KitchenType[] kitchenTypes, boolean favorite) {
        this.favorite = favorite;
        this.id = -1;
        this.name = name;
        this.rating = rating;
        this.budgetTypes = budgetTypes;
        this.kitchenTypes = kitchenTypes;
        this.latitude = lat;
        this.longitude = lon;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public abstract void injectImageOntoImageView(ImageView imageView, RestaurantDAO restaurantDAO);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public KitchenType[] getKitchenTypes() {
        return kitchenTypes;
    }

    public void setKitchenTypes(KitchenType[] kitchenTypes) {
        this.kitchenTypes = kitchenTypes;
    }

    public static boolean restaurantHasIdSet(Restaurant restaurant) {
        return restaurant.getId() != NOT_SAVED_ID;
    }
}
