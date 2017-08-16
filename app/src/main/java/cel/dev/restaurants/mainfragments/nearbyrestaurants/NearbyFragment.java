package cel.dev.restaurants.mainfragments.nearbyrestaurants;

import android.Manifest;
import android.content.DialogInterface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.mainfragments.ListRestaurantsFragment;
import cel.dev.restaurants.mainfragments.showrestaurants.ShowRestaurantsMVP;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.RestaurantRecycleViewAdapter;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.utils.PermissionUtils;

/** This is the fragment which allows the user to explore restaurants which are nearby
 *
 *  This fragment extends the ListRestaurantsFragment which provides the basic functionality
 *  to display a set of restaurants in a RecycleView
 *
 *  The handler of the Floating Action Button will open the Set Range Dialog
 * */
public class NearbyFragment extends ListRestaurantsFragment implements OnSuccessListener<Location>, ShowRestaurantsMVP.View, NearbyMVP.View {

    private static final int REQUEST_LOCATION = 1;

    public NearbyFragment() {}
    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    private NearbyMVP.Presenter presenter;

    @Override
    public void initializePresenter() {
        presenter = new NearbyPresenterImpl(this, getContext());
        presenter.refreshList();
    }

    @Override
    public void initializeViews() {
        getRestaurantRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void handleFABClick() {
        presenter.onFABPressed();
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(getActivity(), PermissionUtils.LOCATION_PERMISSIONS, REQUEST_LOCATION);
    }


    /** This is called when the location service returns a location
     *  Uses this location to get restaurants which is close
     * */
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            presenter.getRestaurantsAndInject(location.getLatitude(), location.getLongitude());
        } else {
            Toast.makeText(getActivity(), R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean checkHasLocationPermission() {
        return PermissionUtils.hasPermissionTo(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
    }

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

    @Override
    public void requestLocation() {
        if (checkHasLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(getActivity())
                    .getLastLocation().addOnSuccessListener(getActivity(), this);
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
            RestaurantRecycleViewAdapter adapter = new RestaurantRecycleViewAdapter(restaurants, getContext(), restaurantDAO);
            getRestaurantRecyclerView().setAdapter(adapter);
        }
    }

    @Override
    public void refreshNearbyList() {
        presenter.refreshList();
    }

    @Override
    public void showGetLocationPermissionDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.need_location_permission)
                .setMessage(R.string.needs_location_permission)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestLocationPermission();
                        dialog.dismiss();
                    }
                }).create().show();
    }


    /** Shows a set range dialog
     *  which allows the user to set the range (close <--> not very close)
     *  of restaurants to consider nearby
     *  When the range (using a SeekBar) is changed the nearby restaurants are updated directly
     *  Which provides good feedback for the user in determining what is "close" and "not very close"
     * */
    @Override
    public void showRangeDialog(double range) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.range_dialog_layout, null);
        builder.setView(view);
        builder.setTitle(R.string.choose_range)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.refreshList();
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        SeekBar rangeBar = (SeekBar) view.findViewById(R.id.range_bar);
        if (rangeBar != null) {
            rangeBar.setProgress(presenter.getRangeForSeekBar());
            rangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    presenter.saveRange(progress);
                    refreshNearbyList();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        dialog.show();
    }
}
