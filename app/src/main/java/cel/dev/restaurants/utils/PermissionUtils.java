package cel.dev.restaurants.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/** This class contains some constants containing arrays of permissions needed
 *  as well as some permission related helper methods
 * */
public class PermissionUtils {

    public static final String[] CAMERA_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    public static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    /** return true if the application has permission to the permission passed as an argument
     * */
    public static boolean hasPermissionTo(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    /** Returns true if result is PERMISSION_GRANTED
     * */
    public static boolean isPermissionGranted(int result) {
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
