package cel.dev.restaurants.choosekitchendialog;

import java.util.List;

import cel.dev.restaurants.model.KitchenType;

public class FoodTypeToTextRenderer {

    public static String foodTypesToString(final List<KitchenType> kitchenTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (KitchenType kitchenType : kitchenTypes) {
            if (!first) {
                stringBuilder.append(", ");
            } else {
                first = false;
            }
            stringBuilder.append(kitchenType.getName());
        }
        return stringBuilder.toString();
    }

}
