package cel.dev.restaurants.mainfragments.showrestaurants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cel.dev.restaurants.R;
import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.mainfragments.ListRestaurantsFragment;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.RestaurantRecycleViewAdapter;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.repository.RestaurantDAO;

/** This fragment shows all restaurants
 * */
public class RestaurantFragment extends ListRestaurantsFragment implements ShowRestaurantsMVP.View{

    public static final String TAG = " restfrag";

    public RestaurantFragment() {}

    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    private ShowRestaurantsMVP.Presenter presenter;
    private HashSet<Long> expandedRestaurants = new HashSet<>();
    private RestaurantRecycleViewAdapter adapter;

    @Override
    public void initializeViews() {
        getRestaurantRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: size of expanded on save = " + adapter.getExpandedRestaurants().size());
        outState.putSerializable(getString(R.string.bundle_expanded_restaurants), adapter.getExpandedRestaurants());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: called");
        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated: instancestate not null");
            if (savedInstanceState.containsKey(getString(R.string.bundle_expanded_restaurants))) {
                Log.d(TAG, "onActivityCreated: contains key");
                this.expandedRestaurants = (HashSet<Long>) savedInstanceState.getSerializable(getString(R.string.bundle_expanded_restaurants));
            }
        }
        Log.d(TAG, "onActivityCreated: expanded size = " + expandedRestaurants.size());
    }

    /** This method is called when the fragment enters the onResume lifecycle-event
     *
     *  Initialized the presenter if it isn't already initialized.
     * */
    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null) {
            presenter = new RestaurantPresenterImpl(this, getContext());
            presenter.onLoadFragment();
        }
    }

    /**
     * Uses the restaurants to create an adapter for the RecycleView
     * If no restaurants were found then a message stating what the user can do
     * in order to show restaurants will be shown
     */
    @Override
    public void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO) {
        Log.d(TAG, "injectData: called size of expanded = " + expandedRestaurants.size());
        if (restaurants.isEmpty()) {
            showNoRestaurantsMessage(R.string.no_restaurants_added);
        } else {
            hideNoRestaurantsMessage();
            adapter = new RestaurantRecycleViewAdapter(restaurants, getContext(), restaurantDAO, expandedRestaurants);
            getRestaurantRecyclerView().setAdapter(adapter);
        }
    }

    /** Called when the fragment is being destroyed
     *
     *  Calls the presenter to preform closing fragment activities if the presenter isn't null
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onCloseFragment();
        }
        presenter = null;
    }

    /** When the floating action button is pressed
     *  the create new restaurant-activity will be started which allows the user to
     *  create new restaurants
     * */
    @Override
    public void handleFABClick() {
        startActivity(new Intent(getContext(),CreateRestaurantActivity.class));
    }
}
