package cel.dev.restaurants.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/** This class contains some util methods related to pictures in the application
 * */
public class PictureUtils {

    /** turns a byte array into a Bitmap
     * */
    public static Bitmap byteArrayToBitMap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /** rotate the Bitmap by angle degrees
     * */
    public static Bitmap rotateBitmap(Bitmap src, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

    }

    /** injects the output and aspect X and Y values into a crop intent
     *  by scaling the height and width so that
     *  out of memory exceptions won't be cause by the image being to big to
     *  return as a result
     * */
    public static Intent cropIntentWidthAndHeight(Intent intent, int width, int height) {
        double scale = 256.0 / width;
        width *= scale;
        height *= scale;
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        return intent;
    }

    /** Turns a bitmap into a byte array (compressed as PNG)
     * */
    public static byte[] bitmapToByteArray(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
        return stream.toByteArray();
    }

}
