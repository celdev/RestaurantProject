package cel.dev.restaurants.Utils;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class PictureUtils {

    private static final String TAG = "picutil";

    public static Bitmap scaleBitmap(Bitmap bitmap) {
        //todo
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap src, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

    }

    public static Intent cropIntentWidthAndHeight(Intent intent, int width, int height) {
        double scale = 256.0 / width;
        width *= scale;
        height *= scale;
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        Log.d(TAG, "cropIntentWidthAndHeight: height = " + height + " width = " + width);
        return intent;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
        return stream.toByteArray();
    }

}
