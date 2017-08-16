package cel.dev.restaurants.mainfragments.randomrestaurant;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;
import cel.dev.restaurants.locationutils.LocationRequestCallback;
import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.ExpandableLayoutAnimation;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.CollectionUtils;
import cel.dev.restaurants.utils.LocationUtils;

/** This fragment displays a random restaurant and
 *  allows the user to make certain choices in order to
 *  find a random restaurant which satisfy their needs (i.e. location and budget)
 * */
public class RandomRestaurantFragment extends Fragment implements FABFragmentHandler, RandomRestaurantMVP.View, OnSuccessListener<Location>, OnCompleteListener<Location> {


    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "random rest";

    public RandomRestaurantFragment() {
    }

    public static RandomRestaurantFragment newInstance() {
        return new RandomRestaurantFragment();
    }

    @BindView(R.id.restaurant_name)
    TextView restaurantName;
    @BindView(R.id.restaurant_rating)
    RatingBar ratingBar;
    @BindView(R.id.restaurant_image)
    ImageView restaurantImage;
    @BindView(R.id.budget_type_card_output)
    TextView budgetTypesText;
    @BindView(R.id.kitchen_type_card_output)
    TextView kitchenTypesText;
    @BindView(R.id.expandable_layout)
    LinearLayout expandableView;
    @BindView(R.id.delete_restaurant_btn)
    ImageButton deleteRestaurantBtn;
    @BindView(R.id.edit_restaurant_btn)
    ImageButton editRestaurantBtn;
    @BindView(R.id.show_restaurant_location_btn)
    ImageButton restaurantLocationBtn;
    @BindView(R.id.favorite_restaurant_btn)
    ImageButton restaurantFavoriteBtn;
    @BindView(R.id.open_restaurant_info_btn)
    ImageButton openRestaurantBtn;
    @BindView(R.id.no_restaurant_found_layout)
    View noRestaurantsLayout;
    @BindView(R.id.random_buttons_layout)
    View randomRestaurantButtonLayout;
    @BindView(R.id.restaurant_card)
    CardView restaurantCard;

    @BindDrawable(R.drawable.ic_favorite_border_black_24dp)
    Drawable favoriteEmpty;
    @BindDrawable(R.drawable.ic_favorite_black_24dp)
    Drawable favoriteFull;

    private RandomRestaurantMVP.Presenter presenter;
    private boolean expanded, hasLocation;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_restaurant, container, false);
        ButterKnife.bind(this, view);
        presenter = new PresenterImpl(this, getContext());
        handleNoRestaurantsFound();
        presenter.onRequestingLocation();
        return view;
    }

    @Override
    public void showLoadingLocationDialog() {
        progressDialog = AndroidUtils.
                createProgressDialog(getContext(), R.string.loading_location, false);
        progressDialog.show();
    }

    @Override
    public void requestLocation() {
        LocationUtils.requestLocation(getContext(), this, this, new LocationRequestCallback() {
            @Override
            public void requestLocationCallback() {
                LocationUtils.requestLocationPermission(getActivity(), REQUEST_LOCATION);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /** This fragment doesn't need a FAB-button but
     *  is required to implement this method since it will be contained in the MainActivity
     * */
    @Override
    public void handleFABClick() {
    }

    /** Displays text stating that no restaurants was found using the current settings
     *  and allows the user to reset the settings and start over
     * */
    @Override
    public void handleNoRestaurantsFound() {
        randomRestaurantButtonLayout.setVisibility(View.GONE);
        restaurantCard.setVisibility(View.GONE);
        noRestaurantsLayout.setVisibility(View.VISIBLE);
    }

    /** Sets the state of the view to display a restaurant
     *  (hides messages about that no restaurants was found)
     * */
    private void setRestaurantFound() {
        restaurantCard.setVisibility(View.VISIBLE);
        randomRestaurantButtonLayout.setVisibility(View.VISIBLE);
        noRestaurantsLayout.setVisibility(View.GONE);
    }

    /** Injects a restaurant into the view
     *  sets and creates the name, rating, location, button click listeners...
     *  The view which displays the restaurant is the cardview used in the RecycleView in
     *  the NearbyFragment and ShowRestaurants-fragment
     * */
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

    /** #################################################################################
     *  #################################################################################
     *  #################################################################################
     *  #################################################################################
     *  OnClick-Listeners for the buttons in the Restaurant-CardView
     * */
    @OnClick(R.id.edit_restaurant_btn)
    public void editRestaurantClicked(View view) {
        presenter.editCurrentRestaurant();
    }

    /** OnClick-handler for the delete restaurant button
     *  Shows a dialog asking if the user wants to delete this restaurant
     *  if so, deletes the restaurant, otherwise hides the dialog
     * */
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

    /** Expands the CardView which will show the presented restaurants information
     *  and some buttons which previously wasn't shown
     *  The expansion is done using an animation which expands the view during a
     *  certain (small) time
     * */
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


    /** #################################################################################
     *  #################################################################################
     *  #################################################################################
     *  OnClick-Listeners for the choices the user can make in order to alter the randomise
     *  settings after being presented with a random restaurant
     *  The choices the user can make is to try to find restaurants that are
     *      Closer, cheaper, have other types of food, or just to find another restaurant
     *      using the same settings as before
     *  Each of these listeners passes a RandomChoice into the presenter which will
     *  alter the settings and load a new restaurants using the new settings
     * */
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
        hasLocation = false;
        presenter.resetSettings();
    }

    /** Callback for when the location service returns a location
     * */
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            hasLocation = true;
            presenter.injectLocation(location.getLatitude(), location.getLongitude());
        } else {
            hideLoadingDialog();
        }
    }

    /** Similar function to the onSuccess callback
     *  The location service seems to have a bug on my phone
     *  which result in the onSuccess retrieving a null location
     *  However, this method (which is called directly after onSuccess)
     *  always gets the location without any problems
     *  If the location wasn't set in the onSuccess-method (=hasLocation will be false)
     *  then use the location in this method
     * */
    @Override
    public void onComplete(@NonNull Task<Location> task) {
        Location location = task.getResult();
        if (location != null) {
            if (!hasLocation) {
                hasLocation = true;
                presenter.injectLocation(location.getLatitude(), location.getLongitude());
            }
        } else {
            Toast.makeText(getContext(), R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }


    }
}