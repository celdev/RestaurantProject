package cel.dev.restaurants.choosekitchendialog;

import android.content.Context;

import java.util.List;

import cel.dev.restaurants.model.KitchenType;

public class FoodTypeToTextRenderer {

    public static String foodTypesToString(Context context, final List<KitchenType> kitchenTypes) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < kitchenTypes.size(); i++) {
            text.append(context.getString(kitchenTypes.get(i).getStringResId()));
            if (i + 1 != kitchenTypes.size()) {
                text.append(", ");
            }
        }
        return text.toString();
    }

}
