package cel.dev.restaurants.mainfragments.randomrestaurant;

import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;

public interface RandomRestaurantMVP {

    interface View {

        boolean checkHasLocationPermission();

        void showGetLocationPermissionDialog();

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
    }

}
