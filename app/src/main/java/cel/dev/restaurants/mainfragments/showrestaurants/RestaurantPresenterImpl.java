package cel.dev.restaurants.mainfragments.showrestaurants;

import android.content.Context;

import java.util.List;

import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;

class RestaurantPresenterImpl implements ShowRestaurantsMVP.Presenter {

    private ShowRestaurantsMVP.View view;
    private final Context context;
    private RestaurantDAO restaurantDAO;

    public RestaurantPresenterImpl(ShowRestaurantsMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        this.restaurantDAO = new RestaurantDAOImpl(context);
    }

    @Override
    public void onLoadFragment() {
        view.injectData(getRestaurants(), restaurantDAO);
    }

    public List<Restaurant> getRestaurants() {
        return restaurantDAO.getAllRestaurants();
    }
}
