package cel.dev.restaurants.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.R;

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
