package cel.dev.restaurants.createrestaurant;

import android.graphics.Bitmap;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.FoodType;

public interface CreateRestaurantMVP {


    interface View {

        void takePictureWithCamera();

        void finishCreateRestaurant();

        void onCancelCreateRestaurantPressed();

        void onOkCreateRestaurantPressed();

        //void onCreateError();

        void requestCameraPermission();

        void requestLocationPermission();

        boolean hasLocationPermission();

        boolean hasCameraPermissions();

        void showSelectKitchenDialog();

        void updateChosenKitchens(List<FoodType> foodTypes);

        Bitmap getRestaurantImage();

        String getRestaurantName();

        float getRestaurantRating();

        BudgetType[] getSelectedBudgetTypes();

        Double[] getPosition();

        void createRestaurantError(int validationMessage);

        void createRestaurantOk();



    }

    interface Presenter {
        void cameraButtonPressed();

        void onCancelPressed();

        void onCreateRestaurantPressed();

        void onChooseKitchenBtnPressed();

        void onNewKitchen(String name);

        List<FoodType> getKitchens();

        void chooseFoodType(FoodType foodType, boolean chosen);

        List<FoodType> getChosenKitchen();

    }

    interface Repository {

        List<FoodType> getKitchenTypes();

        void saveKitchenType(String name);

        List<FoodType> chosenFoodTypes();

        void chooseFoodType(FoodType foodType, boolean chosen);


    }

}
