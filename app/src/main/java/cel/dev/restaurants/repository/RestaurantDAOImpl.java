package cel.dev.restaurants.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.repository.db.RestaurantCRUD;
import cel.dev.restaurants.repository.db.RestaurantDB;
import cel.dev.restaurants.repository.db.RestaurantDBHelper;
import cel.dev.restaurants.repository.db.RestaurantDbSchema;
import cel.dev.restaurants.utils.CollectionUtils;
import cel.dev.restaurants.utils.PictureUtils;

public class RestaurantDAOImpl implements RestaurantDAO {

    public static final String TAG = "restaurantdao";
    private SQLiteDatabase db;
    private RestaurantCRUD restaurantCRUD;

    public RestaurantDAOImpl(Context context) {
        db = new RestaurantDBHelper(context).getWritableDatabase();
        restaurantCRUD = new RestaurantDB(context);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        return restaurantCRUD.getRestaurantById(id);
    }

    @Override
    public List<Restaurant> getRestaurantsByIds(List<Long> ids) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (Long id : ids) {
            restaurants.add(getRestaurantById(id));
        }
        return restaurants;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantCRUD.getAllRestaurants();
    }

    @Override
    public List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType) {
        return null;
    }

    @Override
    public boolean saveRestaurant(Restaurant restaurant) {
        return restaurantCRUD.saveOrUpdateRestaurant(restaurant);
    }

    @Override
    public boolean removeRestaurant(Restaurant restaurant) {
        return restaurantCRUD.removeRestaurant(restaurant);
    }

    @Override
    public void injectImageOntoImageView(ImageView imageView, Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            try {
                byte[] image = restaurantCRUD.getImageOfRestaurant((RestaurantCustomImage) restaurant);
                Log.d(TAG, "injectImageOntoImageView: image size = " + image.length);
                imageView.setImageBitmap(PictureUtils.byteArrayToBitMap(image));
            } catch (Exception e) {
                Log.e(TAG, "injectImageOntoImageView: ", e);
            }
        }
    }

    @Override
    public void setRestaurantFavorite(Restaurant restaurant) {
        restaurantCRUD.setRestaurantFavorite(restaurant);
    }

    @Override
    public List<Restaurant> getRestaurantsByLocation(double lat, double lon, double range) {
        List<Long> restaurantIdsByLocation = restaurantCRUD.getRestaurantIdsByLocation(lat, lon, range);
        List<Restaurant> restaurants = new ArrayList<>();
        for (long id : restaurantIdsByLocation) {
            restaurants.add(restaurantCRUD.getRestaurantById(id));
        }
        return restaurants;
    }

    @Nullable
    @Override
    public Restaurant getRandomRestaurant(RandomiseSettings randomiseSettings) {
        List<Restaurant> restaurants;
        if (randomiseSettings.isUseLocation()) {
            Log.d(TAG, "getRandomRestaurant: using location = " + true);
            restaurants = getRestaurantsByLocation(randomiseSettings.getLatitude(), randomiseSettings.getLongitude(), randomiseSettings.getRange());
        } else {
            restaurants = getAllRestaurants();
        }
        logSize("start", restaurants);
        restaurants = filterById(restaurants, randomiseSettings.getNotTheseRestaurantsById());
        logSize("by id", restaurants);
        restaurants = filterByBudgetTypes(restaurants, randomiseSettings.getBudgetTypes());
        logSize("by budget", restaurants);
        restaurants = filterByKitchenTypes(restaurants, randomiseSettings.getKitchenTypes());
        logSize("by kitchentype", restaurants);
        if (restaurants.isEmpty()) {
            return null;
        }
        return CollectionUtils.getRandomEntryIn(restaurants);
    }

    private void logSize(String filter, List col) {
        Log.d(TAG, "logSize: size of collection = after " + filter + " " + col.size());
    }

    private List<Restaurant> filterById(List<Restaurant> toFilter, Set<Long> ids) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if (ids.contains(restaurant.getId())) {
                Log.d(TAG, "filterById: restaurantid exist in ids to filter" + restaurant.getId() + "\n exist in " + Arrays.toString(ids.toArray()));
                iterator.remove();
            }
        }
        return toFilter;
    }

    @Override
    public void deleteAllRestaurants() {
        db.delete(RestaurantDbSchema.Table.NAME, null, null);
    }

    private List<Restaurant> filterByBudgetTypes(List<Restaurant> toFilter, Set<BudgetType> budgetTypes) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if (shouldFilterRestaurantByBudgetType(restaurant, budgetTypes)) {
                iterator.remove();
            }
        }
        return toFilter;
    }

    private List<Restaurant> filterByKitchenTypes(List<Restaurant> toFilter, Set<KitchenType> kitchenTypes) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            Restaurant restaurant = iterator.next();
            if (shouldFilterRestaurantByKitchenType(restaurant, kitchenTypes)) {
                iterator.remove();
            }
        }
        return toFilter;
    }

    private boolean shouldFilterRestaurantByBudgetType(Restaurant restaurant, Set<BudgetType> budgetTypes) {
        for (BudgetType budgetType : restaurant.getBudgetTypes()) {
            if (!budgetTypes.contains(budgetType)) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldFilterRestaurantByKitchenType(Restaurant restaurant, Set<KitchenType> kitchenTypes) {
        for (KitchenType kitchenType : restaurant.getKitchenTypes()) {
            if (!kitchenTypes.contains(kitchenType)) {
                return false;
            }
        }
        return true;
    }


    private List<Restaurant> filterByFavorite(List<Restaurant> toFilter) {
        Iterator<Restaurant> iterator = toFilter.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isFavorite()) {
                iterator.remove();
            }
        }
        return toFilter;
    }
}
