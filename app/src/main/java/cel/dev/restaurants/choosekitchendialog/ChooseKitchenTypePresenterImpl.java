package cel.dev.restaurants.choosekitchendialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.model.KitchenType;

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


    @Override
    public void onSwitchChangeCallback(FoodTypeAndChosenStatus foodTypeAndChosenStatus) {
        int i = foodTypeAndChosenStatuses.indexOf(foodTypeAndChosenStatus);
        foodTypeAndChosenStatuses.get(i).setChosen(foodTypeAndChosenStatus.isChosen());
        view.onFoodTypeChosenChange(foodTypeAndChosenStatus);
    }
}
