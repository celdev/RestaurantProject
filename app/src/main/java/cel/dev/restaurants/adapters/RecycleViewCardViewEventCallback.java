package cel.dev.restaurants.adapters;

import cel.dev.restaurants.model.Restaurant;

/** This interface contains a contract for the RecycleView-Adapter
 *  which allows the items in the RecycleView to callback into the adapter
 *  for some methods (such as delete and edit the selected restaurant)
 * */
public interface RecycleViewCardViewEventCallback {

    /** This is a callback for when the restaurant information is expanded or collapsed
     *  This allows the callback to save which restaurants information is expanded and
     *  use this to re-expand them after an orientation change.
     * */
    void onExpandChange(boolean expanded, int position, long restaurantId);

    /** This is a callback for when a restaurant is to be deleted from the RecycleViewAdapter and
     *  from the database.
     * */
    void onDeleteRestaurant(int adapterPos);

    /** This is a callback for when a restaurant is to be edited
     * */
    void onEditRestaurant(Restaurant restaurant);

    /** This is a callback for when the location of a restaurant is to be shown in the
     *  ShowRestaurantLocationActivity
     * */
    void onShowRestaurantLocation(Restaurant restaurant);
}
