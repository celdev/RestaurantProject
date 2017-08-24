package cel.dev.restaurants.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.adapters.RestaurantRecycleViewCardViewAdapter;
import cel.dev.restaurants.uicontracts.ListRestaurantsFragment;
import cel.dev.restaurants.presenterimpl.NearbyPresenterImpl;
import cel.dev.restaurants.uicontracts.NearbyMVP;
import cel.dev.restaurants.uicontracts.ShowRestaurantsMVP;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.utils.PermissionUtils;

/** This is the fragment which allows the user to explore restaurants which are nearby
 *
 *  This fragment extends the ListRestaurantsFragment which provides the basic functionality
 *  to display a set of restaurants in a RecycleView
 *
 *  The handler of the Floating Action Button will open the Set Range Dialog
 * */
public class NearbyFragment extends ListRestaurantsFragment implements OnSuccessListener<Location>, ShowRestaurantsMVP.View, NearbyMVP.View, OnCompleteListener<Location> {

    private static final int REQUEST_LOCATION = 1;
    private static final String TAG = "nearbyfrag";
    private AlertDialog permissionDialog;


    public NearbyFragment() {}
    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    private NearbyMVP.Presenter presenter;

    private HashSet<Long> expandedRestaurants = new HashSet<>();
    private RestaurantRecycleViewCardViewAdapter adapter;

    private AlertDialog rangeDialog;
    private boolean shouldShowRangeDialog;
    private Location location;

