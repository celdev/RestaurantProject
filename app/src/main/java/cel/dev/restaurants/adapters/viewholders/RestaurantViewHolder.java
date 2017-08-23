package cel.dev.restaurants.adapters.viewholders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cel.dev.restaurants.R;
import cel.dev.restaurants.model.BudgetType;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.CollectionUtils;
import cel.dev.restaurants.view.ExpandableLayoutAnimation;

/** This is the ViewHolder for the RecycleView
 *
 *  This ViewHolder contains
 *      the name of the restaurant
 *      The rating
 *      The image
 *      5 Buttons
 *          Open - toggles (expand/collapse) a part of the CardView which contains
 *                 information about the restaurants such as budget types and delete and edit buttons
 *          Delete
 *          Edit
 *          Show location
 *          Favorite
 *      Kitchen Types
 *      Budget Types
 *
 *  This ViewHolder has a large amount of set-methods which injects information such as the
 *  name of the restaurants and the rating.
 *  These methods implements the fluent pattern which allows for settings multiple properties
 *  without having to do obj.methodA(); obj.methodB(); instead obj.methodA().methodB() can be
 *  done which makes the code a bit more readable in my opinion.
 * */
public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.restaurant_card) CardView cardView;
    @BindView(R.id.restaurant_name) TextView restaurantName;
    @BindView(R.id.restaurant_rating) RatingBar ratingBar;
    @BindView(R.id.restaurant_image) ImageView restaurantImage;
    @BindView(R.id.open_restaurant_info_btn) ImageButton openRestaurantBtn;
    @BindView(R.id.show_restaurant_location_btn) ImageButton showLocationBtn;
    @BindView(R.id.favorite_restaurant_btn) ImageButton favoriteBtn;
    @BindView(R.id.expandable_layout) View expandableLayout;
    @BindView(R.id.delete_restaurant_btn) ImageButton deleteRestaurantBtn;
    @BindView(R.id.edit_restaurant_btn) ImageButton editRestaurantBtn;
    @BindView(R.id.budget_type_card_output) TextView budgetOutput;
    @BindView(R.id.kitchen_type_card_output) TextView kitchenOutput;
    @BindDrawable(R.drawable.ic_expand_more_black_24dp) Drawable expandMoreDrawable;
    @BindDrawable(R.drawable.ic_expand_less_black_24dp) Drawable expandLessDrawable;
    @BindDrawable(R.drawable.ic_favorite_black_24dp) Drawable favoriteFull;
    @BindDrawable(R.drawable.ic_favorite_border_black_24dp) Drawable favoriteEmpty;



    private boolean expanded = false;

    public RestaurantViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, itemView);
        collapseView();
    }

    /** Sets the text to the parameter
     * */
    public RestaurantViewHolder setRestaurantName(String name) {
        this.restaurantName.setText(name);
        return this;
    }

    /** Sets the rating of the rating bar to the parameter */
    public RestaurantViewHolder setRating(float rating) {
        this.ratingBar.setRating(rating);
        return this;
    }

    /** Sets the open restaurant information button listener
     * */
    public RestaurantViewHolder setOnOpenListener(View.OnClickListener listener) {
        this.openRestaurantBtn.setOnClickListener(listener);
        return this;
    }

    /** Sets the favorite status icon
     *  If favorite is true then the icon will be a red full heart
     *  else the icon will be an empty black border heart
     * */
    public RestaurantViewHolder setFavorite(boolean favorite) {
        if (favorite) {
            favoriteBtn.setImageDrawable(AndroidUtils.tintDrawable(favoriteBtn.getContext(), favoriteFull, R.color.favorite));
        } else {
            favoriteBtn.setImageDrawable(favoriteEmpty);
        }
        return this;
    }

    /** Sets the listener for the favorite button */
    public RestaurantViewHolder setOnFavoriteListener(View.OnClickListener listener) {
        this.favoriteBtn.setOnClickListener(listener);
        return this;
    }

    /** Sets the listener for the show restaurant location button */
    public RestaurantViewHolder setOnShowLocationListener(View.OnClickListener listener) {
        this.showLocationBtn.setOnClickListener(listener);
        return this;
    }

    /** Sets the listener for the delete restaurant button */
    public RestaurantViewHolder setOnDeleteRestaurantListener(View.OnClickListener listener) {
        this.deleteRestaurantBtn.setOnClickListener(listener);
        return this;
    }

    /** Set the listener for the edit restaurant button */
    public RestaurantViewHolder setOnEditRestaurantListener(View.OnClickListener listener) {
        this.editRestaurantBtn.setOnClickListener(listener);
        return this;
    }

    /** Set the kitchen types text to the kitchen types of the restaurant
     *  Uses the kitchenEnumToString to convert an array of KitchenType to a String
     *  containing each KitchenTypes @StringRes id string value separated by a comma
     * */
    public RestaurantViewHolder setKitchenType(Context context, KitchenType[] kitchenTypes) {
        this.kitchenOutput.setText(CollectionUtils.kitchenEnumToString(kitchenTypes, context));
        return this;
    }

    /** Sets the budget info text
     * */
    public RestaurantViewHolder setBudgetType(Context context, BudgetType[] budgetTypes) {
        this.budgetOutput.setText(CollectionUtils.budgetEnumToString(budgetTypes,context));
        return this;
    }

    /** expands the card view if it's collapsed or collapse the cardview if it's expanded
     *  changes the drawable of the open button depending on the
     *  state (when collapsed the button will be a down arrow, when expanded the button will be
     *  an up arrow)
     * */
    public boolean toggleExpand() {
        if (!expanded) {
            expandView();
        } else {
            collapseView();
        }
        setOpenButtonDrawable();
        return expanded;
    }

    /** Sets the drawable of the open button icon, if the information is expanded then the
     *  icon should be a "collapse information"-icon else the icon should be an "expand information"-icon
     * */
    private void setOpenButtonDrawable() {
        openRestaurantBtn.setImageDrawable(expanded ? expandLessDrawable : expandMoreDrawable);
    }

    /** Expands the view by starting the ExpandableLayoutAnimation
     * */
    private void expandView() {
        expanded = true;
        expandableLayout.startAnimation(new ExpandableLayoutAnimation(expandableLayout, true));
    }

    /** Collapses the view by starting the ExpandLayoutAnimation (with the constructor param
     *  expand to false which will cause the animation to collapse instead of expand
     * */
    public void collapseView() {
        expanded = false;
        expandableLayout.startAnimation(new ExpandableLayoutAnimation(expandableLayout, false));
    }

    /** Returns the restaurantImage ImageView
     * */
    public ImageView getRestaurantImage() {
        return restaurantImage;
    }
}
