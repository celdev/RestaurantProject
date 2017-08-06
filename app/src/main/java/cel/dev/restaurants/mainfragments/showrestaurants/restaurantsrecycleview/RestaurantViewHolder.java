package cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview;

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

class RestaurantViewHolder extends RecyclerView.ViewHolder {

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

    public RestaurantViewHolder setRestaurantName(String name) {
        this.restaurantName.setText(name);
        return this;
    }

    public RestaurantViewHolder setRating(Context context, float rating) {
        this.ratingBar.setRating(rating);
        return this;
    }

    public RestaurantViewHolder setOnOpenListener(View.OnClickListener listener) {
        this.openRestaurantBtn.setOnClickListener(listener);
        return this;
    }


    public RestaurantViewHolder setFavorite(boolean favorite) {
        if (favorite) {
            favoriteBtn.setImageDrawable(AndroidUtils.tintDrawable(favoriteBtn.getContext(), favoriteFull, R.color.favorite));
        } else {
            favoriteBtn.setImageDrawable(favoriteEmpty);
        }
        return this;
    }

    public RestaurantViewHolder setOnFavoriteListener(View.OnClickListener listener) {
        this.favoriteBtn.setOnClickListener(listener);
        return this;
    }

    public RestaurantViewHolder setOnShowLocationListener(View.OnClickListener listener) {
        this.showLocationBtn.setOnClickListener(listener);
        return this;
    }

    public RestaurantViewHolder setOnDeleteRestaurantListener(View.OnClickListener listener) {
        this.deleteRestaurantBtn.setOnClickListener(listener);
        return this;
    }

    public RestaurantViewHolder setOnEditRestaurantListener(View.OnClickListener listener) {
        this.editRestaurantBtn.setOnClickListener(listener);
        return this;
    }

    public RestaurantViewHolder setKitchenType(Context context, KitchenType[] kitchenTypes) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < kitchenTypes.length; i++) {
            text.append(context.getString(kitchenTypes[i].getStringResId()));
            if (i + 1 != kitchenTypes.length) {
                text.append(", ");
            }
        }
        this.kitchenOutput.setText(text.toString());
        return this;
    }

    public RestaurantViewHolder setBudgetType(Context context, BudgetType[] budgetTypes) {
        StringBuilder text = new StringBuilder();
        BudgetType.sortBudgetType(budgetTypes);
        for (int i = 0; i < budgetTypes.length; i++) {
            text.append(context.getString(BudgetType.budgetTypeToString(budgetTypes[i])));
            if (i + 1 != budgetTypes.length) {
                text.append(", ");
            }
        }
        this.budgetOutput.setText(text.toString());
        return this;
    }

    boolean toggleExpand() {
        if (!expanded) {
            expandView();
        } else {
            collapseView();
        }
        setOpenButtonDrawable();
        return expanded;
    }

    private void setOpenButtonDrawable() {
        openRestaurantBtn.setImageDrawable(expanded ? expandLessDrawable : expandMoreDrawable);
    }

    private void expandView() {
        expanded = true;
        expandableLayout.startAnimation(new ExpandableLayoutAnimation(expandableLayout, true));
    }

    public void collapseView() {
        expanded = false;
        expandableLayout.startAnimation(new ExpandableLayoutAnimation(expandableLayout, false));
    }


}
