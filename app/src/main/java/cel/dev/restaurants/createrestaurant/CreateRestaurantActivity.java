package cel.dev.restaurants.createrestaurant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.ShowRestaurantLocationActivity;
import cel.dev.restaurants.choosekitchendialog.ChooseKitchenDialogFragment;
import cel.dev.restaurants.choosekitchendialog.FoodTypeToTextRenderer;
import cel.dev.restaurants.choosekitchendialog.OnChooseKitchenCallback;
import cel.dev.restaurants.createrestaurant.ImageFragment.ImageFragmentMVP;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.R;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.CollectionUtils;
import cel.dev.restaurants.utils.PermissionUtils;
import cel.dev.restaurants.utils.PictureUtils;

public class CreateRestaurantActivity extends AppCompatActivity implements CreateRestaurantMVP.View,
        OnChooseKitchenCallback, OnSuccessListener<Location>{

    private enum ActivityMode {
        NEW, EDIT;
    }

    public static final String EDIT_RESTAURANT_ID = "Edit_Restaurant";
    private static final int REQUEST_LOCATION = 4;

    private static final int LOCATION_ACTIVITY_REQUEST = 5;
    private static final String TAG = "cra";

    @BindView(R.id.restaurant_name_field) EditText nameField;

    @BindView(R.id.chosen_kitchen_text) TextView chosenKitchenText;
    @BindView(R.id.restaurant_score) RatingBar ratingBar;
    @BindView(R.id.budget_cheap) CheckBox budgetCheapBox;
    @BindView(R.id.budget_normal) CheckBox budgetNormalBox;
    @BindView(R.id.budget_expensive) CheckBox budgetExpensiveBox;
    @BindView(R.id.budget_very_expensive) CheckBox budgetVeryExpensiveBox;
    @BindView(R.id.location_info_text) TextView locationInfoText;
    @BindView(R.id.create_restaurant_okBtn) Button createRestaurantButton;

    private ImageFragmentMVP.View imageFragmentView;
    private CreateRestaurantMVP.Presenter presenter;
    private Double[] location;
    private ActivityMode mode = ActivityMode.NEW;
    private ChooseKitchenDialogFragment chooseKitchenDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);
        presenter = new CreateRestaurantPresenterImpl(this);
        imageFragmentView = (ImageFragmentMVP.View) getFragmentManager().findFragmentById(R.id.restaurant_image_fragment);
        if (presenter.getIsEditRestaurantMode(getIntent(), this)) {
            mode = ActivityMode.EDIT;
        }
        initializeViewParameters(savedInstanceState);
        Log.d(TAG, "onCreate: creating activity");
    }

    /**
     * Sets values for view that aren't settable in xml
     */
    private void initializeViewParameters(Bundle savedInstanceState) {
        nameField.addTextChangedListener(new RestaurantNameTextWatcher(imageFragmentView));
        chosenKitchenText.setMaxWidth(chosenKitchenText.getWidth());
        if (mode.equals(ActivityMode.EDIT)) {
            createRestaurantButton.setText(R.string.save);
        }
    }

    @Override
    public void onCancelCreateRestaurantPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                double longitude = extras.getDouble(ShowRestaurantLocationActivity.DATA_LONGITUDE);
                double latitude = extras.getDouble(ShowRestaurantLocationActivity.DATA_LATITUDE);
                setLocationInformation(latitude, longitude);
            } else {
                Toast.makeText(this, R.string.error_getting_location, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, PermissionUtils.LOCATION_PERMISSIONS, REQUEST_LOCATION);
    }

    @Override
    public boolean checkHasLocationPermission() {
        return PermissionUtils.hasPermissionTo(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(grantResults[0])) {
            switch (requestCode) {
                case REQUEST_LOCATION:
                    //todo request location again
                    break;
            }
        } else {
            switch (requestCode) {
                case REQUEST_LOCATION:
                    Toast.makeText(this, R.string.no_location_permission, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void setLocationInformation(double latitude, double longitude) {
        location = new Double[]{latitude, longitude};
        locationInfoText.setText(presenter.getLocationStringFromLatLng(this, latitude, longitude));
    }

    @Override
    public void showSelectKitchenDialog() {
        chooseKitchenDialogFragment = ChooseKitchenDialogFragment.newInstance(presenter.getChosenKitchen());
        chooseKitchenDialogFragment
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
        return imageFragmentView.getImage();
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
        return location;
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

    @Override
    public void showError(@StringRes int errorResCode) {
        Toast.makeText(this, errorResCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void injectInformationToViews(@NonNull Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            imageFragmentView.setRestaurantImage(PictureUtils.byteArrayToBitMap(((RestaurantCustomImage) restaurant).getImageByteArray()));
        }
        nameField.setText(restaurant.getName());
        setLocationInformation(restaurant.getLatitude(), restaurant.getLongitude());
        translateBudgetTypesToCheckedBoxes(restaurant.getBudgetTypes());
        ratingBar.setRating(restaurant.getRating());
        updateChosenKitchens(Arrays.asList(restaurant.getKitchenTypes()));
    }

    private void translateBudgetTypesToCheckedBoxes(BudgetType[] budgetTypes) {
        for (BudgetType budgetType : budgetTypes) {
            switch (budgetType) {
                case CHEAP:
                    budgetCheapBox.setChecked(true);
                    break;
                case NORMAL:
                    budgetNormalBox.setChecked(true);
                    break;
                case EXPENSIVE:
                    budgetExpensiveBox.setChecked(true);
                    break;
                case VERY_EXPENSIVE:
                    budgetVeryExpensiveBox.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            setLocationInformation(location.getLatitude(), location.getLongitude());
        } else {
            Toast.makeText(this, R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.unsaved_information)
                .setMessage(R.string.restaurant_not_saved_yet)
                .setPositiveButton(R.string.stay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateRestaurantActivity.super.onBackPressed();
                    }
                }).create().show();
    }



    /**
     #############################################################################
     #############################################################################
     ########################       ON CLICK              ########################
     #############################################################################
     #############################################################################
     * */
    @OnClick(R.id.choose_kitchenBtn)
    void chooseKitchenClick(View view) {
        presenter.onChooseKitchenBtnPressed();
    }

    @OnClick(R.id.create_restaurant_cancelBtn)
    void cancelCreateRestaurantPressed(View view) {
        finish();
    }

    @OnClick(R.id.create_restaurant_okBtn)
    void okCreateRestaurantPressed(View view) {
        presenter.onCreateRestaurantPressed();
    }

    @OnClick(R.id.use_my_location_button)
    void onUseMyLocationPressed(View view) {
        if (checkHasLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(this)
                    .getLastLocation().addOnSuccessListener(this, this);
        }
    }

    @OnClick(R.id.pick_on_map)
    void onPickOnMapPressed(View view) {
        Intent intent;
        if (location != null) {
            intent = AndroidUtils.createMapActivityIntentWithLatLong(this, location[0], location[1]);
        } else {
            intent =new Intent(this, ShowRestaurantLocationActivity.class);
        }
        startActivityForResult(intent, LOCATION_ACTIVITY_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(getString(R.string.bundle_chosen_kitchen_types), CollectionUtils.enumToIntArr(presenter.getChosenKitchen()));
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putBoolean(getString(R.string.bundle_is_showing_choose_kitchen_dialog),
                AndroidUtils.dialogFragmentIsShowing(chooseKitchenDialogFragment));
        if (location != null) {
            outState.putDoubleArray(getString(R.string.bundle_location), new double[]{location[0], location[1]});
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            List<KitchenType> chosenKitchenTypes = CollectionUtils.intArrToEnum(
                            savedInstanceState.getIntArray(getString(R.string.bundle_chosen_kitchen_types)),KitchenType.class);
            for (KitchenType chosenKitchenType : chosenKitchenTypes) {
                presenter.chooseFoodType(chosenKitchenType, true);
            }
        } catch (EnumConstantNotPresentException e) {
            Log.e(TAG, "onRestoreInstanceState: error restoring enum bundle", e);
        }
        if (savedInstanceState.getBoolean(getString(R.string.bundle_is_showing_choose_kitchen_dialog), false)) {
            showSelectKitchenDialog();
        }
        String key = getString(R.string.bundle_location);
        if(savedInstanceState.containsKey(key)) {
            try {
                double[] loc = savedInstanceState.getDoubleArray(key);
                setLocationInformation(loc[0], loc[1]);
            } catch (Exception e) {
                Log.e(TAG, "onRestoreInstanceState: error restoring location", e);
            }
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}

