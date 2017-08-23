package cel.dev.restaurants.uicontracts;

import android.os.Bundle;

import cel.dev.restaurants.fragments.RandomRestaurantFragment;
import cel.dev.restaurants.view.RandomChoice;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.view.RandomiseSettings;

public interface RandomRestaurantMVP {

    interface View {


        void requestLocation();

        void hideLoadingDialog();

        void handleNoRestaurantsFound();

        void injectRestaurant(Restaurant restaurant, RestaurantDAO restaurantDAO);

        void showLoadingLocationDialog();

    }

    interface Presenter {

        /** Loads a random restaurant from the restaurantDAO
         *  if this restaurant is null (a restaurant couldn't be found with the current settings)
         *  the view is set to present that no restaurants could be found and the user can reset the settings and try again
         *  otherwise the found restaurant is injected into the view
         * */
        void loadRestaurant();

        /** Deletes the restaurant (called when the delete restaurant button is pressed)
         * */
        void deleteCurrentRestaurant();

        /** Starts the edit restaurant activity
         * */
        void editCurrentRestaurant();

        /** changes the favorite status of the restaurant
         * */
        void favoriteRestaurantClicked();

        /** Show the location of the current restaurant on a map
         * */
        void showRestaurantLocation();

        /** This method is called when the activity gets a location
         *  The location is stored in the randomise settings and used to retrieve a restaurant which is close
         * */
        void injectLocation(double latitude, double longitude);

        /** Resets the settings of the randomise settings
         * */
        void resetSettings();

        /** This method is called when the user presses any of the buttons which are shown together
         *  with the presented random restaurant
         *  The choices the use can make is:
         *      CLOSER          - lowers the range
         *      DIFFERENT_FOOD  - Won't show a restaurant with the same type of food as the current restaurant
         *      CHEAPER         - Won't show a restaurant which is as expensive as the current restaurant
         *      NO_CHANGE       - Doesn't alter the settings but won't show this restaurant again
         *
         * The three first options also adds the current restaurants to the Set of restaurants to filter
         * so that the current restaurant isn't chosen again
         *
         * When the settings has been altered a new restaurant is loaded
         * */
        void showNewRestaurant(RandomChoice closer);

        /** Asks the view to request the location of the user
         *  shows the loading progress dialog while loading the location
         * */
        void onRequestingLocation();

        /** This method can be used to execute methods which should be used when the
         *  fragment is being destroyed. such as closing the database
         * */
        void onCloseFragment();

        /** This method lets the presenter save it's state before a rotation change
         * */
        void saveState(Bundle outState);

        /** This method allows the view to pass settings retrieved from the bundle
         *  since the presenter is created in onResume which happens after
         *  the method in which the bundle is available
         * */
        void injectState(RandomiseSettings randomiseSettings, long restaurantId);
    }

}
