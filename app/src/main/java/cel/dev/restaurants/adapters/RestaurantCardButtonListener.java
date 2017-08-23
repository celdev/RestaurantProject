package cel.dev.restaurants.adapters;

import android.view.View;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;

/** This class contains an abstract class (RestaurantCardButtonListener) and a
 *  couple of implementation of this class.
 *  These listeners allows for the buttons in each card to call back to the adapter
 *  in part to allow for avoiding to pass DAO-objects into each ViewHolder but also
 *  in order to update the RecycleView after a change (e.g. when a restaurant has been deleted)
 * */
abstract class RestaurantCardButtonListener implements View.OnClickListener {
    RestaurantViewHolder restaurantViewHolder;
    RestaurantCardButtonListener(RestaurantViewHolder restaurantViewHolder) {
        this.restaurantViewHolder = restaurantViewHolder;
    }


    /** This listeners expands/collapses the CardView in order to show/hide some information
     *  in the card
     * */
    static class OnOpenPressedListener extends RestaurantCardButtonListener {
        private final RecycleViewOnExpandChangeCallback callback;
        private final int position;
        private final long restaurantId;

        public OnOpenPressedListener(RestaurantViewHolder restaurantViewHolder, RecycleViewOnExpandChangeCallback callback, int position, long restaurantId) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.position = position;
            this.restaurantId = restaurantId;
        }

        @Override
        public void onClick(View v) {
            callback.onExpandChange(restaurantViewHolder.toggleExpand(), position, restaurantId);
        }
    }

    /** This listeners will toggle the favorite status of the restaurant
     *  when the favorite button is pressed in the CardView
     * */
    static class OnFavoritePressedListener extends RestaurantCardButtonListener {

        private Restaurant restaurant;
        private final RestaurantDAO restaurantDAO;

        OnFavoritePressedListener(RestaurantViewHolder restaurantViewHolder, Restaurant restaurant, RestaurantDAO restaurantDAO) {
            super(restaurantViewHolder);
            this.restaurant = restaurant;
            this.restaurantDAO = restaurantDAO;
        }

        @Override
        public void onClick(View v) {
            boolean favorite = restaurant.isFavorite();
            restaurant.setFavorite(!favorite);
            restaurantViewHolder.setFavorite(restaurant.isFavorite());
            restaurantDAO.setRestaurantFavorite(restaurant);
        }
    }

    /** This Listener will call the callback in the adapter
     *  which will show a dialog asking if the user really wants to delete this restaurant
     * */
    static class OnDeleteRestaurantListener extends RestaurantCardButtonListener {

        private final RecycleViewOnExpandChangeCallback callback;

        OnDeleteRestaurantListener(RestaurantViewHolder restaurantViewHolder, RecycleViewOnExpandChangeCallback callback) {
            super(restaurantViewHolder);
            this.callback = callback;
        }

        @Override
        public void onClick(View v) {
            callback.onDeleteRestaurant(restaurantViewHolder.getAdapterPosition());
        }
    }

    /** This listener will show the location of the restaurant
     * */
    static class OnShowRestaurantLocationListener extends RestaurantCardButtonListener {

        private final RecycleViewOnExpandChangeCallback callback;
        private final Restaurant restaurant;

        OnShowRestaurantLocationListener(RestaurantViewHolder restaurantViewHolder, RecycleViewOnExpandChangeCallback callback, Restaurant restaurant) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.restaurant = restaurant;
        }

        @Override
        public void onClick(View v) {
            callback.onShowRestaurantLocation(restaurant);
        }
    }

    /** This listener will open the activity in order to allow the user to edit this restaurant
     * */
    static class OnEditRestaurantListener extends RestaurantCardButtonListener {

        private final RecycleViewOnExpandChangeCallback callback;
        private final Restaurant restaurant;

        OnEditRestaurantListener(RestaurantViewHolder restaurantViewHolder, RecycleViewOnExpandChangeCallback callback, Restaurant restaurant) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.restaurant = restaurant;
        }

        @Override
        public void onClick(View v) {
            callback.onEditRestaurant(restaurant);
        }
    }

}
