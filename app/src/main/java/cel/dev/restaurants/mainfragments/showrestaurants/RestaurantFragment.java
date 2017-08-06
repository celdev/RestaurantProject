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
import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.model.Restaurant;
import cel.dev.restaurants.R;
import cel.dev.restaurants.repository.RestaurantDAO;
import cel.dev.restaurants.mainfragments.showrestaurants.restaurantsrecycleview.RestaurantRecycleViewAdapter;


public class RestaurantFragment extends Fragment implements ShowRestaurantsMVP.View, FABFragmentHandler {

    public static final String TAG = " restfrag";

    public RestaurantFragment() {
        Log.d(TAG, "RestaurantFragment: creating restaurant fragment");
    }

    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    private ShowRestaurantsMVP.Presenter presenter;

    @BindView(R.id.restaurants_recycle_view)
    RecyclerView restaurantRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, view);
        presenter = new RestaurantPresenterImpl(this, getContext());
        initializeViews();
        return view;
    }

    private void initializeViews() {
        restaurantRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onLoadFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void injectData(List<Restaurant> restaurants, RestaurantDAO restaurantDAO) {
        Log.d(TAG, "injectData: injecting " + restaurants.size() + " restaurants into adapter");
        RestaurantRecycleViewAdapter adapter = new RestaurantRecycleViewAdapter(restaurants, getContext(), restaurantDAO);
        restaurantRecycleView.setAdapter(adapter);

    }


    @Override
    public void handleFABClick() {
        startActivity(new Intent(getContext(),CreateRestaurantActivity.class));
    }
}