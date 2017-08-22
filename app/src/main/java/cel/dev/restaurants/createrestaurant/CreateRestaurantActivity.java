package cel.dev.restaurants.createrestaurant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import cel.dev.restaurants.showrestaurantlocationactivity.ShowRestaurantLocationActivity;
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

/** This activity is the activity which allows the user to add and edit restaurants
 *  in this application
 *
 *  The function (new restaurant or edit restaurant) of this activity is determined by
 *  if the intent used when starting this activity contains the EDIT-key
 *  if the mode is edit mode then the view is populated with the information for the restaurant
 *  with the id passed in the bundle.
 * */
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
    @BindView(R.id.use_my_location_button) Button useMyLocationButton;

    private ImageFragmentMVP.View imageFragmentView;
    private CreateRestaurantMVP.Presenter presenter;
    private Double[] location;
    private ActivityMode mode = ActivityMode.NEW;
    private ChooseKitchenDialogFragment chooseKitchenDialogFragment;
    private AlertDialog cancelDialog;

    private boolean menuInflated;

    /** Creates and handles the options menu containing the menu item for the about dialog
     *  uses a flag (menuInflated) in order to determine if the menu is already inflated
     *  since this method is called each time the menu is opened
     * */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!menuInflated) {
            getMenuInflater().inflate(R.menu.app_bar_menu_no_delete, menu);
            menuInflated = true;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.context_about_app) {
            AndroidUtils.showAboutDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);
        presenter = new CreateRestaurantPresenterImpl(this, this);
        imageFragmentView = (ImageFragmentMVP.View) getFragmentManager().findFragmentById(R.id.restaurant_image_fragment);
        if (presenter.getIsEditRestaurantMode(getIntent(), this)) {
            mode = ActivityMode.EDIT;
        }
        initializeViewParameters(savedInstanceState);
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

    /** When the custom Location activity returns a value this method will retrieve the values
     *  and use it to save the location information
     * */
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

    /** If request of the location is ok then the use my location button is pressed programmatically
     *  otherwise shows a Toast that states that the application needs location permissions to function
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(grantResults[0])) {
            switch (requestCode) {
                case REQUEST_LOCATION:
                    useMyLocationButton.performClick();
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

    /** uses the latitude and longitude to get the address of the location
     *  and sets the locationInfoText text to the address
     * */
    private void setLocationInformation(double latitude, double longitude) {
        location = new Double[]{latitude, longitude};
        locationInfoText.setText(presenter.getLocationStringFromLatLng(this, latitude, longitude));
    }

    /** Shows the choose kitchen dialog
     * */
    @Override
    public void showSelectKitchenDialog() {
        chooseKitchenDialogFragment = ChooseKitchenDialogFragment.newInstance(presenter.getChosenKitchen());
        chooseKitchenDialogFragment
                .show(getSupportFragmentManager(), "fragment_choose_kitchen");
    }

    /** Callback for when a switch in the kitchen dialog has been changed
     *  passes the kitchentype and the status to the presenter which will save this change
     * */
    @Override
    public void chooseKitchen(KitchenType kitchenType, boolean chosen) {
        presenter.chooseFoodType(kitchenType,chosen);
    }

    /** Updates the text of the chosen kitchentypes
     * */
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

    /** This method will take the information in the restaurant object (name, image, budet etc)
     *  and update the view states accordingly
     *  Is used in the Edit-restaurant-mode
     * */
    @Override
    public void injectInformationToViews(@NonNull Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            presenter.injectImageOntoDrawable(imageFragmentView.getRestaurantImageView(), restaurant);
            imageFragmentView.setRestaurantImage(((BitmapDrawable) imageFragmentView.getRestaurantImageView().getDrawable()).getBitmap());
        } else {
            imageFragmentView.getRestaurantImageView().setImageDrawable(AndroidUtils.drawableResToDrawable(R.drawable.restaurant_placeholder, getApplicationContext()));
        }
        nameField.setText(restaurant.getName());
        setLocationInformation(restaurant.getLatitude(), restaurant.getLongitude());
        translateBudgetTypesToCheckedBoxes(restaurant.getBudgetTypes());
        ratingBar.setRating(restaurant.getRating());
        updateChosenKitchens(Arrays.asList(restaurant.getKitchenTypes()));
    }

    /** Takes an array of BudgetType and sets the appropriate checkbox as checked
     * */
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

    /** Called when the location service returns a location
     * */
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            setLocationInformation(location.getLatitude(), location.getLongitude());
        } else {
            Toast.makeText(this, R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }
    }

    /** Shows a dialog that asks if the user doesn't want to save the restaurant
     *  currently being created/edited
     * */
    @Override
    public void onBackPressed() {
        createAndShowCancelDialog();
    }

    private void createAndShowCancelDialog() {
        cancelDialog = createCancelDialog();
        cancelDialog.show();
    }

    private AlertDialog createCancelDialog() {
        return new AlertDialog.Builder(this).setTitle(R.string.unsaved_information)
                .setMessage(R.string.restaurant_not_saved_yet)
                .setPositiveButton(R.string.stay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cancelDialog = null;
                    }
                })
                .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelDialog = null;
                        CreateRestaurantActivity.super.onBackPressed();
                    }
                }).create();
    }


    /**
     #############################################################################
     #############################################################################
     ########################       ON CLICK              ########################
     #############################################################################
     #############################################################################
     *
     *  OnClick-listeners for buttons in the activity
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

    /** Starts the choose location from map activity
     *  if the location has been set previously then
     *  this position will be shown on the map
     * */
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

    /** This method is called when the activity is being shut down
     *  call the presenter to preform on shutdown functionality.
     *
     *  dismisses any dialogs that may be showing
     * */
    @Override
    protected void onDestroy() {
        if (cancelDialog != null) {
            cancelDialog.dismiss();
        }
        if (chooseKitchenDialogFragment != null) {
            chooseKitchenDialogFragment.dismiss();
        }
        presenter.onCloseActivity();
        super.onDestroy();
    }

    /** stores state information in order for this activity
     *  to save it's state when the activity is being hidden
     * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(getString(R.string.bundle_chosen_kitchen_types), CollectionUtils.enumToIntArr(presenter.getChosenKitchen()));
        outState.putBoolean(getString(R.string.bundle_is_showing_choose_kitchen_dialog),
                AndroidUtils.dialogFragmentIsShowing(chooseKitchenDialogFragment));
        outState.putBoolean(getString(R.string.bundle_is_showing_cancel_dialog), cancelDialog != null);
        if (location != null) {
            outState.putDoubleArray(getString(R.string.bundle_location), new double[]{location[0], location[1]});
        }
    }

    /** Restors the state saved in the method above
     * */
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
        if (savedInstanceState.getBoolean(getString(R.string.bundle_is_showing_cancel_dialog), false)) {
            createAndShowCancelDialog();
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

