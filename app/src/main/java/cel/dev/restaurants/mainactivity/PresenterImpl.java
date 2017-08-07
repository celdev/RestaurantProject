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

    @Override
    public boolean tabPressed(@IdRes int navId) {
        switch (navId) {
            case R.id.nav_nearby:
                view.setFragment(createFragment(navId), R.drawable.ic_settings_black_24dp);
                return true;
            case R.id.nav_find:
                view.setFragment(createFragment(navId), R.drawable.ic_filter_list_black_24dp);
                return true;
            case R.id.nav_restaurants:
                view.setFragment(createFragment(navId), R.drawable.ic_add_black_24dp);
                return true;
            default:
                Log.e(TAG, "tabPressed: unhandled item id " + navId);
                return false;

        }
    }

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
