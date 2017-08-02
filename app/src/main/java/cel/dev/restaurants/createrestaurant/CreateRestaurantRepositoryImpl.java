package cel.dev.restaurants.createrestaurant;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.repository.KitchenTypeDAO;
import cel.dev.restaurants.repository.KitchenTypeDAOImpl;

class CreateRestaurantRepositoryImpl implements CreateRestaurantMVP.Repository {

    private CreateRestaurantPresenterImpl presenter;
    private KitchenTypeDAO kitchenTypeDAO;

    private List<KitchenType> chosenTypes = new ArrayList<>();

    CreateRestaurantRepositoryImpl(CreateRestaurantPresenterImpl createRestaurantPresenter) {
        this.presenter = createRestaurantPresenter;
        kitchenTypeDAO = new KitchenTypeDAOImpl();
    }

    @Override
    public List<KitchenType> getKitchenTypes() {
        return kitchenTypeDAO.getAllFoodTypes();
    }


    @Override
    public List<KitchenType> chosenFoodTypes() {
        Log.d("rep", "chosenFoodTypes: current chosen foodtypes = " + Arrays.toString(chosenTypes.toArray()));
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
}
