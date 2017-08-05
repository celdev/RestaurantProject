package cel.dev.restaurants.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;
import cel.dev.restaurants.repository.db.RestaurantCRUD;
import cel.dev.restaurants.repository.db.RestaurantDB;
import cel.dev.restaurants.repository.db.RestaurantDBHelper;

public class RestaurantDAOImpl implements RestaurantDAO {

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
    public List<Restaurant> getRestaurantsByLocation(String longitude, String latitude, double range) {
        return null;
    }

    @Override
    public List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType) {
        return null;
    }

    @Override
    public boolean saveRestaurant(Restaurant restaurant) {
        return restaurantCRUD.saveOrUpdateRestaurant(restaurant);
    }

}
