package cel.dev.restaurants.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.utils.Values;

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

    public void injectLocation(double latitude, double longitude) {
        this.useLocation = true;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void injectKitchenTypeToFilter(KitchenType... kitchenType) {
        Collections.addAll(kitchenTypes, kitchenType);
    }

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



    public void closer() {
        range -= Values.RandomiserDefaults.RANGE_STEP_ON_CLOSER_DECREASE;
        if (range <= 0) {
            range = 0.001;
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
