package cel.dev.restaurants.repository;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.Restaurant;

public interface RestaurantDAO {

    Restaurant getRestaurantById(int id);


    List<Restaurant> getRestaurantsByIds(List<Integer> ids);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> getRestaurantsByLocation(String longitude, String latitude, double range);

    List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType);

    boolean saveRestaurant(Restaurant restaurant);

    int getNextId();

    //void updateRestaurant(Restaurant restaurant);
}
