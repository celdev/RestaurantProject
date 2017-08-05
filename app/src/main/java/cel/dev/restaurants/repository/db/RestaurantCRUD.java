package cel.dev.restaurants.repository.db;

import android.support.annotation.Nullable;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;

public interface RestaurantCRUD {
    boolean saveOrUpdateRestaurant(Restaurant restaurant);

    @Nullable
    Restaurant getRestaurantById(long id);

    List<Restaurant> getAllRestaurants();
}
