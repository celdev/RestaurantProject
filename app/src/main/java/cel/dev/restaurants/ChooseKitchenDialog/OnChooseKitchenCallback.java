package cel.dev.restaurants.ChooseKitchenDialog;

import java.util.List;

import cel.dev.restaurants.Model.FoodType;

public interface OnChooseKitchenCallback {

    void chooseKitchen(FoodType foodType, boolean chosen);

}
