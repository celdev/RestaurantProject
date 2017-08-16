package cel.dev.restaurants.model;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.R;


/** This enum represent different types of food a restaurant can have
 *
 *  It also contains some methods for turning a list of KitchenType into
 *  an integer array which can be added in a bundle
 *
 *  as well as a method to recreate the List of KitchenType from an integer array
 * */
public enum KitchenType {

    Café(R.string.cafe),
    Chinese(R.string.chinese),
    Fast(R.string.fast_food),
    German(R.string.german),
    Italian(R.string.italian),
    Japanese(R.string.japanese),
    Market(R.string.market),
    Mexican(R.string.mexican),
    Pizza(R.string.pizza),
    Thai(R.string.thai),
    Steak(R.string.steak),
    Swedish(R.string.swedish),;

    private int stringResId;

    KitchenType(int stringResId) {
        this.stringResId = stringResId;
    }


    public int getStringResId() {
        return stringResId;
    }

    public static int[] toIntegerArray(List<KitchenType> kitchenTypes) {
        int[] ordinals = new int[kitchenTypes.size()];
        for (int i = 0; i < kitchenTypes.size(); i++) {
            ordinals[i] = kitchenTypes.get(i).ordinal();
        }
        return ordinals;
    }

    public static List<KitchenType> fromParcel(int[] ordinals) {
        List<KitchenType> kitchenTypes = new ArrayList<>(ordinals.length);
        for (Integer ordinal : ordinals) {
            kitchenTypes.add(KitchenType.values()[ordinal]);
        }
        return kitchenTypes;
    }
}
