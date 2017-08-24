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

        /** Returns the image of the ImageView
         *  When the image of the ImageView is set then the instance variable restaurantImage
         *  is also set. restaurantImage will be null if the placeholder image is being used
         *  (in case that the user hasn't taken an image or has deleted the taken image)
         * */
        @Nullable
        Bitmap getImage();

        /** Sets the text of the TextView which rests above the ImageView
         * */
        void setText(String text);

        /** Returns the Image of the ImageView as an drawable
         * */
        Drawable getRestaurantImageDrawable();

        /** Sets the image of the ImageView
         *  also stores this image in the instance variable restaurant image and
         *  show the controllers for rotating and deleting the taken image.
         * */
        void setRestaurantImage(Bitmap image);

        /** Returns the ImageView
         * */
        ImageView getRestaurantImageView();

        /** Returns true if the application has camera permissions
         * */
        boolean hasCameraPermission();

        /** This method will be called when the take photo button is pressed
         *  calls the presenter to handle this event.
         * */
        void takePictureWithCamera();

        /** Request camera permission
         * */
        void requestCameraPermission();

    }

    interface Presenter {

        /** If the app has camera permissions a picture will be taken
         * otherwise the app will ask for camera permission
         * */
        void onTakePictureButtonClicked();


    }

}
