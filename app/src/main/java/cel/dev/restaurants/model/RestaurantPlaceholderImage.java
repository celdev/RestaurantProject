package cel.dev.restaurants.model;

import android.widget.ImageView;

import cel.dev.restaurants.persistance.RestaurantDAO;

/** Implementation of the Restaurant with a placeholder-image
 * */
public class RestaurantPlaceholderImage extends Restaurant {

    private int placeholderImageRes;

    public RestaurantPlaceholderImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, KitchenType[] kitchenTypes, int placeholderImageRes, boolean favorite) {
        super(name, rating, budgetTypes, latitude, longitude, kitchenTypes, favorite);
        this.placeholderImageRes = placeholderImageRes;
    }

    /** This will "inject" the placeholder image into the ImageView
     * */
    @Override
    public void injectImageOntoImageView(ImageView imageView, RestaurantDAO restaurantDAO) {
        imageView.setImageDrawable(imageView.getResources().getDrawable(placeholderImageRes, imageView.getContext().getTheme()));
    }
}
