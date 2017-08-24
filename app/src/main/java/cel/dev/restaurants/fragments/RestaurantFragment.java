package cel.dev.restaurants.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.HashSet;
import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.activities.CreateRestaurantActivity;
import cel.dev.restaurants.uicontracts.ListRestaurantsFragment;
import cel.dev.restaurants.adapters.RestaurantRecycleViewCardViewAdapter;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.persistance.RestaurantDAO;
import cel.dev.restaurants.presenterimpl.RestaurantPresenterImpl;
import cel.dev.restaurants.uicontracts.ShowRestaurantsMVP;

/** This fragment shows all restaurants using a RecycleView-list
 * */
public class RestaurantFragment extends ListRestaurantsFragment implements ShowRestaurantsMVP.View{

    public static final String TAG = " restfrag";

    public RestaurantFragment() {}

    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    private ShowRestaurantsMVP.Presenter presenter;
    private HashSet<Long> expandedRestaurants = new HashSet<>();
    private RestaurantRecycleViewCardViewAdapter adapter;

    /** Initializes the LayoutManager for the RecycleView
     * */
    @Override
    public void initializeViews() {
        getRestaurantRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    /** Stores which restaurants (id) which information is expanded so that their information
     *  can be expanded after e.g. an orientation change
     * */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.bundle_expanded_restaurants), adapter.getExpandedRestaurants());
    }

    /** This method is similar to onRestoreInstanceState
     *  If any restaurant info was expanded when the onSaveInstanceState was called then the ids of
     *  these restaurants was stored and can be used to expand those restaurants information in the
     *  adapters
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.bundle_expanded_restaurants))) {
                this.expandedRestaurants = (HashSet<Long>) savedInstanceState.getSerializable(getString(R.string.bundle_expanded_restaurants));
            }
        }
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
        }
        presenter.onLoadFragment();
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
            adapter = new RestaurantRecycleViewCardViewAdapter(restaurants, getContext(), restaurantDAO, expandedRestaurants);
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
