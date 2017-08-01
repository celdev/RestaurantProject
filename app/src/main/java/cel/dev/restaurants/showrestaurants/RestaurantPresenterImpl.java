package cel.dev.restaurants.showrestaurants;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.FoodType;
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
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.addAll(new RestaurantDAOImpl().getAllRestaurants());
        restaurants.add(createFakeRestaurant());
        restaurants.add(createFakeRestaurant());
        restaurants.add(createFakeRestaurant());
        restaurants.add(createFakeRestaurant());
        restaurants.add(createFakeRestaurant());
        restaurants.add(createFakeRestaurant());
        return restaurants;
    }

    private Restaurant createFakeRestaurant() {
        KitchenTypeDAO kitchenTypeDAO = new KitchenTypeDAOImpl();
        List<FoodType> foodTypes = kitchenTypeDAO.getAllFoodTypes().subList(0, 3);
        return new RestaurantPlaceholderImage("test", 3.5f, new BudgetType[]{BudgetType.CHEAP, BudgetType.EXPENSIVE}, 12.1, 22.2,
                foodTypes.toArray(new FoodType[foodTypes.size()]), R.drawable.restaurant_placeholder);

    }
}
