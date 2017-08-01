package cel.dev.restaurants.choosekitchendialog;


import java.util.List;


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
