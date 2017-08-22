package cel.dev.restaurants.mainfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cel.dev.restaurants.R;


/** This class contains the base functionality for fragments which will show a set of restaurants
 *  in a RecycleView
 *
 *  Contains functionality for showing an error message if no restaurants were found to show
 *  in the RecycleView
 * */
public abstract class ListRestaurantsFragment extends Fragment implements FABFragmentHandler {

    public ListRestaurantsFragment() {}

    private RecyclerView recyclerView;
    private TextView noRestaurantsText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurants_recycle_view);
        noRestaurantsText = (TextView) view.findViewById(R.id.list_fragment_info_text);
        initializeViews();
        return view;
    }

    public void showNoRestaurantsMessage(@StringRes int message) {
        noRestaurantsText.setText(message);
        noRestaurantsText.setVisibility(View.VISIBLE);
    }

    public void hideNoRestaurantsMessage() {
        noRestaurantsText.setVisibility(View.GONE);
    }

    public RecyclerView getRestaurantRecyclerView() {
        return recyclerView;
    }

    /** Allows each fragment to initialize their own view information
     * */
    public abstract void initializeViews();
}
