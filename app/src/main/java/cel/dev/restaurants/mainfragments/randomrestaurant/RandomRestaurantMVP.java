package cel.dev.restaurants.mainfragments.randomrestaurant;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;

public interface RandomRestaurantMVP {

    interface View {

        void injectRestaurant(Restaurant restaurant, RestaurantDAO restaurantDAO);

    }

    interface Presenter {

        void loadRestaurant();

        void deleteCurrentRestaurant();

        void editCurrentRestaurant();

        void favoriteRestaurantClicked();

        void showRestaurantLocation();
    }

}
