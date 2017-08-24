package cel.dev.restaurants.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;
import cel.dev.restaurants.presenterimpl.ImageFragmentPresenterImpl;
import cel.dev.restaurants.uicontracts.ImageFragmentMVP;
import cel.dev.restaurants.utils.PermissionUtils;
import cel.dev.restaurants.utils.PictureUtils;
import cel.dev.restaurants.utils.Values;

import static android.app.Activity.RESULT_OK;

/** This fragment contains the image of the restaurant
 *  as well as the controlls for taking a picture, rotating it and deleting it.
 *
 * */
public class CreateRestaurantImageFragment extends Fragment implements ImageFragmentMVP.View {

    public static final String TAG = "image frag";

    private static final int IMAGE_CROP = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 2;

    @BindView(R.id.restaurantNameWhitePlaceholder) TextView placeHolderWhite;
    @BindView(R.id.restaurant_image) ImageView restaurantImageView;
    @BindView(R.id.image_control_layout) LinearLayout imageControlLayout;

    private Bitmap restaurantImage;
    private ImageFragmentMVP.Presenter presenter;

    public CreateRestaurantImageFragment(){}


    /** inflates the fragment_create_restaurant_image layout and binds Butterknife to this
     *  so that the above @BindView works.
     *
     *  Handles savedInstance in order to retrieve a previously taken image after an orientation change
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_restaurant_image, container, false);
        ButterKnife.bind(this, view);
        presenter = new ImageFragmentPresenterImpl(this);
        handleSavedInstanceState(savedInstanceState);
        return view;
    }


    /** If there is a savedInstanceState and it contains the image key
     *  then the savedInstanceState contains an image stored as a byte-array
     * */
    private void handleSavedInstanceState(Bundle savedInstanceState) {
        String key = getString(R.string.bundle_create_restaurant_image);
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            setRestaurantImage(PictureUtils.byteArrayToBitMap(savedInstanceState.getByteArray(key)));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /** Returns the image of the ImageView
     *  When the image of the ImageView is set then the instance variable restaurantImage
     *  is also set. restaurantImage will be null if the placeholder image is being used
     *  (in case that the user hasn't taken an image or has deleted the taken image)
     * */
    @Nullable
    @Override
    public Bitmap getImage() {
        return restaurantImage;
    }

    /** Sets the text of the TextView which rests above the ImageView
     * */
    @Override
    public void setText(String text) {
        placeHolderWhite.setText(text);
    }

    /** Returns the Image of the ImageView as an drawable
     * */
    @Override
    public Drawable getRestaurantImageDrawable() {
        return restaurantImageView.getDrawable();
    }

    /** Sets the image of the ImageView
     *  also stores this image in the instance variable restaurant image and
     *  show the controllers for rotating and deleting the taken image.
     * */
    @Override
    public void setRestaurantImage(Bitmap image) {
        restaurantImage = image;
        restaurantImageView.setImageBitmap(restaurantImage);
        showImageControls(true);
    }

    /** Returns the ImageView
     * */
    @Override
    public ImageView getRestaurantImageView() {
        return restaurantImageView;
    }

    /** Returns true if the application has camera permissions
     * */
    @Override
    public boolean hasCameraPermission() {
        return PermissionUtils.hasPermissionTo(getActivity(), Manifest.permission.CAMERA);
    }

    /** Show/hides the image control layout, containing controlls for rotating the image
     *  and deleting the image
     *
     *  @param show determine if the layout should be hidden or shown
     * */
    private void showImageControls(boolean show) {
        imageControlLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    /** this method is called when the rotate image button is pressed, rotates
     *  the image by 90 degrees and sets the resulting image as the image of the ImageView
     * */
    @OnClick(R.id.rotate_image_btn)
    void rotateImage(View view) {
        setRestaurantImage(PictureUtils.rotateBitmap(restaurantImage, 90));
    }

    /** Deletes the image and hides the image controls
     *
     * Depending on the android version, uses different setImageDrawable-methods since
     * the getDrawable(int) is deprecated in newer versions of android
     * */
    @OnClick(R.id.delete_image_btn)
    void deleteImage(View view) {
        restaurantImage = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_placeholder, getContext().getTheme()));
        } else {
            restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_placeholder));
        }
        showImageControls(false);
    }

    /** This method will be called when the take photo button is pressed
     *  calls the presenter to handle this event.
     * */
    @OnClick(R.id.take_restaurant_picture_btn)
    void takePicture(View view) {
        presenter.onTakePictureButtonClicked();
    }


    /** This method is called when this fragment receives a result from another activity
     *  If the requestCode is the REQUEST_IMAGE_CAPTURE and the result is ok
     *  that means that the result contains an image which has to be cropped in order to be saved later
     *
     *  if the requestCode is IMAGE_CROP and the result is ok
     *  then it means that the result contains an cropped image which can be displayed in the ImageView
     *  and saved later.
     *
     *  If the resultCode isn't RESULT_OK in either case a Toast containing an error message
     *  will be shown.
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                startRestaurantImageCrop(data.getData());
            } else {
                Toast.makeText(getActivity(),R.string.error_taking_picture, Toast.LENGTH_LONG).show();
            }
        } else {
            if (requestCode == IMAGE_CROP ) {
                if (resultCode == RESULT_OK) {
                    setRestaurantImage((Bitmap) data.getExtras().get("data"));
                } else {
                    Toast.makeText(getActivity(),R.string.error_croping_image, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /** launches an implicit Intent for taking a picture, requesting the result
     *  using the REQUEST_IMAGE_CAPUTRE requestCode.
     * */
    @Override
    public void takePictureWithCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
    }

    /** Creates the Camera crop intent and adds the image to the intent
     *  and starts the intent asking for a result
     *  the height and width of the result is injected using the cropIntentWidthAndHeight static method
     *  of the PictureUtils-class which scales the width and height into a dimension which won't cause
     *  OutOfMemory-exceptions since the maximum size of the image which can be passed around in the intent
     *  is quite small
     * */
    private void startRestaurantImageCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        PictureUtils.cropIntentWidthAndHeight(intent, getRestaurantImageDrawable().getIntrinsicWidth(),
                getRestaurantImageDrawable().getIntrinsicHeight());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, IMAGE_CROP);
    }

    /** Handles the request permission results
     * if the result is OK and it's a camera request the "take picture"-button is programmatically pressed
     * if the result is not ok and it's the camera request a Toast is shown stating that the application needs
     * camera permission in order to take a picture for the restaurant
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(grantResults[0])) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    presenter.onTakePictureButtonClicked();
                    break;
            }
        } else {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    Toast.makeText(getActivity(), R.string.no_camera_permission, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /** Request camera permission
     * */
    @Override
    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), PermissionUtils.CAMERA_PERMISSIONS, CAMERA_REQUEST);
    }


    /** Tries to save the image as a byte array in the bundle
     * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (restaurantImage != null) {
            try {
                outState.putByteArray(getString(R.string.bundle_create_restaurant_image), PictureUtils.bitmapToByteArray(restaurantImage,
                        Values.ON_SAVE_RESTAURANT_IMAGE_COMPRESS_QUALITY));
            } catch (Exception e) {
                Log.e(TAG, "onSaveInstanceState: ", e);
            }
        }
    }

}
