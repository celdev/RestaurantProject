package cel.dev.restaurants.Utils;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PictureUtils {



    public static Bitmap scaleBitmap(Bitmap bitmap) {
        //todo
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap src, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

    }

}
