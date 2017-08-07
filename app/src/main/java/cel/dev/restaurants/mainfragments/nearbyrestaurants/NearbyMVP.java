package cel.dev.restaurants.mainfragments.nearbyrestaurants;

import java.util.List;

import cel.dev.restaurants.mainfragments.showrestaurants.ShowRestaurantsMVP;
import cel.dev.restaurants.model.Restaurant;

public interface NearbyMVP {

    interface View extends ShowRestaurantsMVP.View {

        void requestLocation();

        void refreshNearbyList();

        void showGetLocationPermissionDialog();

        boolean checkHasLocationPermission();

        void showRangeDialog(double range);


    }

    interface Presenter {

        void saveRange(double range);

        void getRestaurantsAndInject(double latitude, double longitude);

        void onFABPressed();

        void refreshList();

        int getRangeForSeekBar();
    }

}
