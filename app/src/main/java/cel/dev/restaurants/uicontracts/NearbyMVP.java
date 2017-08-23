package cel.dev.restaurants.uicontracts;

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

        void onCloseFragment();
    }

}
