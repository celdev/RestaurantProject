package cel.dev.restaurants.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

import cel.dev.restaurants.view.RandomChoice;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.view.RandomiseSettings;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.persistanceimpl.RestaurantDAOImpl;
import cel.dev.restaurants.uicontracts.RandomRestaurantMVP;
import cel.dev.restaurants.utils.AndroidUtils;

/** This is the presenter for the Find Random Restaurant Activity
 *  The responsibilities of this class is mostly, to store the randomise settings which are
 *  built by the choices the user makes when presented with a random restaurant.
 *  This class is also responsible for fetching a random restaurant using these settings
 *  from the RestaurantDAO
 * */
public class RandomRestaurantPresenterImpl implements RandomRestaurantMVP.Presenter {

    private static final String TAG = "random presenter";
    private final RandomRestaurantMVP.View view;
    private final Context context;

    private RestaurantDAO restaurantDAO;
    private RandomiseSettings randomiseSettings;
    private Restaurant restaurant;

    private Set<Long> notThisRestaurantIds;

    public RandomRestaurantPresenterImpl(RandomRestaurantMVP.View view, Context context) {
        this.view = view;
        this.context = context;
        restaurantDAO = new RestaurantDAOImpl(context);
        notThisRestaurantIds = new HashSet<>();
        initRandomiseSettings(notThisRestaurantIds);
    }

    private void initRandomiseSettings(Set<Long> notThisRestaurantIds) {
        randomiseSettings = new RandomiseSettings(notThisRestaurantIds);
    }

    /** Asks the view to request the location
     * */
    @Override
    public void onRequestingLocation() {
        view.showLoadingLocationDialog();
        view.requestLocation();
    }

    /** Loads a random restaurant from the restaurantDAO
     *  if this restaurant is null (a restaurant couldn't be found with the current settings)
     *  the view is set to present that no restaurants could be found and the user can reset the settings and try again
     *  otherwise the found restaurant is injected into the view
     * */
    public void loadRestaurant() {
        restaurant = restaurantDAO.getRandomRestaurant(randomiseSettings);
        if (restaurant == null) {
            view.handleNoRestaurantsFound();
        } else {
            notThisRestaurantIds.add(restaurant.getId());
            view.injectRestaurant(restaurant,restaurantDAO);
        }
    }

    /** This method is called when the activity gets a location
     *  The location is stored in the randomise settings and used to retrieve a restaurant which is close
     * */
    @Override
    public void injectLocation(double latitude, double longitude) {
        randomiseSettings.injectLocation(latitude, longitude);
        view.hideLoadingDialog();
        loadRestaurant();
    }

    /** Resets the settings of the randomise settings
     * */
    @Override
    public void resetSettings() {
        notThisRestaurantIds.clear();
        initRandomiseSettings(notThisRestaurantIds);
        view.requestLocation();
    }

    /** This method is called when the user presses any of the buttons which are shown together
     *  with the presented random restaurant
     *  The choices the use can make is:
     *      CLOSER          - lowers the range
     *      DIFFERENT_FOOD  - Won't show a restaurant with the same type of food as the current restaurant
     *      CHEAPER         - Won't show a restaurant which is as expensive as the current restaurant
     *      NO_CHANGE       - Doesn't alter the settings but won't show this restaurant again
     *
     * The three first options also adds the current restaurants to the Set of restaurants to filter
     * so that the current restaurant isn't chosen again
     *
     * When the settings has been altered a new restaurant is loaded
     * */
    @Override
    public void showNewRestaurant(@NonNull RandomChoice choice) {
        changeSettings(choice);
        loadRestaurant();
    }

    /** Changes the settings of the randomise settings as explained above
     * */
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
    }

    /** Injects each budget type of the current restaurant into the randomise settings
     * */
    private void setCheaperSettings() {
        for (BudgetType budgetType : restaurant.getBudgetTypes()) {
            randomiseSettings.setCheaperThan(budgetType);
        }
    }

    /** Deletes the restaurant (called when the delete restaurant button is pressed)
     * */
    @Override
    public void deleteCurrentRestaurant() {
        if (restaurantDAO.removeRestaurant(restaurant)) {
            loadRestaurant();
        }
    }

    /** Starts the edit restaurant activity
     * */
    @Override
    public void editCurrentRestaurant() {
        context.startActivity(AndroidUtils.createEditRestaurantActivityIntent(context, restaurant));
    }

    /** changes the favorite status of the restaurant
     * */
    @Override
    public void favoriteRestaurantClicked() {
        restaurant.setFavorite(!restaurant.isFavorite());
        restaurantDAO.setRestaurantFavorite(restaurant);
    }

    @Override
    public void onCloseFragment() {
        restaurantDAO.closeDB();
    }

    /** Show the location of the current restaurant on a map
     * */
    @Override
    public void showRestaurantLocation() {
        context.startActivity(AndroidUtils.createMapActivityIntentWithLatLong(context, restaurant.getLatitude(), restaurant.getLongitude()));
    }
}
