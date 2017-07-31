package cel.dev.restaurants.Model;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

class RestaurantPlaceholderImage extends Restaurant {

    private int placeholderImageRes;

    public RestaurantPlaceholderImage(String name, int rating, BudgetType[] budgetTypes, String latitude, String longitude, FoodType[] foodTypes, int placeholderImageRes) {
        super(name, rating, budgetTypes, latitude, longitude, foodTypes);
        this.placeholderImageRes = placeholderImageRes;
    }

    @Override
    public Bitmap getRestaurantImage(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), placeholderImageRes);
    }
}
