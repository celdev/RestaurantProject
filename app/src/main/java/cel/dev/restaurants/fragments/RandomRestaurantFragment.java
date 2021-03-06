package cel.dev.restaurants.fragments;

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
import cel.dev.restaurants.uicontracts.LocationRequestCallback;
import cel.dev.restaurants.view.RandomChoice;
import cel.dev.restaurants.presenterimpl.RandomRestaurantPresenterImpl;
import cel.dev.restaurants.uicontracts.FABFragmentHandler;
import cel.dev.restaurants.utils.PermissionUtils;
import cel.dev.restaurants.view.ExpandableLayoutAnimation;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.uicontracts.RandomRestaurantMVP;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.CollectionUtils;
import cel.dev.restaurants.utils.LocationUtils;
import cel.dev.restaurants.view.RandomiseSettings;

/** This fragment displays a random restaurant and
 *  allows the user to make certain choices in order to
 *  find a random restaurant which satisfy their needs (i.e. location and budget)
 * */
public class RandomRestaurantFragment extends Fragment implements FABFragmentHandler, RandomRestaurantMVP.View, OnSuccessListener<Location>, OnCompleteListener<Location> {


    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "random rest";
    private RandomState randomState;

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
    private boolean firstLoadedRestaurant = true;
    private ProgressDialog progressDialog;
    private AlertDialog deleteDialog;


