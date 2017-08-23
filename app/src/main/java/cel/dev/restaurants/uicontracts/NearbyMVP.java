package cel.dev.restaurants.uicontracts;

/** This is the MVP contract for the Nearby restaurants fragment
 * */
public interface NearbyMVP {


    /** This view extends the ShowRestaurantsMVP.View interface which provides a basic
     *  contract for a Fragment which will show restaurants in a RecycleView
     * */
    interface View extends ShowRestaurantsMVP.View {

        /** Request the location of the user using FusedLocationService
         *  adds this object as an listener for the onSuccess-event and the onComplete-event
         *  since onSuccess sometimes fail for no apparent reason
         * */
        void requestLocation();


        /** Calls the presenter to refresh which restaurants is considered close
         * */
        void refreshNearbyList();


        /** Shows a dialog that staes that the application needs location permission in
         *  order for this functionality of the application to work.
         * */
        void showGetLocationPermissionDialog();


        /** Returns true if the application has location permission
         * */
        boolean checkHasLocationPermission();

        /** Shows a set range dialog
         *  which allows the user to set the range (close <--> not very close)
         *  of restaurants to consider nearby
         *  When the range (using a SeekBar) is changed the nearby restaurants are updated directly
         *  Which provides good feedback for the user in determining what is "close" and "not very close"
         * */
        void showRangeDialog(double range);


    }

    interface Presenter {

        /** Stores the range in the SharedPreference
         * */
        void saveRange(double range);

        /** Gets the restaurants which are considered close using the location and the range
         *  and injects them into the view which will use the restaurants in order to create the adapter
         *  for the RecycleView
         * */
        void getRestaurantsAndInject(double latitude, double longitude);

        /** The view calls this method when the floating action button is pressed
         *  calls the view to show the range dialog using the current saved range
         * */
        void onFABPressed();

        /** calls to the view to request the location of the user again
         * */
        void refreshList();

        /** returns the range multiplied by the SEEK_BAR_PROGRESS_MODIFIER
         *  to turn the range into a range of 1 to 100
         * */
        int getRangeForSeekBar();

        /** called when the fragments onDestroy-method is called
         *  calls the DAO to close the database
         * */
        void onCloseFragment();
    }

}
