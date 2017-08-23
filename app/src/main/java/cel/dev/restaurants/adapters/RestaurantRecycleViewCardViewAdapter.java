package cel.dev.restaurants.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.adapters.viewholders.RestaurantViewHolder;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.view.RestaurantCardButtonListener;

/** This is the RecycleViewAdapter for the RecycleView which shows CardViews
 *  containing information about restaurants.
 *
 *  The class also implements the RecycleViewEventCallback which allows the listeners of the
 *  different buttons in each CardView to callback to the adapter since the adapter needs to know
 *  about some of these changes and also to avoid having the listeners do tasks such as
 *  starting new activities
 * */
public class RestaurantRecycleViewCardViewAdapter extends RecyclerView.Adapter<RestaurantViewHolder> implements RecycleViewCardViewEventCallback {

    private List<Restaurant> restaurants;
    private Context context;
    private final RestaurantDAO restaurantDAO;
    private HashSet<Long> expandedRestaurants;

    public RestaurantRecycleViewCardViewAdapter(List<Restaurant> restaurants, Context context, RestaurantDAO restaurantDAO, HashSet<Long> expandedRestaurants) {
        this.restaurants = restaurants;
        this.context = context;
        this.restaurantDAO = restaurantDAO;
        this.expandedRestaurants = expandedRestaurants;
    }

    /** Returns the HashSet<Long> containing the id of each restaurant which information is expanded.
     * */
    public HashSet<Long> getExpandedRestaurants() {
        return expandedRestaurants;
    }

    /** Create a new RestaurantViewHolder using the restaurant_card_layout layout file.
     * */
    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card_layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    /** Binds the information in the restaurant to the ViewHolder
     *  @param position the restaurant's position in the restaurants array
     *
     *  The listeners of the buttons in the view are initialized and some will use this
     *  class as a callback for handling some tasks, such as deleting the restaurant
     *
     *  if the restaurnt's Id exists in the expandedRestaurants HashSet then the restaurants information
     *  should be expanded.
     * */
    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        restaurant.injectImageOntoImageView(holder.getRestaurantImage(), restaurantDAO);
        holder.setRestaurantName(restaurant.getName())
                .setRating(restaurant.getRating())
                .setFavorite(restaurant.isFavorite())
                .setBudgetType(context, BudgetType.sortBudgetType(restaurant.getBudgetTypes()))
                .setKitchenType(context, restaurant.getKitchenTypes())
                .setOnOpenListener(new RestaurantCardButtonListener.OnOpenPressedListener(holder, this, position, restaurant.getId()))
                .setOnFavoriteListener(new RestaurantCardButtonListener.OnFavoritePressedListener(holder, restaurant, restaurantDAO))
                .setOnDeleteRestaurantListener(new RestaurantCardButtonListener.OnDeleteRestaurantListener(holder, this))
                .setOnShowLocationListener(new RestaurantCardButtonListener.OnShowRestaurantLocationListener(holder, this, restaurant))
                .setOnEditRestaurantListener(new RestaurantCardButtonListener.OnEditRestaurantListener(holder, this, restaurant))
                .collapseView();
        /*  Expands the view if it was expanded before rotation change
        * */
        if (expandedRestaurants.contains(restaurant.getId())) {
            holder.toggleExpand();
        }
    }

    /** Returns the size of the List containing the restaurants
     * */
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
    public void onExpandChange(boolean expanded, int position, long restaurantId) {
        if (expanded) {
            expandedRestaurants.add(restaurantId);
        } else {
            expandedRestaurants.remove(restaurantId);
        }
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
                        removeRestaurant(restaurants.remove(adapterPos));
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

    /** Tries to remove the restaurant from the database
     *  If unable a Toast displaying a message that it couldn't will be shown
     * */
    private void removeRestaurant(Restaurant restaurant) {
        if (!restaurantDAO.removeRestaurant(restaurant)) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
        }
    }


    /** Callback for when the user wants to edit a restaurant.
     *  Creates an edit-restaurant Intent and starts the activity.
     * */
    @Override
    public void onEditRestaurant(Restaurant restaurant) {
        context.startActivity(AndroidUtils.createEditRestaurantActivityIntent(context, restaurant));
    }

    /** Callback for when the user wants to show a restaurants location
     *  Creates the show location intent and starts the activity
     * */
    @Override
    public void onShowRestaurantLocation(Restaurant restaurant) {
        context.startActivity(AndroidUtils.createMapActivityIntentWithLatLong(context, restaurant.getLatitude(), restaurant.getLongitude()));
    }


}
