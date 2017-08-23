package cel.dev.restaurants.uicontracts;

import cel.dev.restaurants.model.RandomChoice;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;

public interface RandomRestaurantMVP {

    interface View {


        void requestLocation();

        void hideLoadingDialog();

        void handleNoRestaurantsFound();

        void injectRestaurant(Restaurant restaurant, RestaurantDAO restaurantDAO);

        void showLoadingLocationDialog();

    }

    interface Presenter {

        void loadRestaurant();

        void deleteCurrentRestaurant();

        void editCurrentRestaurant();

        void favoriteRestaurantClicked();

        void showRestaurantLocation();

        void injectLocation(double latitude, double longitude);

        void resetSettings();

        void showNewRestaurant(RandomChoice closer);

        void onRequestingLocation();

        void onCloseFragment();
    }

}
