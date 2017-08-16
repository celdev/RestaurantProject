package cel.dev.restaurants.choosekitchendialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.model.KitchenType;

/** Implementation of the presenter for the kitchen type dialog in the create kitchen activity
 *
 *  During the creating of this presenter the already chosen kitchen types are merged
 *  with all kitchen types into an object which contains the kitchen type and if it is chosen.
 *
 *  These objects are then used in creating the adapter for the listview in the view
 * */
class ChooseKitchenTypePresenterImpl implements ChooseKitchenDialogFragmentMVP.Presenter {

    private ChooseKitchenDialogFragmentMVP.View view;
    private List<FoodTypeAndChosenStatus> foodTypeAndChosenStatuses;

    public ChooseKitchenTypePresenterImpl(ChooseKitchenDialogFragmentMVP.View view, List<KitchenType> all, List<KitchenType> chosen) {
        this.view = view;
        createArrays(all, chosen);
    }

    private void createArrays(List<KitchenType> all, List<KitchenType> chosen) {
        List<FoodTypeAndChosenStatus> statusList = new ArrayList<>(all.size());
        Set<KitchenType> chosenSet = new HashSet<>(chosen);
        for (KitchenType kitchenType : all) {
            statusList.add(new FoodTypeAndChosenStatus(kitchenType, chosenSet.contains(kitchenType)));
        }
        foodTypeAndChosenStatuses = statusList;
    }

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
