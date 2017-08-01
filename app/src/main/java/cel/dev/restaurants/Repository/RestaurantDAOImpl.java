package cel.dev.restaurants.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cel.dev.restaurants.Model.BudgetType;
import cel.dev.restaurants.Model.Restaurant;

public class RestaurantDAOImpl implements RestaurantDAO {

    private static int id = 0;

    private static List<Restaurant> restaurants = new ArrayList<>();

    public int getNextId() {
        return id++;
    }

    @Override
    public Restaurant getRestaurantById(int id) {
        return restaurants.get(id);
    }

    @Override
    public List<Restaurant> getRestaurantsByIds(List<Integer> ids) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int id : ids) {
            restaurants.add(getRestaurantById(id));
        }
        return restaurants;
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
        restaurant.setId(getNextId());
        return restaurants.add(restaurant);
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        if (restaurant.getId() == Restaurant.NOT_SAVED_ID) {
            saveRestaurant(restaurant);
        } else {
            restaurants.set(restaurant.getId(), restaurant);
        }
    }
}
