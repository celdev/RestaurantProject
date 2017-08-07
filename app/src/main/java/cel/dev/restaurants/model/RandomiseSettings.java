package cel.dev.restaurants.model;

import java.util.List;
import java.util.Set;

public class RandomiseSettings {

    private boolean useLocation;
    private double range;
    private double latitude, longitude;

    private boolean isFavorite;

    private boolean useBudgetTypes;
    private Set<BudgetType> budgetTypes;

    private boolean useKitchenTypes;
    private Set<KitchenType> kitchenTypes;

    private Set<Long> notTheseRestaurantsById;


    public RandomiseSettings(boolean useLocation, double range, double latitude, double longitude, boolean isFavorite,
                             boolean useBudgetTypes, Set<BudgetType> budgetTypes, boolean useKitchenTypes,
                             Set<KitchenType> kitchenTypes, Set<Long> notTheseRestaurantsById) {
        this.useLocation = useLocation;
        this.range = range;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFavorite = isFavorite;
        this.useBudgetTypes = useBudgetTypes;
        this.budgetTypes = budgetTypes;
        this.useKitchenTypes = useKitchenTypes;
        this.kitchenTypes = kitchenTypes;
        this.notTheseRestaurantsById = notTheseRestaurantsById;
    }

    public Set<BudgetType> getBudgetTypes() {
        return budgetTypes;
    }

    public void setBudgetTypes(Set<BudgetType> budgetTypes) {
        this.budgetTypes = budgetTypes;
    }

    public Set<KitchenType> getKitchenTypes() {
        return kitchenTypes;
    }

    public void setKitchenTypes(Set<KitchenType> kitchenTypes) {
        this.kitchenTypes = kitchenTypes;
    }

    public Set<Long> getNotTheseRestaurantsById() {
        return notTheseRestaurantsById;
    }

    public void setNotTheseRestaurantsById(Set<Long> notTheseRestaurantsById) {
        this.notTheseRestaurantsById = notTheseRestaurantsById;
    }

    public boolean isUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isUseBudgetTypes() {
        return useBudgetTypes;
    }

    public void setUseBudgetTypes(boolean useBudgetTypes) {
        this.useBudgetTypes = useBudgetTypes;
    }



    public boolean isUseKitchenTypes() {
        return useKitchenTypes;
    }

    public void setUseKitchenTypes(boolean useKitchenTypes) {
        this.useKitchenTypes = useKitchenTypes;
    }

}
