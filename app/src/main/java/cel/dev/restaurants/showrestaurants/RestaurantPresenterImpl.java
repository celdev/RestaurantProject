package cel.dev.restaurants.showrestaurants;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;
import cel.dev.restaurants.R;
import cel.dev.restaurants.repository.KitchenTypeDAO;
import cel.dev.restaurants.repository.KitchenTypeDAOImpl;
import cel.dev.restaurants.repository.RestaurantDAOImpl;

class RestaurantPresenterImpl implements ShowRestaurantsMVP.Presenter {

    private ShowRestaurantsMVP.View view;

    public RestaurantPresenterImpl(ShowRestaurantsMVP.View view) {
        this.view = view;
    }

    @Override
    public void onLoadFragment() {
        view.injectData(DEBUGfakeRestaurants());
    }


    public List<Restaurant> DEBUGfakeRestaurants() {
        return new RestaurantDAOImpl().getAllRestaurants();
    }
}
