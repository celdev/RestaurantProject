package cel.dev.restaurants.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RestaurantCustomImage extends Restaurant {

    private byte[] image;

    public RestaurantCustomImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, FoodType[] foodTypes, byte[] image) {
        super(name, rating, budgetTypes, latitude, longitude, foodTypes);
        this.image = image;
    }

    @Override
    public Bitmap getRestaurantImage(Context context) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
