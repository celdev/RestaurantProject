package cel.dev.restaurants.utils;

import java.util.List;

import cel.dev.restaurants.exceptions.IllegalFoodTypeName;
import cel.dev.restaurants.model.FoodType;

public class ModelConverterUtils {

    public static FoodType stringToFoodType(String name, List<FoodType> all) throws IllegalFoodTypeName {
        for (FoodType foodType : all) {
            if (foodType.getName().equals(name)) {
                return foodType;
            }
        }
        throw new IllegalFoodTypeName();
    }

}