    /** Creates the presenter if it isn't null and calls to refresh the list
     *  Also checks if the range dialog should be shown and calls the presenter to show it if so.
     * */
    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null) {
            presenter = new NearbyPresenterImpl(this, getContext());
            if (shouldShowRangeDialog) {
                presenter.onFABPressed(); //will call the presenter to show the range dialog
            }
        }
        presenter.refreshList();
    }

    /** Saves the ids of the restaurants which information was expanded
     *  and the state of the range dialog
     * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.bundle_expanded_restaurants), adapter.getExpandedRestaurants());
        outState.putBoolean(getString(R.string.bundle_range_dialog_showing), rangeDialog != null);
    }

    /** This method is similar to onRestoreInstanceState
     *  If any restaurant info was expanded when the onSaveInstanceState was called then the ids of
     *  these restaurants was stored and can be used to expand those restaurants information in the
     *  adapters
     *
     *  If the range dialog was showing when the onSaveInstanceState was called it should be
     *  recreated and shown
     *  Since this method is called before the presenter is created this fragment
     *  can't use the presenter to show the range dialog, which needs the presenter for it's callback
     *  Instead this method sets a boolean to true which the onResume can then use in order
     *  to determine if the range dialog should be shown
     * */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.bundle_expanded_restaurants))) {
                this.expandedRestaurants = (HashSet<Long>) savedInstanceState.getSerializable(getString(R.string.bundle_expanded_restaurants));
                Log.d(TAG, "onActivityCreated: retrieved expanded restaurants");
            }
            if (savedInstanceState.getBoolean(getString(R.string.bundle_range_dialog_showing), false)) {
                shouldShowRangeDialog = true;
            }
        }
    }



    /** Initializes the layoutmanager of the recycle view*/
    @Override
    public void initializeViews() {
        getRestaurantRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    /** Passes the handling of the floating action button click to the presenter
     * */
    @Override
    public void handleFABClick() {
        presenter.onFABPressed();
    }

    /** Request location permissions
     * */
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), PermissionUtils.LOCATION_PERMISSIONS, REQUEST_LOCATION);
    }


    /** This is called when the location service returns a location
     *  Uses this location to get restaurants which is close
     *
     *  For some reason there seems to be a bug in which the location is null
     *  However, the onComplete-method below gets the location just a few milliseconds later
     *  without a problem.
     *
     *  Using the onError-listener for the location service doesn't show anything when this bug(?)
     *  happens
     *
     *  If the location isn't null then the instance variable location will be set
     *  which the onComplete-method can use for checking if the onSuccess was in fact successful
     *
     * */
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            this.location = location;
            presenter.getRestaurantsAndInject(location.getLatitude(), location.getLongitude());
        }
    }

    /** This method is called after the onSuccess method above
     *  There seems to be a bug in the FusedLocationService which causes the onSuccess
     *  to fail (location = null) for no apparent reason.
     *
     *  This method instead gets the result from the Task<Location> which, during my testing
     *  hasn't failed like onSuccess.
     *  This method passes the location into the onSuccess-method above if
     *      * the location isn't null
     *      * the location wasn't set by the onSuccess-method (it didn't fail)
     *  If the location is null then an error has occurred and a Toast will be shown.
     * */
    @Override
    public void onComplete(@NonNull Task<Location> task) {
        Location location = task.getResult();
        if (location != null) {
            if (this.location == null) {
                onSuccess(location);
            }
        } else {
            Toast.makeText(getActivity(), R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }
    }

    /** Returns true if the application has location permission
     * */
    @Override
    public boolean checkHasLocationPermission() {
        return PermissionUtils.hasPermissionTo(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /** Handles the request location permission results
     *  if the result is ok and the requestcode is the one used when requesting the permission then
     *  the application will attempt to retrieve the users location and use this to refresh the list
     *  of nearby restaurants
     *
     *  ifthe result ins't ok then a dialog stating that the location permission is needed for this
     *  use case of the application
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(grantResults[0])) {
            if (requestCode == REQUEST_LOCATION) {
                presenter.refreshList();
            }
        } else {
            showGetLocationPermissionDialog();
        }
    }

    /** Request the location of the user using FusedLocationService
     *  adds this object as an listener for the onSuccess-event and the onComplete-event
     *  since onSuccess sometimes fail for no apparent reason
     * */
    @Override
    public void requestLocation() {
        if (checkHasLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(getActivity())
                    .getLastLocation()
                    .addOnSuccessListener(getActivity(), this)
                    .addOnCompleteListener(this);
        } else {
            showGetLocationPermissionDialog();
        }
    }

    /** Uses the List of restaurants and the restaurantDAO (needed in order to get the image and set changes in the favorite status)
     *  to create the adapter for the RecycleView
     *
     *  If this list of restaurants is empty an error message will be shown instead
     *  with information about what the user can do in order to see more restaurants
     * */
    @Override
    public void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO) {
        if (restaurants.isEmpty()) {
            showNoRestaurantsMessage(R.string.no_restaurant_found_try_changing_area_size);
        } else {
            hideNoRestaurantsMessage();
            adapter = new RestaurantRecycleViewCardViewAdapter(restaurants, getContext(), restaurantDAO, expandedRestaurants);
            getRestaurantRecyclerView().setAdapter(adapter);
        }
    }

    /** If the presenter isn't null, this will call the presenter to preform closing
     *  functionality
     *  hides any dialogs that may be showing
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onCloseFragment();
            presenter = null;
        }
        if (rangeDialog != null) {
            rangeDialog.dismiss();
            rangeDialog = null;
        }
        if (permissionDialog != null) {
            permissionDialog.dismiss();
            permissionDialog = null;
        }
    }

    /** Calls the presenter to refresh which restaurants is considered close
     * */
    @Override
    public void refreshNearbyList() {
        presenter.refreshList();
    }

    /** Shows a dialog that staes that the application needs location permission in
     *  order for this functionality of the application to work.
     *
     *  stores the dialog in a variable so it can be dismissed in onDestroy
     * */
    @Override
    public void showGetLocationPermissionDialog() {
        if (permissionDialog != null) {
            permissionDialog.dismiss();
            permissionDialog = null;
        }
        permissionDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.need_location_permission)
                .setMessage(R.string.needs_location_permission)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestLocationPermission();
                        dialog.dismiss();
                    }
                }).create();
        permissionDialog.show();
    }




    /** Shows a set range dialog
     *  which allows the user to set the range (close <--> not very close)
     *  of restaurants to consider nearby
     *  When the range (using a SeekBar) is changed the nearby restaurants are updated directly
     *  Which provides good feedback for the user in determining what is "close" and "not very close"
     * */
    @Override
    public void showRangeDialog(double range) {
        if (rangeDialog != null) {
            rangeDialog.dismiss();
            rangeDialog = null;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.range_dialog_layout, null);
        builder.setView(view);
        builder.setTitle(R.string.choose_range)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.refreshList();
                        rangeDialog = null;
                        dialog.dismiss();
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                rangeDialog = null;
            }
        });
        rangeDialog = builder.create();
        SeekBar rangeBar = (SeekBar) view.findViewById(R.id.range_bar);
        if (rangeBar != null) {
            rangeBar.setProgress(presenter.getRangeForSeekBar());
            rangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //changes in the seekbar will immediately be used to refresh the range of which
                    //restaurants is to be considered close
                    //this range is also saved directly
                    //this allows the user to see how which restaurants is nearby changes when changing the progress of the seekbar
                    presenter.saveRange(progress);
                    refreshNearbyList();
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
        rangeDialog.show();
    }
}
