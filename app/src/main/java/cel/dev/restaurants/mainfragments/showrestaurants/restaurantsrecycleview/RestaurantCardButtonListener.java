package cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview;

import android.util.Log;
import android.view.View;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;

abstract class RestaurantCardButtonListener implements View.OnClickListener {
    RestaurantViewHolder restaurantViewHolder;
    RestaurantCardButtonListener(RestaurantViewHolder restaurantViewHolder) {
        this.restaurantViewHolder = restaurantViewHolder;
    }

    static class OnOpenPressedListener extends RestaurantCardButtonListener {

        private final RecycleViewOnExpandChangeCallback callback;
        private final int position;

        public OnOpenPressedListener(RestaurantViewHolder restaurantViewHolder, RecycleViewOnExpandChangeCallback callback, int position) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            callback.onExpandChange(restaurantViewHolder.toggleExpand(), position);
        }
    }

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

    static class OnDeleteRestaurantListener extends RestaurantCardButtonListener {

        private final RecycleViewOnExpandChangeCallback callback;
        private final Restaurant restaurant;

        OnDeleteRestaurantListener(RestaurantViewHolder restaurantViewHolder, RecycleViewOnExpandChangeCallback callback, Restaurant restaurant) {
            super(restaurantViewHolder);
            this.callback = callback;
            this.restaurant = restaurant;
        }

        @Override
        public void onClick(View v) {
            Log.d("delet", "onClick: trying to delete adapterpos = " + restaurantViewHolder.getAdapterPosition() + " layoutpos = " + restaurantViewHolder.getLayoutPosition());
            callback.onDeleteRestaurant(restaurantViewHolder.getAdapterPosition());
        }
    }

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
