package cel.dev.restaurants.choosekitchendialog;

import cel.dev.restaurants.model.FoodType;

public interface OnChooseKitchenCallback {

    void chooseKitchen(FoodType foodType, boolean chosen);

}
