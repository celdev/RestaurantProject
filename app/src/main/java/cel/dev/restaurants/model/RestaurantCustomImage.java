package cel.dev.restaurants.model;

import android.util.Log;
import android.widget.ImageView;

import cel.dev.restaurants.persistance.RestaurantDAO;


/** Implementation of the the Restaurant which has a custom image (stored as a byte array)
 *
 *  Has two constructors, one is used when the image should be passed around and one is used
 *  when the image should be retrieved when it's needed.
 *
 *  This is done so e.g. the database will return 100 restaurants, 100 restaurant images doesn't have
 *  to be stored in memory when only ~3 restaurants can be shown at the same time.
 * */
public class RestaurantCustomImage extends Restaurant {

    private byte[] image;

    public RestaurantCustomImage(String name, float rating, BudgetType[] budgetTypes, double latitude, double longitude, KitchenType[] kitchenTypes, byte[] image, boolean favorite) {
        super(name, rating, budgetTypes, latitude, longitude, kitchenTypes, favorite);
        this.image = image;
    }

    public RestaurantCustomImage(String name, float rating, BudgetType[] budgetTypes, double lat, double lon, KitchenType[] kitchenTypes, boolean favorite) {
        super(name, rating, budgetTypes, lat, lon, kitchenTypes, favorite);
    }

    /** This method allows for retrieving an image from the database when the view needs it
     * */
    @Override
    public void injectImageOntoImageView(ImageView imageView, RestaurantDAO restaurantDAO) {
        try {
            restaurantDAO.injectImageOntoImageView(imageView, this);
        } catch (Exception e) {
            Log.e("Restaurant", "injectImageOntoImageView: ", e);
        }
    }

    /** returns the image stored as a byte array
     * */
    public byte[] getImageByteArray() {
        return image;
    }

}
