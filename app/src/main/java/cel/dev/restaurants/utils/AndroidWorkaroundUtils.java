package cel.dev.restaurants.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;

public class AndroidWorkaroundUtils {

    public static Drawable tintDrawable(Context context, Drawable drawable, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColorFilter(context.getResources().getColor(color, context.getTheme()), PorterDuff.Mode.DST_IN);
        } else {
            drawable.setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.DST_OVER);
        }
        return drawable;
    }

}
