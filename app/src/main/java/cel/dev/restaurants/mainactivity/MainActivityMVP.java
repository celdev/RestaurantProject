package cel.dev.restaurants.mainactivity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

public interface MainActivityMVP {

    interface View {
        void setFragment(Fragment fragment, @DrawableRes int fabIconDrawableId);

        void hideShowFAB(boolean show);
    }

    interface Presenter {
        void fabPressed();

        boolean tabPressed(@IdRes int navId);


        void onSaveInstanceState(Bundle outState);

        void handleSavedInstanceState(Bundle savedInstanceState);

        void loadFragment();
    }
}
