package cel.dev.restaurants.choosekitchendialog;

import android.support.annotation.NonNull;

import cel.dev.restaurants.model.KitchenType;

/** This class is used to convert two arrays of food types into a list of
 *  food types which has a chosen status
 * */
public class FoodTypeAndChosenStatus {
    private KitchenType kitchenType;
    private boolean chosen;

    public FoodTypeAndChosenStatus(@NonNull KitchenType kitchenType, boolean chosen) {
        this.kitchenType = kitchenType;
        this.chosen = chosen;
    }

    @NonNull
    public KitchenType getKitchenType() {
        return kitchenType;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodTypeAndChosenStatus that = (FoodTypeAndChosenStatus) o;

        return kitchenType.equals(that.kitchenType);

    }

    @Override
    public int hashCode() {
        return kitchenType.hashCode();
    }
}
