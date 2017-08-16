package cel.dev.restaurants.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.showrestaurantlocationactivity.ShowRestaurantLocationActivity;
import cel.dev.restaurants.choosekitchendialog.ChooseKitchenDialogFragmentMVP;
import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.model.Restaurant;

/** This class contains a large amount of methods which provides some functionality which
 *  would clutter the fragments and activities of this application
 * */
public class AndroidUtils {

    public static final String TAG = "android utils";

    /** Tints the color of the drawable
     * */
    public static Drawable tintDrawable(Context context, Drawable drawable, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColorFilter(context.getResources().getColor(color, context.getTheme()), PorterDuff.Mode.SRC_IN);
        } else {
            drawable.setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC_IN);
        }
        return drawable;
    }

    /** returns true if the activity was started using startActivityForResult()
     * */
    public static boolean activityStartedForResult(Activity activity) {
        return activity.getCallingActivity() != null;
    }

    /** creates an Intent containing latitude and longitude
     * */
    public static Intent createMapActivityIntentWithLatLong(Context context, double latitude, double longitude) {
        Intent intent = new Intent(context, ShowRestaurantLocationActivity.class);
        Bundle extras = new Bundle();
        extras.putDouble(ShowRestaurantLocationActivity.DATA_LATITUDE, latitude);
        extras.putDouble(ShowRestaurantLocationActivity.DATA_LONGITUDE, longitude);
        intent.putExtras(extras);
        return intent;
    }

    /** creates an Intent with the id of the restaurant to edit
     * */
    public static Intent createEditRestaurantActivityIntent(Context context, Restaurant restaurant) {
        Intent intent = new Intent(context, CreateRestaurantActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(CreateRestaurantActivity.EDIT_RESTAURANT_ID, restaurant.getId());
        intent.putExtras(extras);
        return intent;
    }

    /** returns true if the dialog fragment isn't null and the dialog is showing (using the HasShownStatus interface)
     * */
    public static boolean dialogFragmentIsShowing(@Nullable ChooseKitchenDialogFragmentMVP.HasShownStatus dialogFragment) {
        return dialogFragment != null && dialogFragment.getDialogIsShowing();
    }

    /** drawable res to drawable
     * */
    public static Drawable drawableResToDrawable(@DrawableRes int drawableRes, Context context) {
        return context.getDrawable(drawableRes);
    }


    /** Show a progressdialog
     * */
    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int title,
                                                      boolean cancleable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(cancleable);
        return progressDialog;
    }

    public static void showAboutDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.about_dialog_title)
                .setView(R.layout.about_dialog)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /** Utils for turning a List<Enum> into a format which allows it to be stored in the database
     *
     *  E.g. a list containing the enums enum.A, enum.B, enum.C
     *  can be converted into a comma separated string containing their ordinals
     *  The resulting string would be
     *  "0,1,2"
     *  this can then be used to create a List of enum
     *  by splitting the string on comma and adding the enum constant with each ordinal
     * */
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
