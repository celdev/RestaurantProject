package cel.dev.restaurants.CreateRestaurant;

import android.app.Activity;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.Model.Restaurant;

public interface CreateRestaurantMVP {


    interface View {

        void takePictureWithCamera();

        void finishCreateRestaurant();

        void onCancelCreateRestaurantPressed();

        void onOkCreateRestaurantPressed();

        //void onCreateError();

        void requestCameraPermission();

        boolean hasCameraPermissions();

        void showSelectKitchenDialog();
    }

    interface Presenter {
        void cameraButtonPressed();

        void onCancelPressed();

        void onCreateRestaurantPressed(Restaurant restaurant);

        void onChooseKitchenBtnPressed();

    }

    interface Repository {

        List<FoodType> getKitchenTypes();

        void saveKitchenType(String name);


    }

}
