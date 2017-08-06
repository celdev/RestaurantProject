package cel.dev.restaurants.mainfragments.nearbyrestaurants;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.mainfragments.ListRestaurantsFragment;
import cel.dev.restaurants.mainfragments.showrestaurants.ShowRestaurantsMVP;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.RestaurantRecycleViewAdapter;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.repository.RestaurantDAOImpl;
import cel.dev.restaurants.utils.PermissionUtils;


public class NearbyFragment extends ListRestaurantsFragment implements OnSuccessListener<Location>, ShowRestaurantsMVP.View{

    public NearbyFragment() {}
    public static NearbyFragment newInstance() {
        return new NearbyFragment();
    }

    private double range = 0.1;
    private RestaurantDAO restaurantDAO;

    public boolean checkHasLocationPermission() {
        return PermissionUtils.hasPermissionTo(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void initializePresenter() {
        restaurantDAO = new RestaurantDAOImpl(getContext());
        if (checkHasLocationPermission()) {
            LocationServices.getFusedLocationProviderClient(getActivity())
                    .getLastLocation().addOnSuccessListener(getActivity(), this);
        }
    }

    @Override
    public void initializeViews() {
        getRestaurantRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void handleFABClick() {
        initializePresenter();
    }

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            List<Restaurant> restaurantsByLocation = restaurantDAO.getRestaurantsByLocation(location.getLatitude(), location.getLongitude(), range);
            if (!restaurantsByLocation.isEmpty()) {
                injectData(restaurantsByLocation, restaurantDAO);
            } else {
                Toast.makeText(getActivity(), R.string.error_getting_location, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.error_getting_location, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO) {
        RestaurantRecycleViewAdapter adapter = new RestaurantRecycleViewAdapter(restaurants, getContext(), restaurantDAO);
        getRestaurantRecyclerView().setAdapter(adapter);
    }

}
