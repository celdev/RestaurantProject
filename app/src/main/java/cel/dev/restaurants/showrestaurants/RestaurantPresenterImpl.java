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
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.addAll(new RestaurantDAOImpl().getAllRestaurants());
        Restaurant fakeRestaurant1 = createFakeRestaurant();
        fakeRestaurant1.setFavorite(true);
        Restaurant fakeRestaurant2 = createFakeRestaurant();
        Restaurant fakeRestaurant3 = createFakeRestaurant();
        Restaurant fakeRestaurant4 = createFakeRestaurant();
        Restaurant fakeRestaurant5 = createFakeRestaurant();
        Restaurant fakeRestaurant6 = createFakeRestaurant();
        fakeRestaurant1.setName("Test 1");
        fakeRestaurant2.setName("Test 2");
        fakeRestaurant3.setName("Test 3");
        fakeRestaurant4.setName("Test 4");
        fakeRestaurant5.setName("Test 5");
        fakeRestaurant6.setName("Test 6");
        restaurants.add(fakeRestaurant1);
        restaurants.add(fakeRestaurant2);
        restaurants.add(fakeRestaurant3);
        restaurants.add(fakeRestaurant4);
        restaurants.add(fakeRestaurant5);
        restaurants.add(fakeRestaurant6);
        return restaurants;
    }

    private Restaurant createFakeRestaurant() {
        KitchenTypeDAO kitchenTypeDAO = new KitchenTypeDAOImpl();
        List<KitchenType> kitchenTypes = kitchenTypeDAO.getAllFoodTypes().subList(0, 3);
        return new RestaurantPlaceholderImage("test", 3.5f, new BudgetType[]{BudgetType.CHEAP, BudgetType.EXPENSIVE}, 12.1, 22.2,
                kitchenTypes.toArray(new KitchenType[kitchenTypes.size()]), R.drawable.restaurant_placeholder,false);

    }
}
