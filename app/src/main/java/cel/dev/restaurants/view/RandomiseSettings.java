package cel.dev.restaurants.view;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.utils.Values;


/** This class contains the randomise settings used when
 *  retrieving a random restaurant.
 *
 *  The different settings that can be used are
 *      location (within a range)
 *      BudgetTypes
 *      KitchenTypes
 *      Id of restaurants
 * */
public class RandomiseSettings {

    private boolean useLocation;
    private double range;
    private double latitude, longitude;
    private Set<BudgetType> budgetTypes;

    private Set<KitchenType> kitchenTypes;

    private Set<Long> notTheseRestaurantsById;


    public RandomiseSettings(Set<Long> notTheseRestaurantsById) {
        this.range = Values.RandomiserDefaults.DEFAULT_RANGE;
        this.budgetTypes = new HashSet<>();
        this.kitchenTypes = new HashSet<>();
        this.notTheseRestaurantsById = notTheseRestaurantsById;
    }

    /** Injects a location into the random settings
     *  and sets the settings to use this location to filter restaurants
     * */
    public void injectLocation(double latitude, double longitude) {
        this.useLocation = true;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** injects an amount of KitchenTypes to filter
     * */
    public void injectKitchenTypeToFilter(KitchenType... kitchenType) {
        Collections.addAll(kitchenTypes, kitchenType);
    }

    /** Sets the budgetfilters
     *  if the user wants to filter restaurants that are cheaper than NORMAL
     *  then also restaurants which are EXPENSIVE and VERY_EXPENSIVE will also be filtered
     *  if the user tries to filter restaurants cheaper than CHEAP nothing will happen
     *  since filtering restaurants Cheaper than CHEAP will always result in 0 restaurants
     * */
    public boolean setCheaperThan(@NonNull BudgetType cheaperThanThis) {
        switch (cheaperThanThis) {
            case CHEAP:
                return false;
            case NORMAL:
                budgetTypes.add(BudgetType.NORMAL);
            case EXPENSIVE:
                budgetTypes.add(BudgetType.EXPENSIVE);
            case VERY_EXPENSIVE:
                budgetTypes.add(BudgetType.VERY_EXPENSIVE);
        }
        return true;
    }


    /** Decreases the range by a certain amount
     *  if the range is less than a certain amount then the range will be set to that amount
     * */
    public void closer() {
        range -= Values.RandomiserDefaults.RANGE_STEP_ON_CLOSER_DECREASE;
        if (range <= Values.MINIMUM_RANGE_VALUE) {
            range = Values.MINIMUM_RANGE_VALUE;
        }
    }


    public boolean isUseLocation() {
        return useLocation;
    }

    public double getRange() {
        return range;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Set<BudgetType> getBudgetTypes() {
        return budgetTypes;
    }

    public Set<KitchenType> getKitchenTypes() {
        return kitchenTypes;
    }

    public Set<Long> getNotTheseRestaurantsById() {
        return notTheseRestaurantsById;
    }
}
