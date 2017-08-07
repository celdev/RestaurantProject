package cel.dev.restaurants.repository;

import android.widget.ImageView;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;

public interface RestaurantDAO {

    Restaurant getRestaurantById(long id);


    List<Restaurant> getRestaurantsByIds(List<Long> ids);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> getRestaurantsByLocation(double lat, double lon, double range);

    List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType);

    boolean saveRestaurant(Restaurant restaurant);

    void injectImageOntoImageView(ImageView imageView, Restaurant restaurant);

    void setRestaurantFavorite(Restaurant restaurant);

    boolean removeRestaurant(Restaurant restaurant);

    Restaurant getRandomRestaurant(RandomiseSettings randomiseSettings);
}
