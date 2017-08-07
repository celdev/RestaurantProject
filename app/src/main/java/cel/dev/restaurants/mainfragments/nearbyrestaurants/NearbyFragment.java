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

    @Override
    public void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO) {
        RestaurantRecycleViewAdapter adapter = new RestaurantRecycleViewAdapter(restaurants, getContext(), restaurantDAO);
        getRestaurantRecyclerView().setAdapter(adapter);
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
