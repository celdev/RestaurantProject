package cel.dev.restaurants.createrestaurant;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.choosekitchendialog.ChooseKitchenDialogFragment;
import cel.dev.restaurants.choosekitchendialog.FoodTypeToTextRenderer;
import cel.dev.restaurants.choosekitchendialog.OnChooseKitchenCallback;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.R;
import cel.dev.restaurants.utils.PermissionUtils;
import cel.dev.restaurants.utils.PictureUtils;

public class CreateRestaurantActivity extends AppCompatActivity implements CreateRestaurantMVP.View, OnChooseKitchenCallback, OnMapReadyCallback {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOCATION = 4;
    private static final int CAMERA_REQUEST = 2;
    private static final int IMAGE_CROP = 3;
    private static final String TAG = "cra";

    @BindView(R.id.restaurant_name_field) EditText nameField;
    @BindView(R.id.restaurantNameWhitePlaceholder) TextView placeHolderWhite;
    @BindView(R.id.restaurant_image) ImageView restaurantImageView;
    @BindView(R.id.image_control_layout) LinearLayout imageControlLayout;
    @BindView(R.id.chosen_kitchen_text) TextView chosenKitchenText;
    @BindView(R.id.restaurant_score) RatingBar ratingBar;
    @BindView(R.id.budget_cheap) CheckBox budgetCheapBox;
    @BindView(R.id.budget_normal) CheckBox budgetNormalBox;
    @BindView(R.id.budget_expensive) CheckBox budgetExpensiveBox;
    @BindView(R.id.budget_very_expensive) CheckBox budgetVeryExpensiveBox;
    @BindView(R.id.map_view) MapView mapView;

    private GoogleMap map;
    private CreateRestaurantMVP.Presenter presenter;

    private Bitmap restaurantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);
        presenter = new CreateRestaurantPresenterImpl(this);
        nameField.addTextChangedListener(new RestaurantNameTextWatcher(placeHolderWhite));
        initializeViewParameters(savedInstanceState);
    }

    /** Sets values for view that aren't settable in xml
     * */
    private void initializeViewParameters(Bundle savedInstanceState) {
        chosenKitchenText.setMaxWidth(chosenKitchenText.getWidth());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
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
            startRestaurantImageCrop(data.getData());
        } else if (requestCode == IMAGE_CROP && resultCode == RESULT_OK) {
            setRestaurantImage((Bitmap) data.getExtras().get("data"));
        }
    }

    private void startRestaurantImageCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("crop", "true");
        PictureUtils.cropIntentWidthAndHeight(intent, restaurantImageView.getDrawable().getIntrinsicWidth(), restaurantImageView.getDrawable().getIntrinsicHeight());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, IMAGE_CROP);
    }

    @Override
    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, PermissionUtils.CAMERA_PERMISSIONS, CAMERA_REQUEST);
    }

    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, PermissionUtils.LOCATION_PERMISSIONS, REQUEST_LOCATION);
    }

    @Override
    public boolean hasLocationPermission() {
        return PermissionUtils.hasPermissionTo(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(grantResults[0])) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    presenter.cameraButtonPressed();
                    break;
                case REQUEST_LOCATION:
                    onReceiveLocationPermission();
                    break;
            }
        } else {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    Toast.makeText(this, R.string.no_camera_permission, Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_LOCATION:
                    Toast.makeText(this, R.string.no_location_permission, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void onReceiveLocationPermission() {

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

    /** ONCLICK actions
     *
     * */

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

    @OnClick(R.id.take_restaurant_picture_btn)
    void takePicture(View view) {
        presenter.cameraButtonPressed();
    }

    @OnClick(R.id.create_restaurant_cancelBtn)
    void cancelCreateRestaurantPressed(View view) {
        finish();
    }

    @OnClick(R.id.create_restaurant_okBtn)
    void okCreateRestaurantPressed(View view) {
        presenter.onCreateRestaurantPressed();
    }


    /***************************************************************************************************/

    @Override
    public void showSelectKitchenDialog() {
        ChooseKitchenDialogFragment.newInstance(presenter.getChosenKitchen())
                .show(getSupportFragmentManager(), "fragment_choose_kitchen");
    }

    @Override
    public void chooseKitchen(KitchenType kitchenType, boolean chosen) {
        presenter.chooseFoodType(kitchenType,chosen);
    }

    @Override
    public void updateChosenKitchens(List<KitchenType> kitchenTypes) {
        if (kitchenTypes.size() == 0) {
            chosenKitchenText.setText(R.string.no_kitchen_chosen);
        } else {
            chosenKitchenText.setText(FoodTypeToTextRenderer.foodTypesToString(this, kitchenTypes));
        }
    }

    @Override
    public Bitmap getRestaurantImage() {
        return restaurantImage;
    }

    @Nullable
    @Override
    public String getRestaurantName() {
        return nameField.getText().toString();
    }


    @Override
    public float getRestaurantRating() {
        return ratingBar.getRating();
    }

    /** converts checked checkboxes into BudgetType objects
     *  if no budgettype is chosen null will be returned
     * */
    @Nullable
    @Override
    public BudgetType[] getSelectedBudgetTypes() {
        List<BudgetType> budgetTypes = new ArrayList<>();
        if (budgetCheapBox.isChecked()) {
            budgetTypes.add(BudgetType.CHEAP);
        }
        if (budgetNormalBox.isChecked()) {
            budgetTypes.add(BudgetType.NORMAL);
        }
        if (budgetExpensiveBox.isChecked()) {
            budgetTypes.add(BudgetType.EXPENSIVE);
        }
        if (budgetVeryExpensiveBox.isChecked()) {
            budgetTypes.add(BudgetType.VERY_EXPENSIVE);
        }
        if (budgetTypes.size() == 0) {
            return null;
        }
        return budgetTypes.toArray(new BudgetType[budgetTypes.size()]);
    }

    @Override
    public Double[] getPosition() {
        Double[] temp = new Double[2];
        temp[0] = 1.0;
        temp[1] = 2.0;
        return temp;
    }


    /** Creates and shows a dialog containing the error message
     * */
    @Override
    public void createRestaurantError(int validationMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_creating_restaurant)
                .setMessage(validationMessage)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public void createRestaurantOk() {
        finish();
    }


    /** Callback for the initialize async Google map*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
    }


    /** Android lifecycle methods
     * */

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

