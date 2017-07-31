package cel.dev.restaurants.Utils;

import java.util.List;

import cel.dev.restaurants.Exceptions.IllegalFoodTypeName;
import cel.dev.restaurants.Model.FoodType;

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
