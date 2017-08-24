package cel.dev.restaurants.uicontracts;

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
 *
 *  The class provides an abstract method which child classes must implement which allows them
 *  to initialize view information
 * */
public abstract class ListRestaurantsFragment extends Fragment implements FABFragmentHandler {

    public ListRestaurantsFragment() {}

    private RecyclerView recyclerView;
    private TextView noRestaurantsText;


    /** The fragments extending this class will use the fragment_restaurant layout file to show restaurants in a RecycleView
     *  Initializes the view which will show that no restaurants was found which can be shown and hidded
     *  using the hideNoRestaurantsMessage and showNoRestaurantsMessage methods
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.restaurants_recycle_view);
        noRestaurantsText = (TextView) view.findViewById(R.id.list_fragment_info_text);
        initializeViews();
        return view;
    }

    /** This method shows the message passed as a StringRes id in the no restaurants view and
     *  sets the view to visible
     * */
    public void showNoRestaurantsMessage(@StringRes int message) {
        noRestaurantsText.setText(message);
        noRestaurantsText.setVisibility(View.VISIBLE);
    }

    /** This method hides the no restaurants found view (should be used when restaurants has been found)
     * */
    public void hideNoRestaurantsMessage() {
        noRestaurantsText.setVisibility(View.GONE);
    }

    /** Returns the recycle view
     * */
    public RecyclerView getRestaurantRecyclerView() {
        return recyclerView;
    }

    /** Allows each fragment to initialize their own view information
     * */
    public abstract void initializeViews();
}
