package cel.dev.restaurants.presenters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.model.FoodTypeAndChosenStatus;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.uicontracts.dialog.ChooseKitchenDialogFragmentMVP;

/** Implementation of the presenter for the kitchen type dialog in the create kitchen activity
 *
 *  During the creating of this presenter the already chosen kitchen types are merged
 *  with all kitchen types into an object which contains the kitchen type and if it is chosen.
 *
 *  These objects are then used in creating the adapter for the listview in the view
 * */
public class ChooseKitchenTypePresenterImpl implements ChooseKitchenDialogFragmentMVP.Presenter {

    private ChooseKitchenDialogFragmentMVP.View view;
    private List<FoodTypeAndChosenStatus> foodTypeAndChosenStatuses;

    /** Creates the List which will contain FoodTypeAndChosenStatus using all kitchen types and
     *  a list of chosen kitchen types
     * */
    public ChooseKitchenTypePresenterImpl(ChooseKitchenDialogFragmentMVP.View view, List<KitchenType> all, List<KitchenType> chosen) {
        this.view = view;
        createChosenFoodTypeList(all, chosen);
    }

    /** Creates a List of FoodTypeAndChosenStatus
     * */
    private void createChosenFoodTypeList(List<KitchenType> all, List<KitchenType> chosen) {
        List<FoodTypeAndChosenStatus> statusList = new ArrayList<>(all.size());
        //creates a set of the chosen KitchenTypes so that the contains()-method is faster
        Set<KitchenType> chosenSet = new HashSet<>(chosen);
        //use all kitchen type to create the FoodTypeAndChosenStatus, the status will be set to
        //true if the kitchen type is contained in the chosenSet (false if not)
        for (KitchenType kitchenType : all) {
            statusList.add(new FoodTypeAndChosenStatus(kitchenType, chosenSet.contains(kitchenType)));
        }
        foodTypeAndChosenStatuses = statusList;
    }

    /** injects the FoodTypeAndChosenStatus into the view for using with the adapter for the
     *  ListView
     * */
    @Override
    public void createArrayAdapter() {
        view.injectArrayAdapter(foodTypeAndChosenStatuses);
    }


    /** when a switch is changed in the list view this method is called
     *  which changes the is chosen status which is then
     *  saved by the activity through the callback in the view
     * */
    @Override
    public void onSwitchChangeCallback(FoodTypeAndChosenStatus foodTypeAndChosenStatus) {
        int i = foodTypeAndChosenStatuses.indexOf(foodTypeAndChosenStatus);
        foodTypeAndChosenStatuses.get(i).setChosen(foodTypeAndChosenStatus.isChosen());
        view.onFoodTypeChosenChange(foodTypeAndChosenStatus);
    }
}
