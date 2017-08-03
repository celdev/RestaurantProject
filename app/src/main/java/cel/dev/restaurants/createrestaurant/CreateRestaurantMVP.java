package cel.dev.restaurants.createrestaurant;

import android.graphics.Bitmap;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;

public interface CreateRestaurantMVP {


    interface View {

        void takePictureWithCamera();

        void finishCreateRestaurant();

        void onCancelCreateRestaurantPressed();

        void onOkCreateRestaurantPressed();

        //void onCreateError();

        void requestCameraPermission();

        void requestLocationPermission();

        boolean checkHasLocationPermission();

        boolean hasCameraPermissions();

        void showSelectKitchenDialog();

        void updateChosenKitchens(List<KitchenType> kitchenTypes);

        Bitmap getRestaurantImage();

        String getRestaurantName();

        float getRestaurantRating();

        BudgetType[] getSelectedBudgetTypes();

        Double[] getPosition();

        void createRestaurantError(int validationMessage);

        void createRestaurantOk();

    }

    interface UserInputInformationListener {
        void hasInputInformation(boolean hasInput);
    }

    interface Presenter {
        void cameraButtonPressed();

        void onCancelPressed();

        void onCreateRestaurantPressed();

        void onChooseKitchenBtnPressed();

        List<KitchenType> getKitchens();

        void chooseFoodType(KitchenType kitchenType, boolean chosen);

        List<KitchenType> getChosenKitchen();

    }

    interface Repository {

        List<KitchenType> getKitchenTypes();


        List<KitchenType> chosenFoodTypes();

        void chooseFoodType(KitchenType kitchenType, boolean chosen);


    }

}
