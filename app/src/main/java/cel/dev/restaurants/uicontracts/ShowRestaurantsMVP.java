package cel.dev.restaurants.uicontracts;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;

/** This is the MVP contract for the show restaurants fragment
 * */
public interface ShowRestaurantsMVP {


    interface View {

        /** Allows the presenter to inject restaurants into the fragment
         *  which are used to create the adapter for the RecycleView
         *  It also passes in the RestaurantDAO since the Adapter needs this in order to
         *  get the image of the restaurant when the restaurant is to be shown
         * */
        void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO);

    }

    interface Presenter {

        /** This will be called when the fragment has loaded and all restaurants will
         *  be injected into the view in order to create the adapter for the RecycleView
         * */
        void onLoadFragment();

        /** called when the onDestroy-method is called in the view
         *  calls the DAO to close the database
         * */
        void onCloseFragment();
    }

}
