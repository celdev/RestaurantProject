package cel.dev.restaurants.mainactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;

import cel.dev.restaurants.R;
import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.mainfragments.randomrestaurant.RandomRestaurantFragment;
import cel.dev.restaurants.mainfragments.nearbyrestaurants.NearbyFragment;
import cel.dev.restaurants.mainfragments.showrestaurants.RestaurantFragment;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;

/** This is the presenter of the MainActivity
 *  It's mostly responsible for handling change in which fragment should be
 *  active after a tab has been pressed in the BottomNavigationView
 * */
class PresenterImpl implements MainActivityMVP.Presenter {


    public static final String TAG = "main activity pres";
    private final MainActivityMVP.View view;
    private final Context context;

    private FABFragmentHandler fabHandler;

    private int activeFragment;
    public static final String ACTIVE_FRAGMENT_KEY = "active_fragment";


    public PresenterImpl(MainActivityMVP.View view, Context context) {
        this.view = view;
        this.context = context;
    }


    @Override
    public void fabPressed() {
        fabHandler.handleFABClick();
    }

    /** Create a fragment depending on the fragmentId
     * */
    private Fragment createFragment(@IdRes int fragmentId) {
        Fragment fragment;
        switch (fragmentId) {
            case R.id.nav_nearby:
                fragment =  NearbyFragment.newInstance();
                fabHandler = (FABFragmentHandler) fragment;
                break;
            case R.id.nav_find:
                fragment = RandomRestaurantFragment.newInstance();
                fabHandler = (FABFragmentHandler) fragment;
                break;
            case R.id.nav_restaurants:
                fragment = RestaurantFragment.newInstance();
                fabHandler = (FABFragmentHandler) fragment;
                break;
            default:
                throw new RuntimeException("unhandled nav id " + fragmentId);
        }
        activeFragment = fragmentId;
        return fragment;
    }

    /** Creates and sets the fragment depending on which button was pressed
     *  also sets the handler of the Floating Action Button press to the created fragment
     * */
    @Override
    public boolean tabPressed(@IdRes int navId) {
        switch (navId) {
            case R.id.nav_nearby:
                view.hideShowFAB(true);
                view.setFragment(createFragment(navId), R.drawable.ic_settings_black_24dp);
                return true;
            case R.id.nav_find:
                view.hideShowFAB(false);
                view.setFragment(createFragment(navId), R.drawable.ic_filter_list_black_24dp);
                return true;
            case R.id.nav_restaurants:
                view.hideShowFAB(true);
                view.setFragment(createFragment(navId), R.drawable.ic_add_black_24dp);
                return true;
            default:
                Log.e(TAG, "tabPressed: unhandled item id " + navId);
                return false;

        }
    }


    /** handles the press on an item in the options dialog
     * */
    @Override
    public void menuItemSelected(@IdRes int itemId) {
        switch (itemId) {
            case R.id.context_about_app:
                view.showAboutDialog();
                break;
            case R.id.context_delete_all:
                view.showDeleteAllRestaurantsDialog();
                break;
        }
    }

    /** Deletes all restaurants and calls the view to handle the fragment state after all
     *  restaurants have been deleted (returns the user to the show restaurant fragment)
     * */
    @Override
    public void deleteAllRestaurants() {
        new RestaurantDAOImpl(context).deleteAllRestaurants();
        view.handleAfterDeleteRestaurants();
    }

    /** Stores which fragment is active so this fragment can be set during
     *  the re-creation of the activity
     * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ACTIVE_FRAGMENT_KEY, activeFragment);
    }

    @Override
    public void handleSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            activeFragment = savedInstanceState.getInt(ACTIVE_FRAGMENT_KEY);
        } else {
            activeFragment = R.id.nav_restaurants;
        }
    }

    @Override
    public void loadFragment() {
        tabPressed(activeFragment);
    }
}
