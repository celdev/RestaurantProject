package cel.dev.restaurants.repository;

import java.util.List;

import cel.dev.restaurants.model.FoodType;

public interface KitchenTypeDAO {

    void saveKitchenType(FoodType foodType);

    List<FoodType> getAllFoodTypes();

}
