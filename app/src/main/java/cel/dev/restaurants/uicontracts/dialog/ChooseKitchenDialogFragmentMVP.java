package cel.dev.restaurants.uicontracts.dialog;


import java.util.List;

import cel.dev.restaurants.model.FoodTypeAndChosenStatus;


/** "M"VP-interface for the DialogFragment
 * */
public interface ChooseKitchenDialogFragmentMVP {

    /** The view contract extends HasShownStatus which allows the containing activity
     *  to determine if this view is currently being shown.
     * */
    interface View extends HasShownStatus {

        /** Creates the ArrayAdapter for the ListView using the
         *  @param statusList as content
         * */
        void injectArrayAdapter(List<FoodTypeAndChosenStatus> statusList);

        /** Callback for when a Kitchen type has been selected or deselected
         *  Sends this change to the onChooseKitchenCallback (which is the containing activity)
         *  callback, this allows the change to be recorded by the containing activity.
         * */
        void onFoodTypeChosenChange(FoodTypeAndChosenStatus foodTypeAndChosenStatus);

    }

    interface Presenter extends SwitchCallback {

        /** This method is called when the view is ready to retrieve the content for the array adapter
         *  and will create create the contents for the array adapter and inject it into the view
         * */
        void createArrayAdapter();

    }

    interface HasShownStatus {

        /** This method allows for the activity which will contain an implementation of
         *  this interface to check if the object is currently being showed
         * */
        boolean getDialogIsShowing();
    }

    /** Callback for when a switch is changed
     * */
    interface SwitchCallback {

        /** when a switch is changed in the list view this method is called
         *  which changes the is chosen status which is then
         *  saved by the activity through the callback in the view
         * */
        void onSwitchChangeCallback(FoodTypeAndChosenStatus foodTypeAndChosenStatus);
    }

}
