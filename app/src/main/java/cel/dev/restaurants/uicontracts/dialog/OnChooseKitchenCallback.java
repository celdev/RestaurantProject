package cel.dev.restaurants.uicontracts.dialog;

import cel.dev.restaurants.model.KitchenType;

/** This interface allows the communication between the fragmentdialog
 *  and the activity which contains the fragmentdialog in order for the fragment dialog to
 *  pass changes in Kitchen Types chosen status to the activity
 * */
public interface OnChooseKitchenCallback {

    /** The KitchenType and it's new status is passed through this method which allows the
     *  change to be stored without the sender to have a direct connection to the database
     * */
    void chooseKitchen(KitchenType kitchenType, boolean chosen);

}
