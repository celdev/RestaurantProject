package cel.dev.restaurants.utils;

import java.util.List;

import cel.dev.restaurants.exceptions.IllegalFoodTypeName;
import cel.dev.restaurants.model.KitchenType;

public class ModelConverterUtils {

    public static KitchenType stringToFoodType(String name, List<KitchenType> all) throws IllegalFoodTypeName {
        for (KitchenType kitchenType : all) {
            if (kitchenType.getName().equals(name)) {
                return kitchenType;
            }
        }
        throw new IllegalFoodTypeName();
    }

}
