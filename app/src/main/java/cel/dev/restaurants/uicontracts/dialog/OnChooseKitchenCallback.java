package cel.dev.restaurants.uicontracts.dialog;

import cel.dev.restaurants.model.KitchenType;

public interface OnChooseKitchenCallback {

    void chooseKitchen(KitchenType kitchenType, boolean chosen);

}
