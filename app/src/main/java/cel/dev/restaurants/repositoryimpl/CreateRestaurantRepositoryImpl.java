package cel.dev.restaurants.repositoryimpl;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.persistanceimpl.RestaurantDAOImpl;
import cel.dev.restaurants.uicontracts.CreateRestaurantMVP;

/** This implementation will
 *  relieve the presenter of some of the "store data"-responsibilities
 * */
public class CreateRestaurantRepositoryImpl implements CreateRestaurantMVP.Repository {

    private List<KitchenType> chosenTypes = new ArrayList<>();

    private RestaurantDAO restaurantDAO;

    /** Creates the restaurantDAO object which will communicate with the database
     * */
    public CreateRestaurantRepositoryImpl(Context context) {
        restaurantDAO = new RestaurantDAOImpl(context);
    }

    /** Returns the all KitchenTypes as a list
     * */
    @Override
    public List<KitchenType> getKitchenTypes() {
        return Arrays.asList(KitchenType.values());
    }


    /** return the chosen kitchen types
     * */
    @Override
    public List<KitchenType> chosenFoodTypes() {
        return chosenTypes;
    }

    /** If chosen is true
     *  adds the Kitchen type if it isn't already added
     *  if false
     *  tries to remove the KitchenType from the list of chosen types
     * */
    @Override
    public void chooseFoodType(KitchenType kitchenType, boolean chosen) {
        if (!chosenTypes.contains(kitchenType) && chosen) {
            chosenTypes.add(kitchenType);
        } else if(!chosen) {
            chosenTypes.remove(kitchenType);
        }
    }

    /** Return a restaurant with the id passed as a parameter
     *  will return null if there isn't a restaurant with this id
     * */
    @Override
    @Nullable
    public Restaurant getRestaurant(long id) {
        return restaurantDAO.getRestaurantById(id);
    }

    /** adds all KitchenTypes from the parameter array to the List of chosen KitchenTypes
     * */
    @Override
    public void setChosenFoodTypes(KitchenType[] kitchenTypes) {
        chosenTypes.addAll(Arrays.asList(kitchenTypes));
    }


    /** Retrieves the image of the restaurant and adds it onto the ImageView
     * */
    @Override
    public void injectImageOntoImageView(ImageView imageView, Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            restaurant.injectImageOntoImageView(imageView, restaurantDAO);
        }
    }

    /** Calls the DAO to close the database
     * */
    @Override
    public void onClose() {
        restaurantDAO.closeDB();
    }
}
