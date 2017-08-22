package cel.dev.restaurants.mainactivity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public interface MainActivityMVP {

    interface View {
        Fragment getCurrentFragment();

        void showAboutDialog();

        void handleAfterDeleteRestaurants();

        void setFragment(Fragment fragment, @DrawableRes int fabIconDrawableId);

        void hideShowFAB(boolean show);

        void showDeleteAllRestaurantsDialog();


    }

    interface Presenter {
        void fabPressed();

        boolean tabPressed(@IdRes int navId, Fragment fragment);

        void deleteAllRestaurants();

        void onSaveInstanceState(Bundle outState);

        void handleSavedInstanceState(Bundle savedInstanceState);

        void loadFragment();

        void menuItemSelected(@IdRes int itemId);

    }
}
