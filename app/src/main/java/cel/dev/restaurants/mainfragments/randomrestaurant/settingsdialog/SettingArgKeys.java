package cel.dev.restaurants.mainfragments.randomrestaurant.settingsdialog;

import android.os.Bundle;

import cel.dev.restaurants.model.RandomiseSettings;
import cel.dev.restaurants.utils.CollectionUtils;

public class SettingArgKeys {

    public static final String BOOL_USE_LOCATION = "use_location";
    public static final String BOOL_USE_FAVORITE = "use_favorite";
    public static final String BOOL_USE_KITCHEN_TYPE = "use_kitchen_types";
    public static final String BOOL_USE_BUDGET_TYPE = "use_budget_types";
    public static final String DOUBLE_RANGE = "location_range";
    public static final String DOUBLE_LATITUDE = "location_latitude";
    public static final String DOUBLE_LONGITUDE = "location_longitude";
    public static final String SET_ENUM_BUDGET_TYPES = "budget_types";
    public static final String SET_ENUM_KITCHEN_TYPES = "kitchen_types";


    public static Bundle randomiseSettingsToBundle(RandomiseSettings randomiseSettings) {
        Bundle args = new Bundle();
        args.putBoolean(SettingArgKeys.BOOL_USE_BUDGET_TYPE, randomiseSettings.isUseBudgetTypes());
        args.putBoolean(SettingArgKeys.BOOL_USE_FAVORITE, randomiseSettings.isFavorite());
        args.putBoolean(SettingArgKeys.BOOL_USE_LOCATION, randomiseSettings.isUseLocation());
        args.putBoolean(SettingArgKeys.BOOL_USE_KITCHEN_TYPE, randomiseSettings.isUseKitchenTypes());
        args.putDouble(SettingArgKeys.DOUBLE_LATITUDE, randomiseSettings.getLatitude());
        args.putDouble(SettingArgKeys.DOUBLE_LONGITUDE, randomiseSettings.getLongitude());
        args.putDouble(SettingArgKeys.DOUBLE_RANGE, randomiseSettings.getRange());
        args.putIntArray(SettingArgKeys.SET_ENUM_BUDGET_TYPES, CollectionUtils.enumToIntArr(randomiseSettings.getBudgetTypes()));
        args.putIntArray(SettingArgKeys.SET_ENUM_KITCHEN_TYPES, CollectionUtils.enumToIntArr(randomiseSettings.getKitchenTypes()));
        return args;
    }
}
