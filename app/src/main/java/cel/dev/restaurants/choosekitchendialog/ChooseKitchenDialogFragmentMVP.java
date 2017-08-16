package cel.dev.restaurants.choosekitchendialog;


import java.util.List;


/** "M"VP-interface for the DialogFragment
 * */
public interface ChooseKitchenDialogFragmentMVP {

    interface View extends HasShownStatus {

        void injectArrayAdapter(List<FoodTypeAndChosenStatus> statusList);
        void onFoodTypeChosenChange(FoodTypeAndChosenStatus foodTypeAndChosenStatus);

    }

    interface Presenter extends SwitchCallback {

        void createArrayAdapter();

    }

    interface HasShownStatus {
        boolean getDialogIsShowing();
    }

    /** Callback for when a switch is changed
     * */
    interface SwitchCallback {
        void onSwitchChangeCallback(FoodTypeAndChosenStatus foodTypeAndChosenStatus);
    }

}
