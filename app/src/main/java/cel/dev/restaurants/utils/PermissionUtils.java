package cel.dev.restaurants.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    public static final String[] CAMERA_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    public static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    public static boolean hasPermissionTo(Activity activity, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, permission);
    }

    public static boolean isPermissionGranted(int result) {
        return result == PackageManager.PERMISSION_GRANTED;
    }

}