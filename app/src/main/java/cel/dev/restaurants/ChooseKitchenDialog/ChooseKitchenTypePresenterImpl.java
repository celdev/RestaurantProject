package cel.dev.restaurants.ChooseKitchenDialog;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.Model.FoodType;

class ChooseKitchenTypePresenterImpl implements ChooseKitchenDialogFragmentMVP.Presenter {

    private ChooseKitchenDialogFragmentMVP.View view;
    private List<FoodTypeAndChosenStatus> foodTypeAndChosenStatuses;

    public ChooseKitchenTypePresenterImpl(ChooseKitchenDialogFragmentMVP.View view, List<FoodType> all, List<FoodType> chosen) {
        this.view = view;
        createArrays(all, chosen);
    }

    private void createArrays(List<FoodType> all, List<FoodType> chosen) {
        List<FoodTypeAndChosenStatus> statusList = new ArrayList<>(all.size());
        Set<FoodType> chosenSet = new HashSet<>(chosen);
        for (FoodType foodType : all) {
            statusList.add(new FoodTypeAndChosenStatus(foodType, chosenSet.contains(foodType)));
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
