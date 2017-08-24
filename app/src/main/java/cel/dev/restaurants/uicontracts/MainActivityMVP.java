package cel.dev.restaurants.uicontracts;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

public interface MainActivityMVP {

    interface View {


        /** Returns the current fragment
         * */
        Fragment getCurrentFragment();

        /** Shows an about dialog
         * */
        void showAboutDialog();

        /***/
        void handleAfterDeleteRestaurants();

        /** Sets the fragment and the icon of the floating action button to the
         *  fragment and drawable res id passed as parameters
         * */
        void setFragment(Fragment fragment, @DrawableRes int fabIconDrawableId);

        /** hides or shows the floating action button depending on the param show
         * */
        void hideShowFAB(boolean show);

        /** Shows a dialog asking if the user wants to delete all restaurants
         *  if yes then all restaurants are deleted
         *  else the dialog is closed
         * */
        void showDeleteAllRestaurantsDialog();


    }

    interface Presenter {

        /** called by the view when the floating action button is pressed
         *  calls the current active fragment to handle the click
         * */
        void fabPressed();

        /** Creates and sets the fragment depending on which button was pressed
         *  also sets the handler of the Floating Action Button press to the created fragment
         *
         *  if a fragment is passed in this fragment is used instead of recreating the fragment
         * */
        boolean tabPressed(@IdRes int navId, Fragment fragment);

        /** Deletes all restaurants and calls the view to handle the fragment state after all
         *  restaurants have been deleted (returns the user to the show restaurant fragment)
         * */
        void deleteAllRestaurants();

        /** Stores which fragment is active so this fragment can be set during
         *  the re-creation of the activity
         * */
        void onSaveInstanceState(Bundle outState);

        /** This method provides functionality to save which fragment was active when
         *  the onSaveInstanceState-method is called
         *  so that the active fragment can be recreated after e.g. an orientation change
         * */
        void handleSavedInstanceState(Bundle savedInstanceState);

        /** Loads the fragment with (nav bar) id = activeFragment
         * */
        void loadFragment();

        /** handles the press on an item in the options dialog
         * */
        void menuItemSelected(@IdRes int itemId);

    }
}
