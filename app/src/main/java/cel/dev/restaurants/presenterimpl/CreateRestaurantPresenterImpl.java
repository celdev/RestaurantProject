package cel.dev.restaurants.presenterimpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import cel.dev.restaurants.activities.CreateRestaurantActivity;
import cel.dev.restaurants.view.CreateRestaurantValidationErrors;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;
import cel.dev.restaurants.R;
import cel.dev.restaurants.repositoryimpl.CreateRestaurantRepositoryImpl;
import cel.dev.restaurants.uicontracts.CreateRestaurantMVP;
import cel.dev.restaurants.utils.PictureUtils;
import cel.dev.restaurants.utils.Values;

/** This class is the presenter for the Create restaurant activity
 *  and implements the presenter part of the MVP contract in order to extract
 *  some logic from the activity
 * */
public class CreateRestaurantPresenterImpl implements CreateRestaurantMVP.Presenter {

    public static final String TAG = "create rest pres";

    private CreateRestaurantMVP.View view;
    private CreateRestaurantMVP.Repository repository;

    private boolean isEditMode;
    private long restaurantId;
    private boolean restaurantIsFavorite;

    /** Creates the repository
     * */
    public CreateRestaurantPresenterImpl(CreateRestaurantMVP.View view, Context context) {
        this.view = view;
        this.repository = new CreateRestaurantRepositoryImpl(context);
    }


    /** Tries to create the restaurant using the current information stored in the view and the repository
     *  if the creation is successful (all information needed was provided) then the restaurant will be
     *  saved. If this is successful then the createRestaurantOk will be called
     * */
    @Override
    public void onCreateRestaurantPressed() {
        if (buildRestaurant()) {
            view.createRestaurantOk();
        }
    }

    /** Tries to build the restaurant using the information stored in the view and the repository
     *  If the view is missing some necessary information then this method will call the view to
     *  show a message stating which information is missing and return false
     *
     *  if all information was provided the restaurant will be saved
     *  or if the mode is to update the restaurant the restaurant will be updated
     * */
    private boolean buildRestaurant() {
        Bitmap bitmap = view.getRestaurantImage();
        String name = view.getRestaurantName();
        float rating = view.getRestaurantRating();
        Double[] location = view.getPosition();
        BudgetType[] budgetTypes = view.getSelectedBudgetTypes();
        List<KitchenType> chosenKitchen = getChosenKitchen();
        int validateCode = validateRestaurantInfo(name, rating, location, chosenKitchen, budgetTypes);
        if (validateCode != -100) {
            view.createRestaurantError(validateCode);
            return false;
        }
        Restaurant restaurant;
        if (bitmap == null) {
            restaurant = new RestaurantPlaceholderImage(name,
                    rating, budgetTypes, location[0], location[1],
                    chosenKitchen.toArray(new KitchenType[chosenKitchen.size()]),
                    R.drawable.restaurant_placeholder,false);
        } else {
            restaurant = new RestaurantCustomImage(name,rating,budgetTypes,location[0],location[1],
                    chosenKitchen.toArray(new KitchenType[chosenKitchen.size()]),
                    PictureUtils.bitmapToByteArray(bitmap, Values.ON_SAVE_RESTAURANT_IMAGE_COMPRESS_QUALITY),false
                    );
        }
        if (isEditMode) {
            restaurant.setId(restaurantId);
            restaurant.setFavorite(restaurantIsFavorite);
        }
        return repository.saveRestaurant(restaurant);
    }

    /** This method returns a @StringRes id or (-100) corresponding to the first information which wasn't
     *  provided by the user which is needed when creating or saving a restaurant
     *  if everything was provided successfully then the method will return -100
     * */
    private int validateRestaurantInfo(String name, float rating, Double[] location, List<KitchenType> chosenKitchen, BudgetType[] budgetTypes) {
        if (name == null || name.isEmpty()) {
            return CreateRestaurantValidationErrors.ERROR_NO_NAME;
        }
        if (rating <= 0) {
            return CreateRestaurantValidationErrors.ERROR_NO_RATING;
        }
        if (location == null) {
            return CreateRestaurantValidationErrors.ERROR_NO_POS;
        }
        if (chosenKitchen.isEmpty()) {
            return CreateRestaurantValidationErrors.ERROR_NO_KITCHEN;
        }
        if (budgetTypes == null || budgetTypes.length == 0) {
            return CreateRestaurantValidationErrors.ERROR_NO_BUDGET;
        }
        return -100;
    }

    /** This method calls the view to show the chose kitchen dialog
     * */
    @Override
    public void onChooseKitchenBtnPressed() {
        view.showSelectKitchenDialog();
    }


    /** passes the kitchen types and it's new chosen state to the repository
     * */
    @Override
    public void chooseFoodType(KitchenType kitchenType, boolean chosen) {
        repository.chooseFoodType(kitchenType, chosen);
        view.updateChosenKitchens(getChosenKitchen());
    }

    /** Returns the chosen kitchen types
     * */
    @Override
    public List<KitchenType> getChosenKitchen() {
        return repository.chosenFoodTypes();
    }

    /** Tries to retrieve an address of the position passed as a parameter
     *  This is done using the Geocoder
     *
     *  The method will return a String, either the address of the position or
     *  a message saying that the position was retrieved
     *
     *  The address isn't saved so the failure of the address retrieval won't affect the
     *  ability to create or update a restaurant
     * */
    @Override
    public String getLocationStringFromLatLng(Context context, double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> fromLocation = geocoder.getFromLocation(latitude, longitude, 1);
            return fromLocation.get(0).getAddressLine(0);
        } catch (Exception e) {
            Log.d("create pres", "getLocationStringFromLatLng: ", e);
            return context.getString(R.string.error_getting_address_location_set);
        }
    }

    /** Checks if the Intent contains information about an restaurant to edit, if so then
     *  the mode of this activity should be to edit a restaurant.
     *
     *  This method tries to retrieve the restaurant in order to fill the view with its information
     *  return true if the intent contains information about a restaurant to edit
     * */
    @Override
    public boolean getIsEditRestaurantMode(Intent intent, Context context) {
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            long restaurantId = extras.getLong(CreateRestaurantActivity.EDIT_RESTAURANT_ID, -1L);
            if (restaurantId != -1) {
                loadRestaurantToEdit(restaurantId);
                return true;
            }
        }
        return false;
    }

    /** This method tries to load a restaurant from the database and is used when the activity
     *  should be used to update a restaurant
     *
     *  if unsuccessful to retrieve a restaurant with this id (shouldn't be able to happen) then
     *  the view will show an error message and the mode of this activity will be to create a new
     *  restaurant
     * */
    private void loadRestaurantToEdit(long id) {
        Restaurant restaurant = repository.getRestaurant(id);
        if (restaurant == null) {
            view.showError(R.string.error_edit_restaurant);
        } else {
            repository.setChosenFoodTypes(restaurant.getKitchenTypes());
            restaurantId = restaurant.getId();
            restaurantIsFavorite = restaurant.isFavorite();
            isEditMode = true;
            view.injectInformationToViews(restaurant);
        }
    }

    /** Retrieves the image of the restaurant and adds it onto the ImageView
     * */
    @Override
    public void injectImageOntoDrawable(ImageView imageView, Restaurant restaurant) {
        repository.injectImageOntoImageView(imageView, restaurant);
    }

    /** This method allows the presenter to call functions that needs to be called
     *  when the activity of this presenter is being destroyed
     *
     *  Calls the repository to close the database
     * */
    @Override
    public void onCloseActivity() {
        repository.onClose();
    }

}
