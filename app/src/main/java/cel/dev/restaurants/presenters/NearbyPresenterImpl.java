package cel.dev.restaurants.presenters;

import android.content.Context;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.persistanceimpl.RestaurantDAOImpl;
import cel.dev.restaurants.uicontracts.NearbyMVP;
import cel.dev.restaurants.utils.Values;

/** The presenter of the Nearby fragment
 *  Mostly responsible for finding the nearby restaurants using the location and the range provided from the view
 *  also stores the range in a SharedPreference which allows the app to remember the range the user
 *  has decided previously
 *
 *  The SeekBar only supports an integer value which is to large for my implementation of the range
 *  in order to bring this into a reasonable value the value of the SeekBar (0-100) is divided by
 *  SEEK_BAR_PROGRESS_MODIFIER
 * */
public class NearbyPresenterImpl implements NearbyMVP.Presenter {

    private static final double SEEK_BAR_PROGRESS_MODIFIER = 1000.0;

    private NearbyMVP.View view;
    private Context context;
    private RestaurantDAO restaurantDAO;

    private double range;

    /** Creates the DAO-object which will communicate with the database and provide this
     *  object with restaurants
     * */
    public NearbyPresenterImpl(NearbyMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        this.range = loadRange();
        restaurantDAO = new RestaurantDAOImpl(context);
    }

    /** Loads the range from the SharedPreference
     *  If the shared preference doesn't contains a vaue the Values.DEFAULT_RANGE_VALUE is used
     * */
    private double loadRange() {
        return this.context.getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE).getFloat(Values.SharePreferenceKeys.RANGE_KEY, Values.DEFAULT_RANGE_VALUE);
    }

    /** Stores the range in the SharedPreference after dividing it by the SEEK_BAR_PROGRESS_MODIFIER
     * if this value is lower than the MINIMUM_RANGE_VALUE (defined in the Values class) the
     * value is set to the minimum value
     * */
    @Override
    public void saveRange(double range) {
        range /= SEEK_BAR_PROGRESS_MODIFIER;
        if (range < Values.MINIMUM_RANGE_VALUE) {
            range = Values.MINIMUM_RANGE_VALUE;
        }
        this.range = range;
        this.context.getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putFloat(Values.SharePreferenceKeys.RANGE_KEY, (float) range).apply();
    }

    /** Gets the restaurants which are considered close using the location and the range
     *  and injects them into the view which will use the restaurants in order to create the adapter
     *  for the RecycleView
     * */
    @Override
    public void getRestaurantsAndInject(double latitude, double longitude) {
        view.injectData(restaurantDAO.getRestaurantsByLocation(latitude, longitude, range), restaurantDAO);
    }

    /** The view calls this method when the floating action button is pressed
     *  calls the view to show the range dialog using the current saved range
     * */
    @Override
    public void onFABPressed() {
        view.showRangeDialog(range);
    }

    /** returns the range multiplied by the SEEK_BAR_PROGRESS_MODIFIER
     *  to turn the range into a range of 1 to 100
     * */
    @Override
    public int getRangeForSeekBar() {
        return (int)(range * SEEK_BAR_PROGRESS_MODIFIER);
    }

    /** calls to the view to request the location of the user again
     * */
    @Override
    public void refreshList() {
        view.requestLocation();
    }

    /** called when the fragments onDestroy-method is called
     *  calls the DAO to close the database
     * */
    @Override
    public void onCloseFragment() {
        restaurantDAO.closeDB();
    }


}
