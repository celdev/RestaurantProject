package cel.dev.restaurants.choosekitchendialog;

import cel.dev.restaurants.model.KitchenType;

public interface OnChooseKitchenCallback {

    void chooseKitchen(KitchenType kitchenType, boolean chosen);

}
