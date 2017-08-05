package cel.dev.restaurants.showrestaurants;

import android.content.Context;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;

public interface ShowRestaurantsMVP {


    interface View {

        void injectData(List<Restaurant> restaurants);

    }

    interface Presenter {

        void onLoadFragment(Context context);

    }

}
