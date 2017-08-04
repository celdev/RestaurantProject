package cel.dev.restaurants.utils;

import android.os.Parcelable;
import android.support.v7.util.SortedList;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtils {

    private static final String TAG = "collu";

    public static Parcelable[] listToParceableArray(List<? extends Parcelable> list) {
        return list.toArray(new Parcelable[list.size()]);
    }

    public static <T> Set<T> arrayToSet(T[] array){
        Set<T> set = new HashSet<>(array.length);
        Collections.addAll(set, array);
        return set;
    }

    public static <T> List<T> parceableToList(Parcelable[] parcelables, Class<T> clazz) {
        Log.d(TAG, "parceableToList: parceable length = " + parcelables.length);
        Log.d(TAG, "parceableToList: parceable ontent = " + Arrays.toString(parcelables));
        List<T> list = new ArrayList<>(parcelables.length);
        for (int i = 0; i < parcelables.length; i++) {
            list.add(i, (T) parcelables[i]);
        }
        return list;
    }

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


}
