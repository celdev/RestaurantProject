package cel.dev.restaurants.mainfragments.randomrestaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;
import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.ExpandableLayoutAnimation;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.CollectionUtils;


public class RandomRestaurantFragment extends Fragment implements FABFragmentHandler, RandomRestaurantMVP.View {

    public RandomRestaurantFragment() {
    }

    public static RandomRestaurantFragment newInstance() {
        return new RandomRestaurantFragment();
    }

    @BindView(R.id.restaurant_name) TextView restaurantName;
    @BindView(R.id.restaurant_rating) RatingBar ratingBar;
    @BindView(R.id.restaurant_image) ImageView restaurantImage;
    @BindView(R.id.budget_type_card_output) TextView budgetTypesText;
    @BindView(R.id.kitchen_type_card_output) TextView kitchenTypesText;
    @BindView(R.id.expandable_layout) LinearLayout expandableView;
    @BindView(R.id.delete_restaurant_btn) ImageButton deleteRestaurantBtn;
    @BindView(R.id.edit_restaurant_btn) ImageButton editRestaurantBtn;
    @BindView(R.id.show_restaurant_location_btn) ImageButton restaurantLocationBtn;
    @BindView(R.id.favorite_restaurant_btn) ImageButton restaurantFavoriteBtn;
    @BindView(R.id.open_restaurant_info_btn) ImageButton openRestaurantBtn;
    @BindView(R.id.no_restaurant_found_layout) View noRestaurantsLayout;
    @BindView(R.id.random_buttons_layout) View randomRestaurantButtonLayout;
    @BindView(R.id.restaurant_card) CardView restaurantCard;

    @BindDrawable(R.drawable.ic_favorite_border_black_24dp)
    Drawable favoriteEmpty;
    @BindDrawable(R.drawable.ic_favorite_black_24dp)
    Drawable favoriteFull;

    private RandomRestaurantMVP.Presenter presenter;
    private boolean expanded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_restaurant, container, false);
        ButterKnife.bind(this, view);
        presenter = new PresenterImpl(this, getContext());
        presenter.loadRestaurant();
        return view;
    }


    @Override
    public void handleFABClick() {
    }

    @Override
    public void handleNoRestaurantsFound() {
        randomRestaurantButtonLayout.setVisibility(View.GONE);
        restaurantCard.setVisibility(View.GONE);
        noRestaurantsLayout.setVisibility(View.VISIBLE);
    }

    private void setRestaurantFound() {
        restaurantCard.setVisibility(View.VISIBLE);
        randomRestaurantButtonLayout.setVisibility(View.VISIBLE);
        noRestaurantsLayout.setVisibility(View.GONE);
    }


    @Override
    public void injectRestaurant(final Restaurant restaurant, RestaurantDAO restaurantDAO) {
        setRestaurantFound();
        restaurantName.setText(restaurant.getName());
        restaurant.injectImageOntoImageView(restaurantImage, restaurantDAO);
        kitchenTypesText.setText(CollectionUtils.kitchenEnumToString(restaurant.getKitchenTypes(), getContext()));
        budgetTypesText.setText(CollectionUtils.budgetEnumToString(restaurant.getBudgetTypes(), getContext()));
        ratingBar.setRating(restaurant.getRating());
        if (restaurant.isFavorite()) {
            restaurantFavoriteBtn.setImageDrawable(AndroidUtils.tintDrawable(getContext(), favoriteFull, R.color.favorite));
        } else {
            restaurantFavoriteBtn.setImageDrawable(favoriteEmpty);
        }
        editRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(AndroidUtils.createEditRestaurantActivityIntent(getContext(), restaurant));
            }
        });
    }

    @OnClick(R.id.edit_restaurant_btn)
    public void editRestaurantClicked(View view) {
        presenter.editCurrentRestaurant();
    }


    @OnClick(R.id.delete_restaurant_btn)
    public void deleteRestaurantClicked(View view) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_restaurant_dialog_title)
                .setMessage(R.string.are_you_sure_delete)
                .setPositiveButton(R.string.delete_restaurant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteCurrentRestaurant();
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


    @OnClick(R.id.open_restaurant_info_btn)
    public void expandClicked(View view) {
        if (expanded) {
            expandableView.startAnimation(new ExpandableLayoutAnimation(expandableView, false));
            expanded = false;
        } else {
            expandableView.startAnimation(new ExpandableLayoutAnimation(expandableView, true));
            expanded = true;
        }
    }

    @OnClick(R.id.favorite_restaurant_btn)
    public void favoriteThisRestaurantClicked(View view) {
        presenter.favoriteRestaurantClicked();
    }

    @OnClick(R.id.show_restaurant_location_btn)
    public void showLocationClicked(View view) {
        presenter.showRestaurantLocation();
    }


    @OnClick(R.id.restaurant_closer)
    public void showRestaurantThatIsCloserPressed(View view) {
        presenter.showNewRestaurant(RandomChoice.CLOSER);
    }

    @OnClick(R.id.restaurant_cheaper)
    public void showRestaurantThatIsCheaperPressed(View view) {
        presenter.showNewRestaurant(RandomChoice.CHEAPER);
    }

    @OnClick(R.id.restaurant_different_food)
    public void showRestaurantDifferentFoodType(View view) {
        presenter.showNewRestaurant(RandomChoice.DIFFERENT_FOOD);
    }

    @OnClick(R.id.just_a_different_restaurant)
    public void showADifferentRestaurant(View view) {
        presenter.showNewRestaurant(RandomChoice.NO_CHANGE);
    }

    @OnClick(R.id.reset_settings_button)
    public void resetSettingsPressed(View view) {
        presenter.resetSettings();
    }

}