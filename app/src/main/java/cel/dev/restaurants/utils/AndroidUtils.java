package cel.dev.restaurants.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.ShowRestaurantLocationActivity;
import cel.dev.restaurants.choosekitchendialog.ChooseKitchenDialogFragmentMVP;
import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.model.Restaurant;

public class AndroidUtils {

    public static final String TAG = "android utils";

    public static Drawable tintDrawable(Context context, Drawable drawable, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColorFilter(context.getResources().getColor(color, context.getTheme()), PorterDuff.Mode.DST_IN);
        } else {
            drawable.setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.DST_OVER);
        }
        return drawable;
    }

    public static boolean activityStartedForResult(Activity activity) {
        return activity.getCallingActivity() != null;
    }

    public static Intent createMapActivityIntentWithLatLong(Context context, double latitude, double longitude) {
        Intent intent = new Intent(context, ShowRestaurantLocationActivity.class);
        Bundle extras = new Bundle();
        extras.putDouble(ShowRestaurantLocationActivity.DATA_LATITUDE, latitude);
        extras.putDouble(ShowRestaurantLocationActivity.DATA_LONGITUDE, longitude);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createEditRestaurantActivityIntent(Context context, Restaurant restaurant) {
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(CreateRestaurantActivity.EDIT_RESTAURANT_ID, restaurant.getId());
        intent.putExtras(extras);
        return intent;
    }

    public static boolean dialogFragmentIsShowing(@Nullable ChooseKitchenDialogFragmentMVP.HasShownStatus dialogFragment) {
        return dialogFragment != null && dialogFragment.getDialogIsShowing();
    }


    public static class DBUtils {

        public static <T extends Enum> String enumsToString(T[] enums) {
            String ret = "";
            for (int i = 0; i < enums.length; i++) {
                ret += enums[i].ordinal();
                if (i != enums.length - 1) {
                    ret += ",";
                }
            }
            return ret;
        }

        public static <T extends Enum> List<T> stringToEnum(String enumString, Class<T> clazz) throws Exception {
            List<T> list = new ArrayList<>();
            String[] split = enumString.split(",");
            T[] enumConstants = clazz.getEnumConstants();
            for (String s : split) {
                int i = Integer.valueOf(s);
                list.add(enumConstants[i]);
            }
            return list;
        }

    }

}
