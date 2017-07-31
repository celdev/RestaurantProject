package cel.dev.restaurants.CreateRestaurant;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.Model.Restaurant;
import cel.dev.restaurants.Repository.RestaurantDAO;
import cel.dev.restaurants.Repository.RestaurantDAOImpl;

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
    public void onCreateRestaurantPressed(Restaurant restaurant) {
        new RestaurantDAOImpl().saveRestaurant(restaurant);
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
    }

    @Override
    public List<FoodType> getChosenKitchen() {
        return repository.chosenFoodTypes();
    }
}
