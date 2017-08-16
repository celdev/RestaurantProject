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

/** Implementation of the RestaurantCRUD contract
 * */
public class RestaurantDB implements RestaurantCRUD {

    private static final String TAG = "DBClass";
    private SQLiteDatabase db;

    public RestaurantDB(Context context) {
        RestaurantDBHelper restaurantDBHelper = new RestaurantDBHelper(context);
        db = restaurantDBHelper.getWritableDatabase();
    }

    /** saves or updates the restaurant passed as the parameter
     *  if the restaurants id is set then the restaurant will be updated instead of created
     * */
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
        values.put(Cols.KITCHEN_TYPES, AndroidUtils.DBUtils.enumsToString(restaurant.getKitchenTypes()));
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
    public void deleteAllRestaurants() {
        db.delete(RestaurantDbSchema.Table.NAME, null, null);
    }

    /** Returns (if it exist) a restaurant by id
     * */
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

    /** returns all restaurants
     * */
    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(Table.NAME, Projections.PROJECTION_ALL_BUT_BYTE_IMAGE, null, null, null, null, null);
            while (cursor.moveToNext()) {
                restaurants.add(cursorToRestaurant(cursor, Projections.PROJECTION_ALL_BUT_BYTE_IMAGE));
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

    /** converts a Cursor into Restaurant
     *  if the restaurant has an Image (hasImage = true) then the restaurant created will be a
     *  RestaurantCustomImage otherwise the restaurant created will be a RestaurantPlaceholderImage
     * */
    private Restaurant cursorToRestaurant(Cursor cursor, String[] projections) throws Exception {
        Map<String, Integer> cols = createColumnNameToColumnIndexMap(cursor, projections);
        long id = cursor.getLong(cols.get(Cols.ID));
        float rating = Float.valueOf(cursor.getString(cols.get(Cols.RATING)));
        boolean isFavorite = cursor.getInt(cols.get(Cols.FAVORITE)) != 0;
        double lat = cursor.getDouble(cols.get(Cols.LOCATION_LATITUDE));
        double lon = cursor.getDouble(cols.get(Cols.LOCATION_LONGITUDE));
        List<KitchenType> kitchenTypes = AndroidUtils.DBUtils.stringToEnum(cursor.getString(cols.get(Cols.KITCHEN_TYPES)), KitchenType.class);
        List<BudgetType> budgetTypes = AndroidUtils.DBUtils.stringToEnum(cursor.getString(cols.get(Cols.BUDGET_TYPES)), BudgetType.class);
        boolean hasImage = cursor.getInt(cols.get(Cols.HAS_IMAGE)) != 0;
        String name = cursor.getString(cols.get(Cols.NAME));
        Restaurant restaurant;
        if (hasImage) {
            restaurant = new RestaurantCustomImage(name, rating, budgetTypes.toArray(new BudgetType[budgetTypes.size()]),
                    lat, lon, kitchenTypes.toArray(new KitchenType[kitchenTypes.size()]), isFavorite);
            restaurant.setId(id);
        } else {
            restaurant = new RestaurantPlaceholderImage(name, rating, budgetTypes.toArray(new BudgetType[budgetTypes.size()]),
                    lat, lon, kitchenTypes.toArray(new KitchenType[kitchenTypes.size()]), R.drawable.restaurant_placeholder, isFavorite);
            restaurant.setId(id);
        }
        return restaurant;
    }

    /** Helper-method for creating a Map containing
     *  Column-name -> column index
     *  which can be used when converting a Cursor into a Restaurant-object
     * */
    private Map<String, Integer> createColumnNameToColumnIndexMap(Cursor cursor, String[] projection) {
        Map<String, Integer> map = new HashMap<>();
        for (String p : projection) {
            map.put(p, cursor.getColumnIndex(p));
        }
        return map;
    }

    /** Retrieves the image of a RestaurantCustomImage
     * */
    @Override
    public byte[] getImageOfRestaurant(RestaurantCustomImage restaurantCustomImage) throws Exception {
        Cursor cursor = db.query(Table.NAME, Projections.PROJECTION_IMAGE, Selections.SELECTION_ID, new String[]{"" + restaurantCustomImage.getId()}, null, null, null);
        if (cursor.moveToFirst()) {
            byte[] image = cursor.getBlob(0);
            cursor.close();
            return image;
        }else {
            cursor.close();
            throw new Exception();
        }
    }

    /** saves the restaurants favorite status
     * */
    @Override
    public void setRestaurantFavorite(Restaurant restaurant) {
        ContentValues values = new ContentValues();
        values.put(Cols.FAVORITE, restaurant.isFavorite());
        db.update(Table.NAME, values, Selections.SELECTION_ID, new String[]{"" + restaurant.getId()});
    }

    /** removes the restaurant
     * */
    @Override
    public boolean removeRestaurant(Restaurant restaurant) {
        return 0 < db.delete(Table.NAME, Selections.SELECTION_ID, new String[]{"" + restaurant.getId()});
    }

    /** returns all restaurant ids within a certain range of parameters lat and lon
     * */
    @Override
    public List<Long> getRestaurantIdsByLocation(double lat, double lon, double range) {
        Cursor cursor = db.rawQuery(
                "SELECT " + Cols.ID + " FROM " + Table.NAME + " WHERE " +
                        Cols.LOCATION_LATITUDE + " < ? AND " +
                        Cols.LOCATION_LATITUDE + " > ? AND " +
                        Cols.LOCATION_LONGITUDE + " < ? AND " +
                        Cols.LOCATION_LONGITUDE + " > ?"
                , new String[]{
                        "" + (lat + range),
                        "" + (lat - range),
                        "" + (lon + range),
                        "" + (lon - range)
                }
        );

        List<Long> ids = new ArrayList<>();
        while (cursor.moveToNext()) {
            ids.add(cursor.getLong(0));
        }
        cursor.close();
        return ids;
    }

}
