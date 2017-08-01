package cel.dev.restaurants.utils;

import android.os.Parcelable;
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

}
