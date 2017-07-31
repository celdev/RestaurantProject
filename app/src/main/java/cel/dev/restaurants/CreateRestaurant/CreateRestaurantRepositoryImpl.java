package cel.dev.restaurants.CreateRestaurant;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.Exceptions.IllegalFoodTypeName;
import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.Repository.KitchenTypeDAO;
import cel.dev.restaurants.Repository.KitchenTypeDAOImpl;
import cel.dev.restaurants.Utils.ModelConverterUtils;

class CreateRestaurantRepositoryImpl implements CreateRestaurantMVP.Repository {

    private CreateRestaurantPresenterImpl presenter;
    private KitchenTypeDAO kitchenTypeDAO;

    private List<FoodType> chosenTypes = new ArrayList<>();

    CreateRestaurantRepositoryImpl(CreateRestaurantPresenterImpl createRestaurantPresenter) {
        this.presenter = createRestaurantPresenter;
        kitchenTypeDAO = new KitchenTypeDAOImpl();
    }

    @Override
    public List<FoodType> getKitchenTypes() {
        return kitchenTypeDAO.getAllFoodTypes();
    }

    @Override
    public void saveKitchenType(String name) {
        FoodType foodType = new FoodType(name);
        kitchenTypeDAO.saveKitchenType(foodType);
    }

    @Override
    public List<FoodType> chosenFoodTypes() {
        return chosenTypes;
    }

    @Override
    public void chooseFoodType(FoodType foodType, boolean chosen) {
        if (!chosenTypes.contains(foodType) && chosen) {
            chosenTypes.add(foodType);
        } else if(!chosen) {
            chosenTypes.remove(foodType);
        }
    }
}
