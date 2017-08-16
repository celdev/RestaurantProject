package cel.dev.restaurants.choosekitchendialog;

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

class FoodTypeArrayAdapter extends ArrayAdapter<FoodTypeAndChosenStatus>  {

    private List<FoodTypeAndChosenStatus> values;
    private Activity activity;
    private ChooseKitchenDialogFragmentMVP.SwitchCallback callback;

    FoodTypeArrayAdapter(@NonNull Activity activity,
                         @LayoutRes int resource,
                         @NonNull List<FoodTypeAndChosenStatus> objects,
                         ChooseKitchenDialogFragmentMVP.SwitchCallback callback) {
        super(activity, resource, objects);
        this.activity = activity;
        values = objects;
        this.callback = callback;
    }

    @Nullable
    @Override
    public FoodTypeAndChosenStatus getItem(int position) {
        return values.get(position);
    }

    /** The amount of different kitchens in this application will never become so large that
     *  using a ListView and inflating the layout will cause performance problems
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder")
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

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            foodTypeAndChosenStatus.setChosen(isChecked);
            callback.onSwitchChangeCallback(foodTypeAndChosenStatus);
        }
    }

}
