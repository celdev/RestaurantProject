package cel.dev.restaurants.mainfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cel.dev.restaurants.R;

public abstract class ListRestaurantsFragment extends Fragment implements FABFragmentHandler {

    public ListRestaurantsFragment() {}

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurants_recycle_view);
        initializeViews();
        initializePresenter();
        return view;
    }

    public RecyclerView getRestaurantRecyclerView() {
        return recyclerView;
    }

    public abstract void initializePresenter();

    public abstract void initializeViews();
}
