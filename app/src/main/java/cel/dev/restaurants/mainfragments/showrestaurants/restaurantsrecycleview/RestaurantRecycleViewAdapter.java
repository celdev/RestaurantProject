package cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.utils.AndroidUtils;

public class RestaurantRecycleViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> implements RecycleViewOnExpandChangeCallback {

    private List<Restaurant> restaurants;
    private Context context;
    private final RestaurantDAO restaurantDAO;

    public RestaurantRecycleViewAdapter(List<Restaurant> restaurants, Context context, RestaurantDAO restaurantDAO) {
        this.restaurants = restaurants;
        this.context = context;
        this.restaurantDAO = restaurantDAO;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card_layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    /** Binds the information in the restaurant to the view
     *  @param position the restaurant's position in the restaurants array
     * */
    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        restaurant.injectImageOntoImageView(holder.restaurantImage, restaurantDAO);
        holder.setRestaurantName(restaurant.getName())
                .setRating(context, restaurant.getRating())
                .setFavorite(restaurant.isFavorite())
                .setBudgetType(context, BudgetType.sortBudgetType(restaurant.getBudgetTypes()))
                .setKitchenType(context, restaurant.getKitchenTypes())
                .setOnOpenListener(new RestaurantCardButtonListener.OnOpenPressedListener(holder, this, position))
                .setOnFavoriteListener(new RestaurantCardButtonListener.OnFavoritePressedListener(holder, restaurant, restaurantDAO))
                .setOnDeleteRestaurantListener(new RestaurantCardButtonListener.OnDeleteRestaurantListener(holder, this, restaurant))
                .setOnShowLocationListener(new RestaurantCardButtonListener.OnShowRestaurantLocationListener(holder, this, restaurant))
                .setOnEditRestaurantListener(new RestaurantCardButtonListener.OnEditRestaurantListener(holder, this, restaurant))
                .collapseView();
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /** Prepare for needing to callback to the recycle view in case of
     *  i.e. needing to remove the item if the user presses the
     *  delete restaurant button
     * */
    @Override
    public void onExpandChange(boolean expanded, int position) {
        //notifyItemChanged(position);
    }

    /** Creates and shows a dialog which askes the user if the user really wants
     *  to delete this restaurant
     *  if the user wants to delete the restaurant
     *  will be deleted, otherwise the dialog will just be dismissed
     * */
    @Override
    public void onDeleteRestaurant(final int adapterPos) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_restaurant_dialog_title)
                .setMessage(R.string.are_you_sure_delete)
                .setPositiveButton(R.string.delete_restaurant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("recycle", "onClick: trying to delete restaurant with " + adapterPos);
                        restaurants.remove(adapterPos);
                        notifyItemRemoved(adapterPos);
                        notifyItemRangeChanged(adapterPos, restaurants.size());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onEditRestaurant(Restaurant restaurant) {
        context.startActivity(AndroidUtils.createEditRestaurantActivityIntent(context, restaurant));
    }

    @Override
    public void onShowRestaurantLocation(Restaurant restaurant) {
        context.startActivity(AndroidUtils.createMapActivityIntentWithLatLong(context, restaurant.getLatitude(), restaurant.getLongitude()));
    }


}
