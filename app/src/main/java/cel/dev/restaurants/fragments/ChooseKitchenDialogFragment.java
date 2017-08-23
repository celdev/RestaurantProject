package cel.dev.restaurants.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.adapters.FoodTypeArrayAdapter;
import cel.dev.restaurants.model.FoodTypeAndChosenStatus;
import cel.dev.restaurants.uicontracts.dialog.OnChooseKitchenCallback;
import cel.dev.restaurants.model.KitchenType;
import cel.dev.restaurants.R;
import cel.dev.restaurants.presenters.ChooseKitchenTypePresenterImpl;
import cel.dev.restaurants.uicontracts.dialog.ChooseKitchenDialogFragmentMVP;

/** This is the DialogFragment which will list the chosen KitchenTypes and allow
 *  the user to choose and deselect kitchen types.
 *
 *  In order to be able to callback changes of chosen status to the activity
 *  the activity overrides the onAttach-method which allows the fragment to create an connection to
 *  the containing activity. This Activity must implement OnChooseKitchenCallback which is used
 *  to communicate the chosen status changes from the listview-adapter to the activity.
 *
 *  In order to determine if this DialogFragment is showing it has a boolean called shown which is
 *  set to true when the dialog is showing and false when the dialog has been dismissed.
 *
 *  ChooseKitchenDialogFragmentMVP.View extends the interface HasShownStatus which allows the
 *  containing activity to determine if this DialogFragment is showing
 *  This can be used to determine if the Choose Kitchen Dialog should be shown when the activity is
 *  recreated after an orientation change
 * */
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

    /** Called when this dialog is being paused
     *  calls dismissAllowingStateLoss since the state of this dialog is
     *  saved by the presenter of the Activity of this dialog
     *  so this fragment doesn't need to save its own state
     * */
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
     *  Sends this change to the onChooseKitchenCallback (which is the containing activity)
     *  callback, this allows the change to be recorded by the containing activity.
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

    /** This method is used to be able to tell if the dialog was showing before
     *  the containing activity was recreated (e.g. in the case of phone orientation change)
     *
     *  allows for the activity to show the dialog on the re-creation of the activity
     * */
    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) {
            return;
        }
        super.show(manager, tag);
        shown = true;
    }

    /** Sets the shown boolean to false
     * */
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

    /** Returns true if the dialog is showing, false if it's not showing.
     * */
    @Override
    public boolean getDialogIsShowing() {
        return shown;
    }
}
