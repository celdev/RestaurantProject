package cel.dev.restaurants.presenters;

import android.content.Context;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.persistance.RestaurantDAOImpl;
import cel.dev.restaurants.uicontracts.ShowRestaurantsMVP;

/** The presenter for the Show restaurant Activity
 *  responsible for retrieving all restaurants from the database and injecting those
 *  into the view
 * */
public class RestaurantPresenterImpl implements ShowRestaurantsMVP.Presenter {

    private ShowRestaurantsMVP.View view;
    private RestaurantDAO restaurantDAO;

    public RestaurantPresenterImpl(ShowRestaurantsMVP.View view, Context context) {
        this.view = view;
        this.restaurantDAO = new RestaurantDAOImpl(context);
    }

    @Override
    public void onLoadFragment() {
        view.injectData(getRestaurants(), restaurantDAO);
    }

    public List<Restaurant> getRestaurants() {
        return restaurantDAO.getAllRestaurants();
    }

    @Override
    public void onCloseFragment() {
        restaurantDAO.closeDB();
    }
}
