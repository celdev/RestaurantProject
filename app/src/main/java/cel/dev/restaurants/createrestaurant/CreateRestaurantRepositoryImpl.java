package cel.dev.restaurants.createrestaurant;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.FoodType;
import cel.dev.restaurants.repository.KitchenTypeDAO;
import cel.dev.restaurants.repository.KitchenTypeDAOImpl;

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
