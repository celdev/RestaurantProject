package cel.dev.restaurants.uicontracts;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;

public interface ShowRestaurantsMVP {


    interface View {

        void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO);

    }

    interface Presenter {

        void onLoadFragment();

        void onCloseFragment();
    }

}
