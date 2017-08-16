package cel.dev.restaurants.createrestaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.ImageView;

import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.db.RestaurantCRUD;

/** "M"VP for this create restaurant use case
 *  The "M"/repository part is a bit unnecessary
 *  since this information can be retrieved
 *  from the RestaurantDao
 *
 *  Se the implementations for more details
 * */
public interface CreateRestaurantMVP {


    interface View {


        void onCancelCreateRestaurantPressed();

        void requestLocationPermission();

        boolean checkHasLocationPermission();

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

        Context getViewContext();
    }




    interface Presenter {

        void onCancelPressed();

        void onCreateRestaurantPressed();

        void onChooseKitchenBtnPressed();

        List<KitchenType> getKitchens();

        void chooseFoodType(KitchenType kitchenType, boolean chosen);

        List<KitchenType> getChosenKitchen();

        String getLocationStringFromLatLng(Context context, double latitude, double longitude);

        boolean getIsEditRestaurantMode(Intent intent, Context context);

        long getRestaurantId();

        void injectImageOntoDrawable(ImageView imageView, Restaurant restaurant);
    }

    interface Repository {

        List<KitchenType> getKitchenTypes();


        List<KitchenType> chosenFoodTypes();

        void chooseFoodType(KitchenType kitchenType, boolean chosen);



        @Nullable
        Restaurant getRestaurant(long id);

        void setChosenFoodTypes(KitchenType[] kitchenTypes);

        void injectImageOntoImageView(ImageView imageView, Restaurant restaurant);
    }

}
