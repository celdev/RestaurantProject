package cel.dev.restaurants.repository;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;

public class RestaurantDAOImpl implements RestaurantDAO {

    private static int id = 0;

    private static List<Restaurant> restaurants = new ArrayList<>();

    public int getNextId() {
        return id++;
    }

    static {
        restaurants.addAll(new RestaurantDAOImpl().getAllRestaurants());
        Restaurant fakeRestaurant1 = createFakeRestaurant();
        fakeRestaurant1.setFavorite(true);
        Restaurant fakeRestaurant2 = createFakeRestaurant();
        Restaurant fakeRestaurant3 = createFakeRestaurant();
        Restaurant fakeRestaurant4 = createFakeRestaurant();
        Restaurant fakeRestaurant5 = createFakeRestaurant();
        Restaurant fakeRestaurant6 = createFakeRestaurant();
        fakeRestaurant1.setName("Test 1");
        fakeRestaurant2.setName("Test 2 med ett lite längre namn");
        fakeRestaurant3.setName("Test 3");
        fakeRestaurant4.setName("Test 4 med ett väldigt långt namn, onödigt långt");
        fakeRestaurant5.setName("Test 5");
        fakeRestaurant6.setName("Test 6");
        RestaurantDAOImpl restaurantDAO = new RestaurantDAOImpl();
        restaurantDAO.saveRestaurant(fakeRestaurant1);
        restaurantDAO.saveRestaurant(fakeRestaurant2);
        restaurantDAO.saveRestaurant(fakeRestaurant3);
        restaurantDAO.saveRestaurant(fakeRestaurant4);
        restaurantDAO.saveRestaurant(fakeRestaurant5);
        restaurantDAO.saveRestaurant(fakeRestaurant6);
    }

    private static Restaurant createFakeRestaurant() {
        KitchenTypeDAO kitchenTypeDAO = new KitchenTypeDAOImpl();
        List<KitchenType> kitchenTypes = kitchenTypeDAO.getAllFoodTypes().subList(0, 3);
        return new RestaurantPlaceholderImage("test", 3.5f, new BudgetType[]{BudgetType.CHEAP, BudgetType.EXPENSIVE}, 12.1, 22.2,
                kitchenTypes.toArray(new KitchenType[kitchenTypes.size()]), R.drawable.restaurant_placeholder,false);

    }

    @Override
    public Restaurant getRestaurantById(int id) {
        return restaurants.get(id);
    }

    @Override
    public List<Restaurant> getRestaurantsByIds(List<Integer> ids) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int id : ids) {
            restaurants.add(getRestaurantById(id));
        }
        return restaurants;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurants;
    }

    @Override
    public List<Restaurant> getRestaurantsByLocation(String longitude, String latitude, double range) {
        return null;
    }

    @Override
    public List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType) {
        return null;
    }

    @Override
    public boolean saveRestaurant(Restaurant restaurant) {
        if (restaurant.getId() != Restaurant.NOT_SAVED_ID) {
            restaurants.set(restaurant.getId(), restaurant);
            return true;
        } else {
            restaurant.setId(getNextId());
            return restaurants.add(restaurant);
        }
    }

}
