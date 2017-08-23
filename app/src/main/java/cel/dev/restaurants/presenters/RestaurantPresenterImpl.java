package cel.dev.restaurants.presenters;

import android.content.Context;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.persistanceimpl.RestaurantDAOImpl;
import cel.dev.restaurants.uicontracts.ShowRestaurantsMVP;

/** The presenter for the Show restaurant Activity
 *  responsible for retrieving all restaurants from the database and injecting those
 *  into the view
 * */
public class RestaurantPresenterImpl implements ShowRestaurantsMVP.Presenter {

    private ShowRestaurantsMVP.View view;
    private RestaurantDAO restaurantDAO;

    /** Creates the DAO which will communicate with the database
     * */
    public RestaurantPresenterImpl(ShowRestaurantsMVP.View view, Context context) {
        this.view = view;
        this.restaurantDAO = new RestaurantDAOImpl(context);
    }

    /** This will be called when the fragment has loaded and all restaurants will
     *  be injected into the view in order to create the adapter for the RecycleView
     * */
    @Override
    public void onLoadFragment() {
        view.injectData(getRestaurants(), restaurantDAO);
    }

    /** Retrieves and returns all Restaurants from the database
     * */
    public List<Restaurant> getRestaurants() {
        return restaurantDAO.getAllRestaurants();
    }

    /** called when the onDestroy-method is called in the view
     *  calls the DAO to close the database
     * */
    @Override
    public void onCloseFragment() {
        restaurantDAO.closeDB();
    }
}
