package cel.dev.restaurants.mainfragments.nearbyrestaurants;

import android.content.Context;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.utils.Values;

class NearbyPresenterImpl implements NearbyMVP.Presenter {



    private NearbyMVP.View view;
    private Context context;
    private RestaurantDAO restaurantDAO;

    private double range;

    NearbyPresenterImpl(NearbyMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        this.range = loadRange();
        restaurantDAO = new RestaurantDAOImpl(context);
    }

    private double loadRange() {
        return this.context.getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE).getFloat(Values.SharePreferenceKeys.RANGE_KEY, Values.DEFAULT_RANGE_VALUE);
    }

    @Override
    public void saveRange(double range) {
        range /= 1000.0;
        if (range < Values.MINIMUM_RANGE_VALUE) {
            range = Values.MINIMUM_RANGE_VALUE;
        }
        this.range = range;
        this.context.getSharedPreferences(Values.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit().putFloat(Values.SharePreferenceKeys.RANGE_KEY, (float) range).apply();
    }

    @Override
    public void getRestaurantsAndInject(double latitude, double longitude) {
        view.injectData(restaurantDAO.getRestaurantsByLocation(latitude, longitude, range), restaurantDAO);
    }

    @Override
    public void onFABPressed() {
        view.showRangeDialog(range);
    }

    @Override
    public int getRangeForSeekBar() {
        return (int)(range * 100);
    }

    @Override
    public void refreshList() {
        view.requestLocation();
    }


}
