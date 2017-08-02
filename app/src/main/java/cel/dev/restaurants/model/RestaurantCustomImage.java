package cel.dev.restaurants.model;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class RestaurantCustomImage extends Restaurant {

    private byte[] image;

    public RestaurantCustomImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, KitchenType[] kitchenTypes, byte[] image, boolean favorite) {
        super(name, rating, budgetTypes, latitude, longitude, kitchenTypes, favorite);
        this.image = image;
    }

    @Override
    public void injectImageOntoImageView(ImageView imageView) {
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

}
