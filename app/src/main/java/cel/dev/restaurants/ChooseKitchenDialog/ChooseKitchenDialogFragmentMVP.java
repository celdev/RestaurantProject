package cel.dev.restaurants.ChooseKitchenDialog;

import android.widget.ArrayAdapter;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;

public interface ChooseKitchenDialogFragmentMVP {

    interface View {

        void injectArrayAdapter(List<FoodTypeAndChosenStatus> statusList);
        void onFoodTypeChosenChange(FoodTypeAndChosenStatus foodTypeAndChosenStatus);

    }

    interface Presenter extends SwitchCallback {

        void createArrayAdapter();


    }

    interface SwitchCallback {
        void onSwitchChangeCallback(FoodTypeAndChosenStatus foodTypeAndChosenStatus);
    }

}
