package cel.dev.restaurants.mainfragments.showrestaurants;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;

public interface ShowRestaurantsMVP {


    interface View {

        void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO);

    }

    interface Presenter {

        void onLoadFragment();

    }

}
