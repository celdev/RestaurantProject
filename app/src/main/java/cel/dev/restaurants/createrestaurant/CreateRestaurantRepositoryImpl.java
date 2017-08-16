package cel.dev.restaurants.createrestaurant;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;

/** This implementation will
 *  relieve the presenter of some of the "store data"-responsibilities
 * */
class CreateRestaurantRepositoryImpl implements CreateRestaurantMVP.Repository {

    private List<KitchenType> chosenTypes = new ArrayList<>();

    private RestaurantDAO restaurantDAO;

    CreateRestaurantRepositoryImpl(Context context) {
        restaurantDAO = new RestaurantDAOImpl(context);
    }

    @Override
    public List<KitchenType> getKitchenTypes() {
        return Arrays.asList(KitchenType.values());
    }


    @Override
    public List<KitchenType> chosenFoodTypes() {
        return chosenTypes;
    }

    @Override
    public void chooseFoodType(KitchenType kitchenType, boolean chosen) {
        if (!chosenTypes.contains(kitchenType) && chosen) {
            chosenTypes.add(kitchenType);
        } else if(!chosen) {
            chosenTypes.remove(kitchenType);
        }
    }

    @Override
    @Nullable
    public Restaurant getRestaurant(long id) {
        return restaurantDAO.getRestaurantById(id);
    }

    @Override
    public void setChosenFoodTypes(KitchenType[] kitchenTypes) {
        chosenTypes.addAll(Arrays.asList(kitchenTypes));
    }

    @Override
    public void injectImageOntoImageView(ImageView imageView, Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            restaurant.injectImageOntoImageView(imageView, restaurantDAO);
        }
    }
}
