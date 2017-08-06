package cel.dev.restaurants.mainfragments;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import cel.dev.restaurants.R;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.utils.PermissionUtils;


public class NearbyFragment extends Fragment implements FABFragmentHandler, OnSuccessListener<Location>{

    public NearbyFragment() {}
    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }


    private double range;
    private RestaurantDAO restaurantDAO;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (checkHasLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(getActivity())
                    .getLastLocation().addOnSuccessListener(getActivity(), this);
        }
        restaurantDAO = new RestaurantDAOImpl(getContext());

        return inflater.inflate(R.layout.fragment_restaurant, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    public boolean checkHasLocationPermission() {
        return PermissionUtils.hasPermissionTo(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
    }


    @Override
    public void handleFABClick() {

    }

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            restaurantDAO.getRestaurantsByLocation(location.getLatitude(), location.getLongitude(), range);
        } else {
            Toast.makeText(getActivity(), R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }
    }
}
