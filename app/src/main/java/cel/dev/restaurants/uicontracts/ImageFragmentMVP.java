package cel.dev.restaurants.uicontracts;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/** interface for the view and presenter of the ImageFragment
 *  Mostly contains methods which allows the container activity to
 *  access information in the fragment
 * */
public interface ImageFragmentMVP {

    interface View {

        @Nullable
        Bitmap getImage();

        void setText(String text);

        Drawable getRestaurantImageDrawable();

        void setRestaurantImage(Bitmap image);

        ImageView getRestaurantImageView();

        boolean hasCameraPermission();

        void takePictureWithCamera();

        void requestCameraPermission();


    }

    interface Presenter {

        void onTakePictureButtonClicked();


    }

}
