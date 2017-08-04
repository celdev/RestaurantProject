package cel.dev.restaurants.choosekitchendialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.R;

public class ChooseKitchenDialogFragment extends DialogFragment implements ChooseKitchenDialogFragmentMVP.View {

    private static final String CHOSEN_FOOD_TYPE_ARG = "CHOSEN";

    private boolean shown;

    public ChooseKitchenDialogFragment() {
    }

    private ChooseKitchenDialogFragmentMVP.Presenter presenter;
    private OnChooseKitchenCallback onChooseKitchenCallback;

    @BindView(R.id.kitchen_type_list_view)
    ListView kitchenList;

    /** Creates a new instance of the DialogFragment
     *  adds the chosen KitchenTypes as an argument which can then be used
     *  in onCreateView
     * */
    public static ChooseKitchenDialogFragment newInstance(List<KitchenType> chosen) {
        ChooseKitchenDialogFragment f = new ChooseKitchenDialogFragment();
        Bundle args = new Bundle();
        args.putIntArray(CHOSEN_FOOD_TYPE_ARG, KitchenType.toIntegerArray(chosen));
        f.setArguments(args);
        return f;
    }

    /** Inflates the view using the fragment_choose_kitchen xml file
     *  Creates the presenter and calls to the presenter so that the
     *  contents for the adapter is created which is then used for
     *  creating the adapter which is then attached to the list view
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_kitchen, container);
        getDialog().setTitle(R.string.choose_kitchen);
        ButterKnife.bind(this, view);
        List<KitchenType> chosen = KitchenType.fromParcel(getArguments().getIntArray(CHOSEN_FOOD_TYPE_ARG));
        presenter = new ChooseKitchenTypePresenterImpl(this,
                Arrays.asList(KitchenType.values()),
                chosen);
        presenter.createArrayAdapter();
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        dismissAllowingStateLoss();
    }

    /** Creates the ArrayAdapter for the ListView using the
     *  @param statusList as content
     * */
    @Override
    public void injectArrayAdapter(List<FoodTypeAndChosenStatus> statusList) {
        FoodTypeArrayAdapter foodTypeArrayAdapter = new FoodTypeArrayAdapter(getActivity(), -1, statusList, presenter);
        kitchenList.setAdapter(foodTypeArrayAdapter);
    }

    /** Callback for when a Kitchen type has been selected or deselected
     * */
    @Override
    public void onFoodTypeChosenChange(FoodTypeAndChosenStatus foodTypeAndChosenStatus) {
        if (onChooseKitchenCallback != null) {
            onChooseKitchenCallback.chooseKitchen(foodTypeAndChosenStatus.getKitchenType(),foodTypeAndChosenStatus.isChosen());
        }
    }

    /** The activity that will contain this DialogFragment must implement OnChooseKitchenCallback
     *  This is so that the presenter of the activity can handle change in chosen kitchen types
     * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChooseKitchenCallback) {
            onChooseKitchenCallback = (OnChooseKitchenCallback) context;
        } else {
            throw new RuntimeException("Activity must implement onChooseKitchenCallback");
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) {
            return;
        }
        super.show(manager, tag);
        shown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        shown = false;
        super.onDismiss(dialog);
    }

    /** Releases the choose kitchen type callback
     * */
    @Override
    public void onDetach() {
        super.onDetach();
        onChooseKitchenCallback = null;
    }

    /** Closes the dialog when the ok button is pressed
     * */
    @OnClick(R.id.choose_kitchen_dialog_ok)
    void okDialogPressed(View view) {
        dismiss();
    }

    @Override
    public boolean getDialogIsShowing() {
        return shown;
    }
}
