package cel.dev.restaurants.view;

import android.view.View;

import cel.dev.restaurants.adapters.RecycleViewCardViewEventCallback;
import cel.dev.restaurants.adapters.viewholders.RestaurantViewHolder;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;

/** This class contains an abstract class (RestaurantCardButtonListener) and a
 *  couple of implementation of this class.
 *  These listeners allows for the buttons in each card to call back to the adapter
 *  in part to allow for avoiding to pass DAO-objects into each ViewHolder but also
 *  in order to update the RecycleView after a change (e.g. when a restaurant has been deleted)
 * */
public abstract class RestaurantCardButtonListener implements View.OnClickListener {
    private RestaurantViewHolder restaurantViewHolder;
    private RestaurantCardButtonListener(RestaurantViewHolder restaurantViewHolder) {
        this.restaurantViewHolder = restaurantViewHolder;
    }


    /** This listeners expands/collapses the CardView in order to show/hide some information
     *  in the card
     * */
    public static class OnOpenPressedListener extends RestaurantCardButtonListener {
        private final RecycleViewCardViewEventCallback callback;
        private final int position;
        private final long restaurantId;

        public OnOpenPressedListener(RestaurantViewHolder restaurantViewHolder, RecycleViewCardViewEventCallback callback, int position, long restaurantId) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.position = position;
            this.restaurantId = restaurantId;
        }

        /** calls to the callback (the adapter) to handle on expand change
         * */
        @Override
        public void onClick(View v) {
            callback.onExpandChange(super.restaurantViewHolder.toggleExpand(), position, restaurantId);
        }
    }

    /** This listeners will toggle the favorite status of the restaurant
     *  when the favorite button is pressed in the CardView
     * */
    public static class OnFavoritePressedListener extends RestaurantCardButtonListener {

        private Restaurant restaurant;
        private final RestaurantDAO restaurantDAO;

        public OnFavoritePressedListener(RestaurantViewHolder restaurantViewHolder, Restaurant restaurant, RestaurantDAO restaurantDAO) {
            super(restaurantViewHolder);
            this.restaurant = restaurant;
            this.restaurantDAO = restaurantDAO;
        }

        /** Toggles the favorite state in the restaurant
         *  calls to the viewholder to update the icon
         *  calls to the DAO to save the new favorite state
         * */
        @Override
        public void onClick(View v) {
            boolean favorite = restaurant.isFavorite();
            restaurant.setFavorite(!favorite);
            super.restaurantViewHolder.setFavorite(restaurant.isFavorite());
            restaurantDAO.setRestaurantFavorite(restaurant);
        }
    }

    /** This Listener will call the callback in the adapter
     *  which will show a dialog asking if the user really wants to delete this restaurant
     * */
    public static class OnDeleteRestaurantListener extends RestaurantCardButtonListener {

        private final RecycleViewCardViewEventCallback callback;

        public OnDeleteRestaurantListener(RestaurantViewHolder restaurantViewHolder, RecycleViewCardViewEventCallback callback) {
            super(restaurantViewHolder);
            this.callback = callback;
        }

        /** calls to the callback (adapter) to handle the delete restaurant functionality
         * */
        @Override
        public void onClick(View v) {
            callback.onDeleteRestaurant(super.restaurantViewHolder.getAdapterPosition());
        }
    }

    /** This listener will show the location of the restaurant
     * */
    public static class OnShowRestaurantLocationListener extends RestaurantCardButtonListener {

        private final RecycleViewCardViewEventCallback callback;
        private final Restaurant restaurant;

        public OnShowRestaurantLocationListener(RestaurantViewHolder restaurantViewHolder, RecycleViewCardViewEventCallback callback, Restaurant restaurant) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.restaurant = restaurant;
        }

        /** calls to the callback (adapter) to handle show restaurant location functionality for this restaurant
         * */
        @Override
        public void onClick(View v) {
            callback.onShowRestaurantLocation(restaurant);
        }
    }

    /** This listener will open the activity in order to allow the user to edit this restaurant
     * */
    public static class OnEditRestaurantListener extends RestaurantCardButtonListener {

        private final RecycleViewCardViewEventCallback callback;
        private final Restaurant restaurant;

        public OnEditRestaurantListener(RestaurantViewHolder restaurantViewHolder, RecycleViewCardViewEventCallback callback, Restaurant restaurant) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.restaurant = restaurant;
        }

        /** calls to the callback (adapter) to handle edit restaurant functionality for this restaurant
         * */
        @Override
        public void onClick(View v) {
            callback.onEditRestaurant(restaurant);
        }
    }

}
