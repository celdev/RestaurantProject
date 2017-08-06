package cel.dev.restaurants.mainfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cel.dev.restaurants.R;


public class FindFragment extends Fragment implements FABFragmentHandler {



    public FindFragment() {
    }


    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false);
    }



    @Override
    public void onDetach() {
        super.onDetach();
        
    }


    @Override
    public void handleFABClick() {

    }
}
