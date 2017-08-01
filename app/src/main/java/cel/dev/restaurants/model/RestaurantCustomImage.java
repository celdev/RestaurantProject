package cel.dev.restaurants.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class RestaurantCustomImage extends Restaurant {

    private byte[] image;

    public RestaurantCustomImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, FoodType[] foodTypes, byte[] image) {
        super(name, rating, budgetTypes, latitude, longitude, foodTypes);
        this.image = image;
    }

    @Override
    public void injectImageOntoImageView(ImageView imageView) {
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

}
