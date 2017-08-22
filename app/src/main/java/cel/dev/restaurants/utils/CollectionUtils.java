package cel.dev.restaurants.utils;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;

/** This class contains a large amount of
 *  methods which helps with dealing with collections
 * */
public class CollectionUtils {


    /** turns a list of enum into an integer array containing each enum entry in the list's ordinal
     * */
    public static int[] enumToIntArr(List<? extends Enum> list) {
        int[] arr = new int[list.size()];
        if (list.size() == 0) {
            return arr;
        }
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i).ordinal();
        }
        return arr;
    }

    /** turns a integer array of ordinal values of enum class clazz into a List of this clazz
     * */
    public static <T extends Enum> List<T> intArrToEnum(int[] arr, Class<T> clazz) throws EnumConstantNotPresentException {
        List<T> list = new ArrayList<>(arr.length);
        if (arr.length == 0) {
            return list;
        }
        T[] enumConstants = clazz.getEnumConstants();
        for (int i = 0; i < arr.length; i++) {
            try {
                list.add(enumConstants[arr[i]]);
            } catch (Exception e) {
                throw new EnumConstantNotPresentException(clazz, "ordinal = " + i);
            }
        }
        return list;
    }


    /** turns an array of Kitchentypes into a String which can be shown
     *  e.g. [KitchenType.CAFE, KITCHENTYPE.STEAK] --> "Café, Steak"
     * */
    public static String kitchenEnumToString(KitchenType[] kitchenTypes, Context context) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < kitchenTypes.length; i++) {
            text.append(context.getString(kitchenTypes[i].getStringResId()));
            if (i + 1 != kitchenTypes.length) {
                text.append(", ");
            }
        }
        return text.toString();
    }

    /** Same function as kitchenEnumToString except for BudgetType
     * */
    public static String budgetEnumToString(BudgetType[] budgetTypes, Context context) {
        StringBuilder text = new StringBuilder();
        BudgetType.sortBudgetType(budgetTypes);
        for (int i = 0; i < budgetTypes.length; i++) {
            text.append(context.getString(BudgetType.budgetTypeToString(budgetTypes[i])));
            if (i + 1 != budgetTypes.length) {
                text.append(", ");
            }
        }
        return text.toString();
    }

    /** return a random entry of the List or null if the list is empty
     * */
    @Nullable
    public static <T> T getRandomEntryIn(List<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(collection.size());
        return collection.get(index);
    }


}
