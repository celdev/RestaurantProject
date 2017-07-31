package cel.dev.restaurants.ChooseKitchenDialog;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;

public class FoodTypeToTextRenderer {

    public static String foodTypesToString(final List<FoodType> foodTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (FoodType foodType : foodTypes) {
            if (!first) {
                stringBuilder.append(", ");
            } else {
                first = false;
            }
            stringBuilder.append(foodType.getName());
        }
        return stringBuilder.toString();
    }

}
