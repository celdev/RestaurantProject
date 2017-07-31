package cel.dev.restaurants.CreateRestaurant;

import cel.dev.restaurants.Model.Restaurant;

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

    }

    @Override
    public void onChooseKitchenBtnPressed() {
        view.showSelectKitchenDialog();
    }
}
