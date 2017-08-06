package cel.dev.restaurants.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.model.RestaurantCustomImage;
import cel.dev.restaurants.repository.db.RestaurantCRUD;
import cel.dev.restaurants.repository.db.RestaurantDB;
import cel.dev.restaurants.repository.db.RestaurantDBHelper;
import cel.dev.restaurants.utils.PictureUtils;

public class RestaurantDAOImpl implements RestaurantDAO {

    private SQLiteDatabase db;
    private RestaurantCRUD restaurantCRUD;

    public RestaurantDAOImpl(Context context) {
        db = new RestaurantDBHelper(context).getWritableDatabase();
        restaurantCRUD = new RestaurantDB(context);
    }

    @Override
    public Restaurant getRestaurantById(long id) {
        return restaurantCRUD.getRestaurantById(id);
    }

    @Override
    public List<Restaurant> getRestaurantsByIds(List<Long> ids) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (Long id : ids) {
            restaurants.add(getRestaurantById(id));
        }
        return restaurants;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantCRUD.getAllRestaurants();
    }

    @Override
    public List<Restaurant> getRestaurantsByBudgetType(BudgetType budgetType) {
        return null;
    }

    @Override
    public boolean saveRestaurant(Restaurant restaurant) {
        return restaurantCRUD.saveOrUpdateRestaurant(restaurant);
    }

    @Override
    public void injectImageOntoImageView(ImageView imageView, Restaurant restaurant) {
        if (restaurant instanceof RestaurantCustomImage) {
            try {
                byte[] image = restaurantCRUD.getImageOfRestaurant((RestaurantCustomImage) restaurant);
                imageView.setImageBitmap(PictureUtils.byteArrayToBitMap(image));
            } catch (Exception e) {
                Log.e("restaurantdao", "injectImageOntoImageView: ", e);
            }
        }
    }

    @Override
    public void setRestaurantFavorite(Restaurant restaurant) {
        restaurantCRUD.setRestaurantFavorite(restaurant);
    }

    @Override
    public List<Restaurant> getRestaurantsByLocation(double lat, double lon, double range) {
        List<Long> restaurantIdsByLocation = restaurantCRUD.getRestaurantIdsByLocation(lat, lon, range);
        List<Restaurant> restaurants = new ArrayList<>();
        for (long id : restaurantIdsByLocation) {
            restaurants.add(restaurantCRUD.getRestaurantById(id));
        }
        return restaurants;
    }
}
