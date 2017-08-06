package cel.dev.restaurants.createrestaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;
import cel.dev.restaurants.R;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.repository.db.RestaurantCRUD;
import cel.dev.restaurants.utils.PictureUtils;
import cel.dev.restaurants.utils.Values;

class CreateRestaurantPresenterImpl implements CreateRestaurantMVP.Presenter {

    public static final String TAG = "create rest pres";

    private CreateRestaurantMVP.View view;
    private CreateRestaurantMVP.Repository repository;

    private boolean isEditMode;
    private long restaurantId;
    private boolean restaurantIsFavorite;

    public CreateRestaurantPresenterImpl(CreateRestaurantMVP.View view, Context context) {
        this.view = view;
        this.repository = new CreateRestaurantRepositoryImpl(context);
        Log.d(TAG, "CreateRestaurantPresenterImpl: Creating presenter" );
    }


    @Override
    public void onCancelPressed() {
        view.onCancelCreateRestaurantPressed();
    }

    @Override
    public void onCreateRestaurantPressed() {
        if (buildRestaurant()) {
            view.createRestaurantOk();
        }
    }

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
        return saveRestaurant(restaurant);
    }

    private boolean saveRestaurant(Restaurant restaurant) {
        RestaurantDAO restaurantDAO = new RestaurantDAOImpl(view.getViewContext());
        return restaurantDAO.saveRestaurant(restaurant);
    }

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

    @Override
    public void onChooseKitchenBtnPressed() {
        view.showSelectKitchenDialog();
    }


    @Override
    public List<KitchenType> getKitchens() {
        return repository.getKitchenTypes();
    }

    @Override
    public void chooseFoodType(KitchenType kitchenType, boolean chosen) {
        repository.chooseFoodType(kitchenType, chosen);
        view.updateChosenKitchens(getChosenKitchen());
    }

    @Override
    public List<KitchenType> getChosenKitchen() {
        return repository.chosenFoodTypes();
    }

    @Override
    public String getLocationStringFromLatLng(Context context, double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> fromLocation = geocoder.getFromLocation(latitude, longitude, 1);
            return fromLocation.get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.d("create pres", "getLocationStringFromLatLng: ", e);
            return context.getString(R.string.error_getting_address);
        }
    }

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

    public void loadRestaurantToEdit(long id) {
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

    @Override
    public long getRestaurantId() {
        return restaurantId;
    }

    @Override
    public void injectImageOntoDrawable(ImageView imageView, Restaurant restaurant) {
        repository.injectImageOntoImageView(imageView, restaurant);
    }

}