    /** Inflates the fragment_random_restaurant layout file and binds Butterknife to this so
     *  that the @BindView etc. above works.
     *  sets the state to no restaurants found which sets
     *  some of the views visibility-status to the appropriate value
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_restaurant, container, false);
        ButterKnife.bind(this, view);
        handleNoRestaurantsFound();
        return view;
    }

    /** If the presenter is null create a new presenter
     *
     *  if the randomState instance variable isn't null then we can use it to recreate
     *  the state of the presenter after an orientation change
     *  the randomise settings and the id of the previoulsy shown restaurant is
     *  injected into the presenter
     *
     *  if the randomState is null then
     *  Ask the presenter to retrieve the location of the user
     * */
    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null) {
            presenter = new RandomRestaurantPresenterImpl(this, getContext());
        }
        if (randomState != null) {
            presenter.injectState(randomState.randomiseSettings, randomState.restaurantId);
        } else {
            presenter.onRequestingLocation();
        }
    }

    /** Creates and shows the loading location progress dialog
     * */
    @Override
    public void showLoadingLocationDialog() {
        progressDialog = AndroidUtils.
                createProgressDialog(getContext(), R.string.loading_location, false);
        progressDialog.show();
    }

    /** Request the location of the user using the LocationUtils
     *  if the application doesn't have location permission then the permission will be requested
     * */
    @Override
    public void requestLocation() {
        LocationUtils.requestLocation(getContext(), this, this, new LocationRequestCallback() {
            @Override
            public void requestLocationCallback() {
                LocationUtils.requestLocationPermission(getActivity(), REQUEST_LOCATION);
            }
        });
    }

    /** Handles the result of the location permission request
     *  if the result is ok then the location will be requested again
     *
     *  else a toast will be shown with an error message
     *  and the location permission will be requested again
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(grantResults[0])) {
            if (requestCode == REQUEST_LOCATION) {
                requestLocation();
            }else {
                Toast.makeText(getActivity(), R.string.no_location_permission, Toast.LENGTH_SHORT).show();
                requestLocation();
            }
        }
    }

    /** hides the progress dialog (loading location)
     * */
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
     *
     *  also expands the info layout if this restaurant is the first restaurant loaded
     *  since this fragment was loaded and the layout was shown previously
     * */
    @Override
    public void injectRestaurant(final Restaurant restaurant, RestaurantDAO restaurantDAO) {
        if (getContext() == null) {
            return;
        }
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
        if (firstLoadedRestaurant && randomState != null && randomState.expanded) {
            firstLoadedRestaurant = false;
            openRestaurantBtn.callOnClick();
        }
    }

    /** #################################################################################
     *  #################################################################################
     *  #################################################################################
     *  #################################################################################
     *  OnClick-Listeners for the buttons in the Restaurant-CardView
     * */

    /** Calls the presenter to handle the edit-restaurant button clicked event
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
        deleteDialog = new AlertDialog.Builder(getContext())
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
                .create();
        deleteDialog.show();
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

    /** Calls the presenter to handle the favorite button click event
     * */
    @OnClick(R.id.favorite_restaurant_btn)
    public void favoriteThisRestaurantClicked(View view) {
        presenter.favoriteRestaurantClicked();
    }

    /** calls the presenter to handle the show restaurant location button click event
     * */
    @OnClick(R.id.show_restaurant_location_btn)
    public void showLocationClicked(View view) {
        presenter.showRestaurantLocation();
    }

    /**
     * */
    @Override
    public void setFavoriteIcon(boolean favorite) {
        if (favorite) {
            restaurantFavoriteBtn.setImageDrawable(AndroidUtils.tintDrawable(getContext(), favoriteFull, R.color.favorite));
        } else {
            restaurantFavoriteBtn.setImageDrawable(favoriteEmpty);
        }
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
    /** Calls the presenter to show a restaurant that is closer
     * */
    @OnClick(R.id.restaurant_closer)
    public void showRestaurantThatIsCloserPressed(View view) {
        presenter.showNewRestaurant(RandomChoice.CLOSER);
    }

    /** calls the presneter to show a restaurant that is cheaper
     * */
    @OnClick(R.id.restaurant_cheaper)
    public void showRestaurantThatIsCheaperPressed(View view) {
        presenter.showNewRestaurant(RandomChoice.CHEAPER);
    }

    /** calls the presenter to show a restaurant that servers different food than the current one
     * */
    @OnClick(R.id.restaurant_different_food)
    public void showRestaurantDifferentFoodType(View view) {
        presenter.showNewRestaurant(RandomChoice.DIFFERENT_FOOD);
    }

    /** Calls the presenter to show a restaurant without any new filtering settings
     * */
    @OnClick(R.id.just_a_different_restaurant)
    public void showADifferentRestaurant(View view) {
        presenter.showNewRestaurant(RandomChoice.NO_CHANGE);
    }

    /** calls the presenter to reset the settings and to start over
     *  showing an random restaurant without any filters
     * */
    @OnClick(R.id.reset_settings_button)
    public void resetSettingsPressed(View view) {
        hasLocation = false;
        presenter.resetSettings();
    }

    /** This method is called before the onResume method in which the presenter is
     *  created. This is a problem since the presenter needs to be able to retrieve
     *  the state that may be saved in the bundle
     *  In order to allow this the state is temporarily created and stored in the
     *  randomState instance variable (which may be null if state couldn't be restored)
     *  In the onResume method this variable can be used to pass the state into the presenter
     *  in order to recreate the state
     * */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        randomState = createRandomStateFromBundle(savedInstanceState);
    }

    /** This method passes the bundle to the presenter so it can save the state of the
     *  random settings and which restaurant (if any) that is currently being shown
     * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveState(outState);
        outState.putBoolean(getString(R.string.bundle_restaurant_expanded), expanded);
    }

    /** Called when the fragment is being destroyed
     *  If the presenter isn't null then the presenter will be called to
     *  preform closing functionality (such as closing the database)
     *
     *  closes any dialogs that may be showing
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onCloseFragment();
            presenter = null;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (deleteDialog != null) {
            deleteDialog.dismiss();
            deleteDialog = null;
        }
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

    /** This creates a RandomState object containing the information needed to recreate the
     *  state of the presenter after an orientation change
     *
     *  The state that is needed in order to recreate the state is the RandomiseSettings which
     *  implements parcelable, it also needs the id of the current restaurant to show
     *  If this information is stored in the bundle then this method will return an object
     *  containing this information
     *  otherwise the method will return null and the state can't be recreated
     *  (or there was no state saved)
     *
     *  also saves the expanded state so it can be restored
     * */
    @Nullable
    private RandomState createRandomStateFromBundle(Bundle bundle) {
        if (bundle != null) {
            long id = -1;
            RandomiseSettings randomiseSettings = null;
            if (bundle.containsKey(getString(R.string.bundle_random_settings))) {
                try {
                    randomiseSettings = bundle.getParcelable(getString(R.string.bundle_random_settings));
                } catch (Exception e) {
                    Log.e("randomrest", "loadState: ", e);
                }
            }
            if (bundle.containsKey(getString(R.string.bundle_restaurant_id))) {
                id = bundle.getLong(getString(R.string.bundle_restaurant_id));
            }
            if (id != -1 && randomiseSettings != null) {
                return new RandomState(randomiseSettings, id, bundle.getBoolean(getString(R.string.bundle_restaurant_expanded), false));
            }
        }
        return null;
    }


    /** This method allows the passing of state after an recreation due to an
     *  orientation change
     *  Since the saved instance bundle is available in activityCreated
     *  and the presenter is created in onResume there has to be a way to pass
     *  the state to a later part of the lifecycle
     *  The relevant state is saved in this class and used to recreate the state
     *  in the presenter in the onResume method
     * */
    private class RandomState {
        private RandomiseSettings randomiseSettings;
        private long restaurantId;
        private boolean expanded;

        public RandomState(RandomiseSettings randomiseSettings, long restaurantId, boolean expanded) {
            this.randomiseSettings = randomiseSettings;
            this.restaurantId = restaurantId;
            this.expanded = expanded;
        }
    }
}