package cel.dev.restaurants.createrestaurant.ImageFragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import cel.dev.restaurants.createrestaurant.CreateRestaurantMVP;
import cel.dev.restaurants.utils.PermissionUtils;
import cel.dev.restaurants.utils.PictureUtils;
import cel.dev.restaurants.utils.Values;

import static android.app.Activity.RESULT_OK;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_restaurant_image, container, false);
        ButterKnife.bind(this, view);
        presenter = new ImageFragmentPresenterImpl(this);
        handleSavedInstanceState(savedInstanceState);
        return view;
    }


    private void handleSavedInstanceState(Bundle savedInstanceState) {
        String key = getString(R.string.bundle_create_restaurant_image);
        if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
            Log.d(TAG, "onCreate: contains image");
            setRestaurantImage(PictureUtils.byteArrayToBitMap(savedInstanceState.getByteArray(key)));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public Bitmap getImage() {
        return restaurantImage;
    }

    @Override
    public void setText(String text) {
        placeHolderWhite.setText(text);
    }

    @Override
    public Drawable getRestaurantImageDrawable() {
        return restaurantImageView.getDrawable();
    }

    @Override
    public void setRestaurantImage(Bitmap image) {
        restaurantImage = image;
        restaurantImageView.setImageBitmap(restaurantImage);
        showImageControls(true);
    }

    @Override
    public boolean hasCameraPermission() {
        return PermissionUtils.hasPermissionTo(getActivity(), Manifest.permission.CAMERA);
    }

    private void showImageControls(boolean show) {
        imageControlLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.rotate_image_btn)
    void rotateImage(View view) {
        setRestaurantImage(PictureUtils.rotateBitmap(restaurantImage, 90));
    }

    @OnClick(R.id.delete_image_btn)
    void deleteImage(View view) {
        restaurantImage = null;
        restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_placeholder, getContext().getTheme()));
        showImageControls(false);
    }

    @OnClick(R.id.take_restaurant_picture_btn)
    void takePicture(View view) {
        presenter.onTakePictureButtonClicked();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            startRestaurantImageCrop(data.getData());
        } else if (requestCode == IMAGE_CROP && resultCode == RESULT_OK) {
            setRestaurantImage((Bitmap) data.getExtras().get("data"));
        }
    }

    @Override
    public void takePictureWithCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
    }

    private void startRestaurantImageCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        PictureUtils.cropIntentWidthAndHeight(intent, getRestaurantImageDrawable().getIntrinsicWidth(),
                getRestaurantImageDrawable().getIntrinsicHeight());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, IMAGE_CROP);
    }

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

    @Override
    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), PermissionUtils.CAMERA_PERMISSIONS, CAMERA_REQUEST);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: called, saving image = " + (restaurantImage != null) );
        if (restaurantImage != null) {
            try {
                outState.putByteArray(getString(R.string.bundle_create_restaurant_image), PictureUtils.bitmapToByteArray(restaurantImage,
                        Values.ON_SAVE_RESTAURANT_IMAGE_COMPRESS_QUALITY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onSaveInstanceState: contains image = " + outState.containsKey(getString(R.string.bundle_create_restaurant_image)));
        }
    }

}
