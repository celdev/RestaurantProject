package cel.dev.restaurants.persistanceimpl;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.persistanceimpl.db.RestaurantDB;
import cel.dev.restaurants.view.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.persistance.db.RestaurantCRUD;
import cel.dev.restaurants.utils.CollectionUtils;
import cel.dev.restaurants.utils.PictureUtils;

/** Implementation of the RestaurantDAO
 * */
public class RestaurantDAOImpl implements RestaurantDAO {

    public static final String TAG = "restaurantdao";
    private RestaurantCRUD restaurantCRUD;

    /** Creates an implementation of the RestaurantCrud interface which will communicate with the database
     * */
    public RestaurantDAOImpl(Context context) {
        restaurantCRUD = new RestaurantDB(context);
    }

    /** Returns a restaurant by the id provided
     *  will return null if a restaurant with this id doesn't exist
     * */
    @Nullable
    @Override
    public Restaurant getRestaurantById(long id) {
        return restaurantCRUD.getRestaurantById(id);
    }

    /** Returns a list of all restaurants
     * */
    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantCRUD.getAllRestaurants();
    }

    /** Tries to save or update the restaurant passed as a parameter
     *  returns true of successful
     * */
    @Override
    public boolean saveRestaurant(Restaurant restaurant) {
        return restaurantCRUD.saveOrUpdateRestaurant(restaurant);
    }

    /** Removes a restaurant
     * */
    @Override
    public boolean removeRestaurant(Restaurant restaurant) {
        return restaurantCRUD.removeRestaurant(restaurant);
    }

    /** Retrieves the image of the restaurant and sets the image of the ImageView to that image
     * */
    @Override
    public void injectImageOntoImageView(ImageView imageView, Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            try {
                byte[] image = restaurantCRUD.getImageOfRestaurant((RestaurantCustomImage) restaurant);
                imageView.setImageBitmap(PictureUtils.byteArrayToBitMap(image));
            } catch (Exception e) {
                Log.e(TAG, "injectImageOntoImageView: ", e);
            }
        }
    }

    /** stores the updated favorite state of the restaurant
     * */
    @Override
    public void setRestaurantFavorite(Restaurant restaurant) {
        restaurantCRUD.setRestaurantFavorite(restaurant);
    }

    /** Returns a List of restaurants within parameter range of
     *  lat and lon
     *
     *  The range will be used to find all restaurants within a
     *  box with the center at lat and lon
     *  with the side length/width of 2 * range
     *   _____<-- range
     *  |     |
     *  ##############
     *  #            #
     *  #     lat    #
     *  #     lon    #
     *  #            #
     *  ##############
     *  Every restaurant within this area will be returned
     * */
    @Override
    public List<Restaurant> getRestaurantsByLocation(double lat, double lon, double range) {
        List<Long> restaurantIdsByLocation = restaurantCRUD.getRestaurantIdsByLocation(lat, lon, range);
        List<Restaurant> restaurants = new ArrayList<>();
        for (long id : restaurantIdsByLocation) {
            restaurants.add(restaurantCRUD.getRestaurantById(id));
        }
        return restaurants;
    }


    /** Returns a Restaurant (or null if no restaurants were found)
     *  using the information in the randomise settings
     *
     *  depending on if the location information of the randomise settings is set
     *  then the restaurants which will be shown till be restaurants close to the user
     *  else all restaurants is used
     *  The values in randomise settings determine which (if any)
     *  budget types, kitchen types or restaurant ids which should be filtered out
     *
     * */
    @Nullable
    @Override
    public Restaurant getRandomRestaurant(RandomiseSettings randomiseSettings) {
        List<Restaurant> restaurants;
        if (randomiseSettings.isUseLocation()) {
            restaurants = getRestaurantsByLocation(randomiseSettings.getLatitude(), randomiseSettings.getLongitude(), randomiseSettings.getRange());
        } else {
            restaurants = getAllRestaurants();
        }
        filterById(restaurants, randomiseSettings.getNotTheseRestaurantsById());
        filterByBudgetTypes(restaurants, randomiseSettings.getBudgetTypes());
        filterByKitchenTypes(restaurants, randomiseSettings.getKitchenTypes());
        if (restaurants.isEmpty()) {
            return null;
        }
        return CollectionUtils.getRandomEntryIn(restaurants);
    }

   /**  removes all restaurants from the list which has an id which
    *   exists in the ids Set
    * */
    private void filterById(List<Restaurant> toFilter, Set<Long> ids) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if (ids.contains(restaurant.getId())) {
                iterator.remove();
            }
        }
    }

    /** Deletes all restaurants
     * */
    @Override
    public void deleteAllRestaurants() {
        restaurantCRUD.deleteAllRestaurants();
    }

    /** Closes the database
     * */
    @Override
    public void closeDB() {
        restaurantCRUD.close();
    }

    /** removes all restaurants from the list which's budgettypes are too expensive (according
     *  to the settings)
     * */
    private void filterByBudgetTypes(List<Restaurant> toFilter, Set<BudgetType> budgetTypes) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if (shouldFilterRestaurantByBudgetType(restaurant, budgetTypes)) {
                iterator.remove();
            }
        }
    }

    /** removes all restaurants from the lsit which's food types the user doesn't want
     * */
    private void filterByKitchenTypes(List<Restaurant> toFilter, Set<KitchenType> kitchenTypes) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if (shouldFilterRestaurantByKitchenType(restaurant, kitchenTypes)) {
                iterator.remove();
            }
        }
    }

    /** returns false if the restaurant contains a budget type which isn't being filtered
     * */
    private boolean shouldFilterRestaurantByBudgetType(Restaurant restaurant, Set<BudgetType> budgetTypes) {
        for (BudgetType budgetType : restaurant.getBudgetTypes()) {
            if (!budgetTypes.contains(budgetType)) {
                return false;
            }
        }
        return true;
    }

    /** returns false if the restaurant contains a kitchen type which isn't being filtered
     * */
    private boolean shouldFilterRestaurantByKitchenType(Restaurant restaurant, Set<KitchenType> kitchenTypes) {
        for (KitchenType kitchenType : restaurant.getKitchenTypes()) {
            if (!kitchenTypes.contains(kitchenType)) {
                return false;
            }
        }
        return true;
    }


}
