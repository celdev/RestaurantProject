package cel.dev.restaurants.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import cel.dev.restaurants.R;
import cel.dev.restaurants.model.FoodTypeAndChosenStatus;
import cel.dev.restaurants.uicontracts.dialog.ChooseKitchenDialogFragmentMVP;

/** This is the ArrayAdapter which is used for representing different kitchen types and
 *  ther chosen status in the dialog
 * */
public class FoodTypeArrayAdapter extends ArrayAdapter<FoodTypeAndChosenStatus>  {

    private List<FoodTypeAndChosenStatus> values;
    private Activity activity;
    private ChooseKitchenDialogFragmentMVP.SwitchCallback callback;

    /** When this Adapter is created, the necessary object for creating the adapter is passed in
     *  as well as a callback which allows the adapter to communicate with the creator of the adapter
     *
     *  This allows passing change in chosen status so that information can be store without
     *  having to create a direct connection between e.g. the adapter and the presenter
     * */
    public FoodTypeArrayAdapter(@NonNull Activity activity,
                                @LayoutRes int resource,
                                @NonNull List<FoodTypeAndChosenStatus> objects,
                                ChooseKitchenDialogFragmentMVP.SwitchCallback callback) {
        super(activity, resource, objects);
        this.activity = activity;
        values = objects;
        this.callback = callback;
    }

    /** Returns the item at position in the values List
     * */
    @Nullable
    @Override
    public FoodTypeAndChosenStatus getItem(int position) {
        return values.get(position);
    }

    /** The amount of different kitchens in this application will never become so large that
     *  using a ListView and inflating the layout will cause performance problems
     *
     *  Inflates the choose_kitchen_list_item_layout containing a TextView and a Switch
     *  The text of the TextView is set to the KitchenType's @StringRes id and the switch's status
     *  is set to the chosen status in the FoodTypeAndChosenStatus object
     *
     *  A SwitchChangeListener is added to the switch which will trigger when the switch state is changed
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") // Using the RecycleView is better, however this list is very small and simple so a ListView won't cause any problems
        View view = activity.getLayoutInflater().inflate(R.layout.choose_kitchen_list_item_layout, parent, false);
        Switch switchView = (Switch) view.findViewById(R.id.kitchen_chosen_switch);
        TextView kitchenName = (TextView) view.findViewById(R.id.kitchen_name);
        FoodTypeAndChosenStatus foodTypeAndChosenStatus = getItem(position);
        kitchenName.setText(foodTypeAndChosenStatus.getKitchenType().getStringResId());
        switchView.setChecked(foodTypeAndChosenStatus.isChosen());
        switchView.setOnCheckedChangeListener(new SwitchChangeListener(foodTypeAndChosenStatus));
        view.setId(View.generateViewId());
        return view;
    }


    /** Listener for the switch change event
     *  when the switch is changed the FoodTypeAndChosenStatus is passed into the
     *  callback which will then pass the status into the presenter of the activity which will save the
     *  change
     * */
    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        private FoodTypeAndChosenStatus foodTypeAndChosenStatus;

        public SwitchChangeListener(FoodTypeAndChosenStatus foodTypeAndChosenStatus) {
            this.foodTypeAndChosenStatus = foodTypeAndChosenStatus;
        }

        /** Passes the changed FoodTypeAndChosenStatus to the callback which will record this change
         * */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            foodTypeAndChosenStatus.setChosen(isChecked);
            callback.onSwitchChangeCallback(foodTypeAndChosenStatus);
        }
    }

}
