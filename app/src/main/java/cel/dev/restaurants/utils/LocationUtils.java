package cel.dev.restaurants.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import cel.dev.restaurants.R;
import cel.dev.restaurants.uicontracts.LocationRequestCallback;

/** This class contains some methods for extracting location related functionality from the
 *  fragments and activities.
 * */
public class LocationUtils {

    public static final String TAG = "Loc util";

    /** Shows a dialog which states that this functionality requires location permissions
     *  when the dialog is dismissed it calls to the LocationRequestCallback which can try to request
     *  the permission again
     * */
    public static void showRequireLocationDialog(Context context, final LocationRequestCallback requestCallback) {
        new AlertDialog.Builder(context).setTitle(R.string.require_location)
                .setMessage(R.string.require_location_dialog_text)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestCallback.requestLocationCallback();
                        dialog.dismiss();

                    }
                }).create().show();
    }


    /** Request the location permission
     * */
    public static void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, PermissionUtils.LOCATION_PERMISSIONS, requestCode);
    }


    /** checks if the application has location permission
     * */
    public static boolean checkHasLocationPermission(Context context) {
        return PermissionUtils.hasPermissionTo(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /** requests the location if the user has given location permission
     *  else the request location dialog will be shown
     *  The callbacks for the onSuccess och onComplete events will be set to the listeners passed
     *  as parameters
     * */
    public static void requestLocation(Context context, OnSuccessListener<Location> successCallback,
                                       OnCompleteListener<Location> completeCallback, LocationRequestCallback onRequestCallback) {
        if (checkHasLocationPermission(context)) {
            LocationServices.getFusedLocationProviderClient(context)
                    .getLastLocation()
                    .addOnSuccessListener(successCallback)
                    .addOnCompleteListener(completeCallback);
        } else {
            showRequireLocationDialog(context, onRequestCallback);
        }
    }
}
