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
import android.support.v4.content.ContextCompat;

import cel.dev.restaurants.ShowRestaurantLocationActivity;
import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.model.Restaurant;

public class AndroidUtils {

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
        extras.putInt(CreateRestaurantActivity.EDIT_RESTAURANT_ID, restaurant.getId());
        intent.putExtras(extras);
        return intent;
    }

}
