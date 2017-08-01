package cel.dev.restaurants.Repository;

import java.util.List;

import cel.dev.restaurants.Model.BudgetType;
import cel.dev.restaurants.Model.Restaurant;

public interface RestaurantDAO {

    Restaurant getRestaurantById(int id);


    List<Restaurant> getRestaurantsByIds(List<Integer> ids);

    List<Restaurant> getRestaurantsByLocation(String longitude, String latitude, double range);

    List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType);

    boolean saveRestaurant(Restaurant restaurant);

    int getNextId();

    void updateRestaurant(Restaurant restaurant);
}
