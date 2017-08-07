package cel.dev.restaurants.mainfragments.randomrestaurant;

import android.content.Context;

import java.util.HashSet;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.utils.AndroidUtils;

class PresenterImpl implements RandomRestaurantMVP.Presenter {

    private final RandomRestaurantMVP.View view;
    private final Context context;

    private RestaurantDAO restaurantDAO;
    private RandomiseSettings randomiseSettings;
    private Restaurant restaurant;

    public PresenterImpl(RandomRestaurantMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        restaurantDAO = new RestaurantDAOImpl(context);
        randomiseSettings = new RandomiseSettings(false, 0.0, 0.0, 0.0, false, false,
                new HashSet<BudgetType>(), false, new HashSet<KitchenType>(), new HashSet<Long>());
    }

    public void loadRestaurant() {
        restaurant = restaurantDAO.getRandomRestaurant(randomiseSettings);
        view.injectRestaurant(restaurant,restaurantDAO);
    }

    @Override
    public void deleteCurrentRestaurant() {
        if (restaurantDAO.removeRestaurant(restaurant)) {
            loadRestaurant();
        }
    }

    @Override
    public void editCurrentRestaurant() {
        context.startActivity(AndroidUtils.createEditRestaurantActivityIntent(context, restaurant));
    }

    @Override
    public void favoriteRestaurantClicked() {
        restaurant.setFavorite(!restaurant.isFavorite());
        restaurantDAO.setRestaurantFavorite(restaurant);
    }

    @Override
    public void showRestaurantLocation() {
        context.startActivity(AndroidUtils.createMapActivityIntentWithLatLong(context, restaurant.getLatitude(), restaurant.getLongitude()));
    }
}
