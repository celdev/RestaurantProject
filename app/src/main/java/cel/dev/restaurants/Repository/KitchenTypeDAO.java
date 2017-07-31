package cel.dev.restaurants.Repository;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;

public interface KitchenTypeDAO {

    void saveKitchenType(FoodType foodType);

    List<FoodType> getAllFoodTypes();

}
