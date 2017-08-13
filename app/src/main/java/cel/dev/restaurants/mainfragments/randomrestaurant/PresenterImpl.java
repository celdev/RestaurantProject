package cel.dev.restaurants.mainfragments.randomrestaurant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
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
        randomiseSettings = new RandomiseSettings(notThisRestaurantIds);
    }

    @Override
    public void onRequestingLocation() {
        view.showLoadingLocationDialog();
        view.requestLocation();
    }



    public void loadRestaurant() {
        for (Long aLong : randomiseSettings.getNotTheseRestaurantsById()) {
            Log.d(TAG, "loadRestaurant: longs in not these ids = " + aLong);
        }
        restaurant = restaurantDAO.getRandomRestaurant(randomiseSettings);
        if (restaurant == null) {
            view.handleNoRestaurantsFound();
        } else {
            notThisRestaurantIds.add(restaurant.getId());
            view.injectRestaurant(restaurant,restaurantDAO);
        }
    }

    @Override
    public void injectLocation(double latitude, double longitude) {
        Log.d(TAG, "injectLocation: injecting lat and long to randomise settings lat " + latitude + " lon " + longitude);
        randomiseSettings.injectLocation(latitude, longitude);
        view.hideLoadingDialog();
        loadRestaurant();
    }

    @Override
    public void resetSettings() {
        notThisRestaurantIds.clear();
        initRandomiseSettings(notThisRestaurantIds);
        view.requestLocation();
    }

    @Override
    public void showNewRestaurant(@NonNull RandomChoice choice) {
        changeSettings(choice);
        loadRestaurant();
    }

    private void changeSettings(RandomChoice choice) {
        switch (choice) {
            case CLOSER:
                randomiseSettings.closer();
                break;
            case DIFFERENT_FOOD:
                randomiseSettings.injectKitchenTypeToFilter(restaurant.getKitchenTypes());
                break;
            case CHEAPER:
                setCheaperSettings();
                break;
            case NO_CHANGE:
                break;
        }
        loadRestaurant();
    }

    private void setCheaperSettings() {
        for (BudgetType budgetType : restaurant.getBudgetTypes()) {
            randomiseSettings.setCheaperThan(budgetType);
        }
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
