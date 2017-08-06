package cel.dev.restaurants.repository.db;

import android.support.annotation.Nullable;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;

public interface RestaurantCRUD {
    boolean saveOrUpdateRestaurant(Restaurant restaurant);

    @Nullable
    Restaurant getRestaurantById(long id);

    List<Restaurant> getAllRestaurants();

    byte[] getImageOfRestaurant(RestaurantCustomImage restaurantCustomImage) throws Exception;

    void setRestaurantFavorite(Restaurant restaurant);

    List<Long> getRestaurantIdsByLocation(double lat, double lon, double range);

}
