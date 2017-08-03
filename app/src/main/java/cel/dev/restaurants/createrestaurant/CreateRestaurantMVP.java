package cel.dev.restaurants.createrestaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;

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

        void showError(@StringRes int errorResCode);

        void injectInformationToViews(@NonNull Restaurant restaurant);

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

        String getLocationStringFromLatLng(Context context, double latitude, double longitude);

        boolean getIsEditRestaurantMode(Intent intent);
    }

    interface Repository {

        List<KitchenType> getKitchenTypes();


        List<KitchenType> chosenFoodTypes();

        void chooseFoodType(KitchenType kitchenType, boolean chosen);


        @Nullable
        Restaurant getRestaurant(int id);

        void setChosenFoodTypes(KitchenType[] kitchenTypes);
    }

}
