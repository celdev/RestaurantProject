package cel.dev.restaurants.mainfragments.showrestaurants;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.List;

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


    @Override
    public void initializePresenter() {
        presenter = new RestaurantPresenterImpl(this, getContext());
    }


    @Override
    public void initializeViews() {
        getRestaurantRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onLoadFragment();
    }

    /** Uses the restaurants to create an adapter for the RecycleView
     *  If no restaurants were found then a message stating what the user can do
     *  in order to show restaurants will be shown
     * */
    @Override
    public void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO) {
        if (restaurants.isEmpty()) {
            showNoRestaurantsMessage(R.string.no_restaurants_added);
        } else {
            hideNoRestaurantsMessage();
            RestaurantRecycleViewAdapter adapter = new RestaurantRecycleViewAdapter(restaurants, getContext(), restaurantDAO);
            getRestaurantRecyclerView().setAdapter(adapter);
        }
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
