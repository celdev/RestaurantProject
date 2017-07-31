package cel.dev.restaurants.Repository;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.Model.FoodType;

public class KitchenTypeDAOImpl implements KitchenTypeDAO {

    private static List<FoodType> foodTypes = new ArrayList<>();

    static {
        foodTypes.add(new FoodType("Thai"));
        foodTypes.add(new FoodType("Swedish"));
        foodTypes.add(new FoodType("German"));
    }

    @Override
    public void saveKitchenType(FoodType foodType) {
        if (!foodTypes.contains(foodType)) {
            foodTypes.add(foodType);
        }
    }

    @Override
    public List<FoodType> getAllFoodTypes() {
        return foodTypes;
    }
}
