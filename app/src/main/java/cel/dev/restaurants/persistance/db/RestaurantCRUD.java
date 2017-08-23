package cel.dev.restaurants.persistance.db;

import android.support.annotation.Nullable;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;

/** This interface contains a contract which includes all functions that the
 *  class interfacing with the database should implement
 * */
public interface RestaurantCRUD {

    /** This method is used to save or update a restaurant
     *  Will return true if successful
     * */
    boolean saveOrUpdateRestaurant(Restaurant restaurant);

    /** This method will return a restaurant with the id passed as a parameter
     *  Will return null if no restaurant with this id was found
     * */
    @Nullable
    Restaurant getRestaurantById(long id);

    /** Returns a list containing all restaurants
     * */
    List<Restaurant> getAllRestaurants();

    /** Returns the image of the restaurant passed as a parameter
     *  throws an exception if the restaurant doesn't have an image
     * */
    byte[] getImageOfRestaurant(RestaurantCustomImage restaurantCustomImage) throws Exception;

    /** Stores the updated favorite state of the restaurant
     * */
    void setRestaurantFavorite(Restaurant restaurant);

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
    List<Long> getRestaurantIdsByLocation(double lat, double lon, double range);

    /** Removes the restaurant passed as a parameter
     * */
    boolean removeRestaurant(Restaurant restaurant);

    /** deletes all restaurants
     * */
    void deleteAllRestaurants();

    /** Closes the database
     * */
    void close();
}
