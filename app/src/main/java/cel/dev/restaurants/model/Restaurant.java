package cel.dev.restaurants.model;

import android.widget.ImageView;

import cel.dev.restaurants.persistance.RestaurantDAO;

/** This class represents a Restaurant in this application
 *  There is two different types of restaurants, one with a custom image (taken by the user)
 *  and one with a placeholder image
 *
 *  This abstract class is used since how these two different types of restaurants will
 *  interact with an ImageView is different, the custom image will be provided as a byte array
 *  while the placeholder-image will be provided as a drawable resource id
 *  The two different types of restaurants ResturantCustomImage and ResturantPlaceHolderImage
 *  implements this method which allows for retrieval of the image from the database when the view needs it
 * */
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

    /** Returns true if the id of the restaurant id is set to NOT_SAVED_ID
     * */
    public static boolean restaurantHasIdSet(Restaurant restaurant) {
        return restaurant.getId() != NOT_SAVED_ID;
    }
}
