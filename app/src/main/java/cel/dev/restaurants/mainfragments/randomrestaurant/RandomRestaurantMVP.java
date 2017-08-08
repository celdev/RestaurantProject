package cel.dev.restaurants.mainfragments.randomrestaurant;

import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;

public interface RandomRestaurantMVP {

    interface View {

        void showSettingsDialog();

        void handleNoRestaurantsFound();

        void injectRestaurant(Restaurant restaurant, RestaurantDAO restaurantDAO);

    }

    interface Presenter {

        void loadRestaurant();

        void deleteCurrentRestaurant();

        void editCurrentRestaurant();

        void favoriteRestaurantClicked();

        void showRestaurantLocation();

        void resetSettings();

        void setSettings(boolean useLocation, double range, double latitude, double longitude, boolean useFavorite, boolean useBudgetTypes, boolean useKitchenTypes,
                         Set<BudgetType> budgetTypes, Set<KitchenType> kitchenTypes);

        void notThisRestaurantPressed();
    }

}
