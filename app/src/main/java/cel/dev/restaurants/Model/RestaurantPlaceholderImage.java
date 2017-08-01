package cel.dev.restaurants.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RestaurantPlaceholderImage extends Restaurant {

    private int placeholderImageRes;

    public RestaurantPlaceholderImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, FoodType[] foodTypes, int placeholderImageRes) {
        super(name, rating, budgetTypes, latitude, longitude, foodTypes);
        this.placeholderImageRes = placeholderImageRes;
    }

    @Override
    public Bitmap getRestaurantImage(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), placeholderImageRes);
    }
}
