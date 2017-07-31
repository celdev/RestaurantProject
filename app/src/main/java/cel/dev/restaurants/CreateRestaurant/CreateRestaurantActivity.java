package cel.dev.restaurants.CreateRestaurant;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.ChooseKitchenDialog.ChooseKitchenDialogFragment;
import cel.dev.restaurants.ChooseKitchenDialog.FoodTypeToTextRenderer;
import cel.dev.restaurants.ChooseKitchenDialog.OnChooseKitchenCallback;
import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.R;
import cel.dev.restaurants.Utils.PermissionUtils;
import cel.dev.restaurants.Utils.PictureUtils;

public class CreateRestaurantActivity extends AppCompatActivity implements CreateRestaurantMVP.View, OnChooseKitchenCallback {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final String TAG = "cra";

    @BindView(R.id.restaurant_name_field)
    EditText nameField;

    @BindView(R.id.restaurantNameWhitePlaceholder)
    TextView placeHolderWhite;

    @BindView(R.id.restaurant_image)
    ImageView restaurantImageView;

    @BindView(R.id.image_control_layout)
    LinearLayout imageControlLayout;

    @BindView(R.id.chosen_kitchen_text)
    TextView chosenKitchenText;

    private CreateRestaurantMVP.Presenter presenter;

    private Bitmap restaurantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);
        presenter = new CreateRestaurantPresenterImpl(this);
        nameField.addTextChangedListener(new RestaurantNameTextWatcher(placeHolderWhite));
    }


    @OnClick(R.id.take_restaurant_picture_btn)
    void takePicture(View view) {
        presenter.cameraButtonPressed();
    }

    @Override
    public void takePictureWithCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void finishCreateRestaurant() {

    }

    @Override
    public void onCancelCreateRestaurantPressed() {

    }

    @Override
    public void onOkCreateRestaurantPressed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setRestaurantImage((Bitmap) data.getExtras().get("data"));
        }
    }

    @Override
    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, PermissionUtils.CAMERA_PERMISSIONS, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST && PermissionUtils.isPermissionGranted(grantResults[0])) {
            presenter.cameraButtonPressed();
        } else {
            Toast.makeText(this, R.string.no_camera_permission, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean hasCameraPermissions() {
        return PermissionUtils.hasPermissionTo(this, Manifest.permission.CAMERA);
    }

    private void setRestaurantImage(Bitmap image) {
        restaurantImage = image;
        restaurantImageView.setImageBitmap(restaurantImage);
        showImageControls(true);
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
        restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_placeholder, getTheme()));
        showImageControls(false);
    }

    @OnClick(R.id.choose_kitchenBtn)
    void chooseKitchenClick(View view) {
        presenter.onChooseKitchenBtnPressed();
    }

    @Override
    public void showSelectKitchenDialog() {
        ChooseKitchenDialogFragment.newInstance(presenter.getKitchens(), presenter.getChosenKitchen())
                .show(getSupportFragmentManager(), "fragment_choose_kitchen");
    }

    @Override
    public void chooseKitchen(FoodType foodType, boolean chosen) {
        presenter.chooseFoodType(foodType,chosen);
    }

    @Override
    public void updateChosenKitchens(List<FoodType> foodTypes) {
        if (foodTypes.size() == 0) {
            chosenKitchenText.setText(R.string.no_kitchen_chosen);
        } else {
            chosenKitchenText.setText(FoodTypeToTextRenderer.foodTypesToString(foodTypes));
        }
    }


}

