package cel.dev.restaurants.showrestaurants.restaurantsrecycleview;

import cel.dev.restaurants.model.Restaurant;

public interface RecycleViewOnExpandChangeCallback {

    void onExpandChange(boolean expanded, int position);

    void onDeleteRestaurant(int adapterPos);
}
