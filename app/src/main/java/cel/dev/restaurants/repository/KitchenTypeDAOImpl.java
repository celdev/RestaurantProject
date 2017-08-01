package cel.dev.restaurants.repository;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.FoodType;

public class KitchenTypeDAOImpl implements KitchenTypeDAO {

    private static List<FoodType> foodTypes = new ArrayList<>();

    static {
        foodTypes.add(new FoodType("Café"));
        foodTypes.add(new FoodType("Chinese"));
        foodTypes.add(new FoodType("Fast food"));
        foodTypes.add(new FoodType("German"));
        foodTypes.add(new FoodType("Italian"));
        foodTypes.add(new FoodType("Japanese"));
        foodTypes.add(new FoodType("Market"));
        foodTypes.add(new FoodType("Mexican"));
        foodTypes.add(new FoodType("Pizza"));
        foodTypes.add(new FoodType("Thai"));
        foodTypes.add(new FoodType("Steak"));
        foodTypes.add(new FoodType("Swedish"));

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
