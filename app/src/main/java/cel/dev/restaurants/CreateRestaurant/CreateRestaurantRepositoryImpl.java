package cel.dev.restaurants.CreateRestaurant;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.Repository.KitchenTypeDAO;
import cel.dev.restaurants.Repository.KitchenTypeDAOImpl;

class CreateRestaurantRepositoryImpl implements CreateRestaurantMVP.Repository {

    private CreateRestaurantPresenterImpl presenter;
    private KitchenTypeDAO kitchenTypeDAO;

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
}
