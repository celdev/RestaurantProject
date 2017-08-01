package cel.dev.restaurants.choosekitchendialog;

import cel.dev.restaurants.model.FoodType;

/** This class is used to convert two arrays of food types into a list of
 *  food types which has a chosen status
 * */
public class FoodTypeAndChosenStatus {
    private FoodType foodType;
    private boolean chosen;

    public FoodTypeAndChosenStatus(FoodType foodType, boolean chosen) {
        this.foodType = foodType;
        this.chosen = chosen;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodTypeAndChosenStatus that = (FoodTypeAndChosenStatus) o;

        return foodType.equals(that.foodType);

    }

    @Override
    public int hashCode() {
        return foodType.hashCode();
    }
}
