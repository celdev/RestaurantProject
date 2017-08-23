package cel.dev.restaurants.persistance;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.util.List;

import cel.dev.restaurants.view.RandomiseSettings;
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

    /** Returns a restaurant by the id provided
     *  will return null if a restaurant with this id doesn't exist
     * */
    @Nullable
    Restaurant getRestaurantById(long id);

    /** Returns a list of all restaurants
     * */
    List<Restaurant> getAllRestaurants();

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
    List<Restaurant> getRestaurantsByLocation(double lat, double lon, double range);

    /** Tries to save or update the restaurant passed as a parameter
     *  returns true of successful
     * */
    boolean saveRestaurant(Restaurant restaurant);

    /** Retrieves the image of the restaurant and sets the image of the ImageView to that image
     * */
    void injectImageOntoImageView(ImageView imageView, Restaurant restaurant);

    /** stores the updated favorite state of the restaurant
     * */
    void setRestaurantFavorite(Restaurant restaurant);

    /** Removes a restaurant
     * */
    boolean removeRestaurant(Restaurant restaurant);

    /** returns a random restaurant depending on the values set in
     *  RandomiseSettings passed as a parameter
     * */
    Restaurant getRandomRestaurant(RandomiseSettings randomiseSettings);

    /** Deletes all restaurants
     * */
    void deleteAllRestaurants();

    /** Closes the database
     * */
    void closeDB();
}
