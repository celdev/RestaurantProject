package cel.dev.restaurants.createrestaurant;

import android.graphics.Bitmap;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;
import cel.dev.restaurants.R;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.utils.PictureUtils;
import cel.dev.restaurants.utils.Values;

class CreateRestaurantPresenterImpl implements CreateRestaurantMVP.Presenter {



    private CreateRestaurantMVP.View view;
    private CreateRestaurantMVP.Repository repository;

    public CreateRestaurantPresenterImpl(CreateRestaurantMVP.View view) {
        this.view = view;
        this.repository = new CreateRestaurantRepositoryImpl(this);
    }

    @Override
    public void cameraButtonPressed() {
        if (view.hasCameraPermissions()) {
            view.takePictureWithCamera();
        } else {
            view.requestCameraPermission();
        }
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
        return saveRestaurant(restaurant);
    }

    private boolean saveRestaurant(Restaurant restaurant) {
        RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
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
    public void onNewKitchen(String name) {
        repository.saveKitchenType(name);
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
}
