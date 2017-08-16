package cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview;

import cel.dev.restaurants.model.Restaurant;

/** This interface contains a contract for the RecycleView-Adapter
 *  which allows the items in the RecycleView to callback into the adapter
 *  for some methods (such as delete and edit the selected restaurant)
 * */
public interface RecycleViewOnExpandChangeCallback {

    void onExpandChange(boolean expanded, int position);

    void onDeleteRestaurant(int adapterPos);

    void onEditRestaurant(Restaurant restaurant);

    void onShowRestaurantLocation(Restaurant restaurant);
}
