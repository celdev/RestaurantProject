package cel.dev.restaurants.persistance;

import android.widget.ImageView;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;

/** This interface contains the contract which the RestaurantDAO should implement
 *
 *  The architecture of my application could be simplified as
 *
 *  View asks Presenter
 *  Presenter asks DAO
 *  DAO asks RestaurantCRUD
 * */
public interface RestaurantDAO {

    Restaurant getRestaurantById(long id);


    List<Restaurant> getAllRestaurants();

    List<Restaurant> getRestaurantsByLocation(double lat, double lon, double range);

    boolean saveRestaurant(Restaurant restaurant);

    void injectImageOntoImageView(ImageView imageView, Restaurant restaurant);

    void setRestaurantFavorite(Restaurant restaurant);

    boolean removeRestaurant(Restaurant restaurant);

    Restaurant getRandomRestaurant(RandomiseSettings randomiseSettings);

    void deleteAllRestaurants();

    void closeDB();
}
