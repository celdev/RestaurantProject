package cel.dev.restaurants.CreateRestaurant;

import android.graphics.Bitmap;

import java.util.List;

import cel.dev.restaurants.Model.BudgetType;
import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.Model.Restaurant;
import cel.dev.restaurants.Model.RestaurantCustomImage;
import cel.dev.restaurants.Model.RestaurantPlaceholderImage;
import cel.dev.restaurants.R;
import cel.dev.restaurants.Repository.RestaurantDAO;
import cel.dev.restaurants.Repository.RestaurantDAOImpl;
import cel.dev.restaurants.Utils.PictureUtils;
import cel.dev.restaurants.Utils.Values;

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
        List<FoodType> chosenKitchen = getChosenKitchen();
        int validateCode = validateRestaurantInfo(name, rating, location, chosenKitchen, budgetTypes);
        if (validateCode != -100) {
            view.createRestaurantError(validateCode);
            return false;
        }
        Restaurant restaurant;
        if (bitmap == null) {
            restaurant = new RestaurantPlaceholderImage(name,
                    rating, budgetTypes, location[0], location[1],
                    chosenKitchen.toArray(new FoodType[chosenKitchen.size()]),
                    R.drawable.restaurant_placeholder);
        } else {
            restaurant = new RestaurantCustomImage(name,rating,budgetTypes,location[0],location[1],
                    chosenKitchen.toArray(new FoodType[chosenKitchen.size()]),
                    PictureUtils.bitmapToByteArray(bitmap, Values.ON_SAVE_RESTAURANT_IMAGE_COMPRESS_QUALITY)
                    );
        }
        return saveRestaurant(restaurant);
    }

    private boolean saveRestaurant(Restaurant restaurant) {
        RestaurantDAO restaurantDAO = new RestaurantDAOImpl();
        return restaurantDAO.saveRestaurant(restaurant);
    }

    private int validateRestaurantInfo(String name, float rating, Double[] location, List<FoodType> chosenKitchen, BudgetType[] budgetTypes) {
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
    public List<FoodType> getKitchens() {
        return repository.getKitchenTypes();
    }

    @Override
    public void chooseFoodType(FoodType foodType, boolean chosen) {
        repository.chooseFoodType(foodType, chosen);
        view.updateChosenKitchens(getChosenKitchen());
    }

    @Override
    public List<FoodType> getChosenKitchen() {
        return repository.chosenFoodTypes();
    }
}
