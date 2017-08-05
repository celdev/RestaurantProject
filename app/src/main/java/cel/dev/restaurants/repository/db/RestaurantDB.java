package cel.dev.restaurants.repository.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cel.dev.restaurants.R;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.model.RestaurantPlaceholderImage;
import cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols;
import cel.dev.restaurants.utils.AndroidUtils;

import static cel.dev.restaurants.repository.db.RestaurantDbSchema.*;

public class RestaurantDB implements RestaurantCRUD {

    private static final String TAG = "DBClass";
    private SQLiteDatabase db;

    public RestaurantDB(Context context) {
        RestaurantDBHelper restaurantDBHelper = new RestaurantDBHelper(context);
        db = restaurantDBHelper.getWritableDatabase();
    }

    @Override
    public boolean saveOrUpdateRestaurant(Restaurant restaurant) {
        boolean success;
        ContentValues values = new ContentValues();
        values.put(Cols.NAME, restaurant.getName());
        values.put(Cols.RATING, restaurant.getRating());
        values.put(Cols.FAVORITE, restaurant.isFavorite());
        boolean hasImage = restaurant instanceof RestaurantCustomImage;
        values.put(Cols.HAS_IMAGE, hasImage);
        if (hasImage) {
            values.put(Cols.IMAGE, ((RestaurantCustomImage) restaurant).getImageByteArray());
        }
        values.put(Cols.BUDGET_TYPES, AndroidUtils.DBUtils.enumsToString(restaurant.getBudgetTypes()));
        values.put(Cols.KITCHEN_TYPES, AndroidUtils.DBUtils.enumsToString(restaurant.getBudgetTypes()));
        values.put(Cols.LOCATION_LATITUDE, restaurant.getLatitude());
        values.put(Cols.LOCATION_LONGITUDE, restaurant.getLongitude());
        if (Restaurant.restaurantHasIdSet(restaurant)) {
            values.put(Cols.ID, restaurant.getId());
            success = 0 != db.update(Table.NAME, values, Cols.ID + " = ?", new String[]{"" + restaurant.getId()});
        } else {
            long restaurantId = db.insert(Table.NAME, null, values);
            restaurant.setId(restaurantId);
            success = true;
        }
        return success;
    }

    @Override
    @Nullable
    public Restaurant getRestaurantById(long id) {
        Cursor cursor = null;
        Restaurant restaurant = null;
        try {
            cursor = db.query(Table.NAME, Projections.PROJECTION_ALL, Selections.SELECTION_ID, new String[]{"" + id}, null, null, null);
            cursor.moveToFirst();
            restaurant = cursorToRestaurant(cursor, Projections.PROJECTION_ALL);
        } catch (Exception e) {
            Log.e(TAG, "getRestaurantById: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        Cursor cursor = null;
        Restaurant restaurant = null;
        try {
            cursor = db.query(Table.NAME, Projections.PROJECTION_ALL, null, null, null, null, null);
            while (cursor.moveToNext()) {
                restaurants.add(cursorToRestaurant(cursor, Projections.PROJECTION_ALL));
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllRestaurants: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return restaurants;
    }

    private Restaurant cursorToRestaurant(Cursor cursor, String[] projections) throws Exception {
        Map<String, Integer> index = createColumnNameToColumnIndexMap(cursor, projections);
        long id = cursor.getLong(index.get(Cols.ID));
        float rating = Float.valueOf(cursor.getString(index.get(Cols.RATING)));
        boolean isFavorite = cursor.getInt(index.get(Cols.FAVORITE)) != 0;
        double lat = cursor.getDouble(index.get(Cols.LOCATION_LATITUDE));
        double lon = cursor.getDouble(index.get(Cols.LOCATION_LONGITUDE));
        List<KitchenType> kitchenTypes = AndroidUtils.DBUtils.stringToEnum(cursor.getString(index.get(Cols.KITCHEN_TYPES)), KitchenType.class);
        List<BudgetType> budgetTypes = AndroidUtils.DBUtils.stringToEnum(cursor.getString(index.get(Cols.BUDGET_TYPES)), BudgetType.class);
        boolean hasImage = cursor.getInt(index.get(Cols.HAS_IMAGE)) != 0;
        String name = cursor.getString(index.get(Cols.NAME));
        Restaurant restaurant;
        if (hasImage) {
            byte[] image = cursor.getBlob(index.get(Cols.IMAGE));
            restaurant = new RestaurantCustomImage(name, rating, budgetTypes.toArray(new BudgetType[budgetTypes.size()]),
                    lat, lon, kitchenTypes.toArray(new KitchenType[kitchenTypes.size()]), image, isFavorite);
            restaurant.setId(id);
        } else {
            restaurant = new RestaurantPlaceholderImage(name, rating, budgetTypes.toArray(new BudgetType[budgetTypes.size()]),
                    lat, lon, kitchenTypes.toArray(new KitchenType[kitchenTypes.size()]), R.drawable.restaurant_placeholder, isFavorite);
            restaurant.setId(id);
        }
        return restaurant;
    }

    private Map<String, Integer> createColumnNameToColumnIndexMap(Cursor cursor, String[] projection) {
        Map<String, Integer> map = new HashMap<>();
        for (String p : projection) {
            map.put(p, cursor.getColumnIndex(p));
        }
        return map;
    }
}
