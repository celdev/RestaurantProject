package cel.dev.restaurants.mainfragments.randomrestaurant.settingsdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import cel.dev.restaurants.R;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.utils.CollectionUtils;

public class RandomRestaurantSettingsDialogFragment extends DialogFragment{

    public RandomRestaurantSettingsDialogFragment(){}

    public static RandomRestaurantSettingsDialogFragment newInstance(RandomiseSettings randomiseSettings) {
        RandomRestaurantSettingsDialogFragment f = new RandomRestaurantSettingsDialogFragment();
        f.setArguments(SettingArgKeys.randomiseSettingsToBundle(randomiseSettings));
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_random_settings, container, false);
        ButterKnife.bind(this, v);
        handleSavedInstanceState(savedInstanceState);
        return v;
    }

    private void handleSavedInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean useLocation = savedInstanceState.getBoolean(SettingArgKeys.BOOL_USE_LOCATION);
            boolean useFavorite = savedInstanceState.getBoolean(SettingArgKeys.BOOL_USE_FAVORITE);
            boolean useBudgetTypes = savedInstanceState.getBoolean(SettingArgKeys.BOOL_USE_BUDGET_TYPE);
            boolean useKitchenTypes = savedInstanceState.getBoolean(SettingArgKeys.BOOL_USE_KITCHEN_TYPE);
            double range = savedInstanceState.getDouble(SettingArgKeys.DOUBLE_RANGE);
            double latitude = savedInstanceState.getDouble(SettingArgKeys.DOUBLE_LATITUDE);
            double longitude = savedInstanceState.getDouble(SettingArgKeys.DOUBLE_LONGITUDE);
            List<KitchenType> kitchenTypes = CollectionUtils.intArrToEnum(savedInstanceState.getIntArray(SettingArgKeys.SET_ENUM_KITCHEN_TYPES), KitchenType.class);
            List<BudgetType> budgetTypes = CollectionUtils.intArrToEnum(savedInstanceState.getIntArray(SettingArgKeys.SET_ENUM_BUDGET_TYPES), BudgetType.class);
            injectSettingsBools(useLocation, useFavorite, useKitchenTypes, useBudgetTypes);
            injectSettingsDoubles(range, latitude, longitude);
            injectSettingsCollections(kitchenTypes, budgetTypes);
        }
    }

    private void injectSettingsBools(boolean useLocation, boolean useFavorite, boolean useKitchenTypes, boolean useBudgetTypes) {

    }

    private void injectSettingsDoubles(double range, double latitude, double longitude) {

    }

    private void injectSettingsCollections(List<KitchenType> kitchenTypes, List<BudgetType> budgetTypes) {

    }
}
