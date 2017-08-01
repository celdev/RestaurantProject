package cel.dev.restaurants.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class RestaurantPlaceholderImage extends Restaurant {

    private int placeholderImageRes;

    public RestaurantPlaceholderImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, FoodType[] foodTypes, int placeholderImageRes) {
        super(name, rating, budgetTypes, latitude, longitude, foodTypes);
        this.placeholderImageRes = placeholderImageRes;
    }

    @Override
    public void injectImageOntoImageView(ImageView imageView) {
        imageView.setImageDrawable(imageView.getResources().getDrawable(placeholderImageRes, imageView.getContext().getTheme()));
    }
}
