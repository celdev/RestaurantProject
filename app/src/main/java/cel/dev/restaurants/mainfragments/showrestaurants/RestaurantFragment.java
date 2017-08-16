package cel.dev.restaurants.mainfragments.showrestaurants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.mainfragments.ListRestaurantsFragment;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.R;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.RestaurantRecycleViewAdapter;


public class RestaurantFragment extends ListRestaurantsFragment implements ShowRestaurantsMVP.View{

    public static final String TAG = " restfrag";

    public RestaurantFragment() {
        Log.d(TAG, "RestaurantFragment: creating restaurant fragment");
    }

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


    @Override
    public void handleFABClick() {
        startActivity(new Intent(getContext(),CreateRestaurantActivity.class));
    }
}
