package cel.dev.restaurants.mainfragments.randomrestaurant;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.utils.AndroidUtils;

class PresenterImpl implements RandomRestaurantMVP.Presenter {

    private static final String TAG = "random presenter";
    private final RandomRestaurantMVP.View view;
    private final Context context;

    private RestaurantDAO restaurantDAO;
    private RandomiseSettings randomiseSettings;
    private Restaurant restaurant;

    private Set<Long> notThisRestaurantIds;

    public PresenterImpl(RandomRestaurantMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        restaurantDAO = new RestaurantDAOImpl(context);
        notThisRestaurantIds = new HashSet<>();
        initRandomiseSettings(notThisRestaurantIds);
    }

    private void initRandomiseSettings(Set<Long> notThisRestaurantIds) {
        randomiseSettings = new RandomiseSettings(false, 0.0, 0.0, 0.0, false, false,
                new HashSet<BudgetType>(), false, new HashSet<KitchenType>(), notThisRestaurantIds);
    }

    public void loadRestaurant() {
        for (Long aLong : randomiseSettings.getNotTheseRestaurantsById()) {
            Log.d(TAG, "loadRestaurant: longs in not these ids = " + aLong);
        }
        restaurant = restaurantDAO.getRandomRestaurant(randomiseSettings);
        if (restaurant == null) {
            view.handleNoRestaurantsFound();
        } else {
            view.injectRestaurant(restaurant,restaurantDAO);
        }
    }

    @Override
    public void resetSettings() {
        notThisRestaurantIds.clear();
        initRandomiseSettings(notThisRestaurantIds);
        view.showSettingsDialog();
    }

    @Override
    public void setSettings(boolean useLocation, double range, double latitude, double longitude, boolean useFavorite, boolean useBudgetTypes, boolean useKitchenTypes,
                            Set<BudgetType> budgetTypes, Set<KitchenType> kitchenTypes) {
        notThisRestaurantIds.clear();
        randomiseSettings = new RandomiseSettings(useLocation, range, latitude, longitude,
                useFavorite, useBudgetTypes, budgetTypes, useKitchenTypes, kitchenTypes, notThisRestaurantIds);
    }



    @Override
    public void notThisRestaurantPressed() {
        if (restaurant != null) {
            notThisRestaurantIds.add(restaurant.getId());
        }
        loadRestaurant();
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
