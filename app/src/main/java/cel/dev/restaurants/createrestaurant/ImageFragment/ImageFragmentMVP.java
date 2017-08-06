package cel.dev.restaurants.createrestaurant.ImageFragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

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
