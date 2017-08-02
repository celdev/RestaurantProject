package cel.dev.restaurants.choosekitchendialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import cel.dev.restaurants.utils.CollectionUtils;

public class ChooseKitchenDialogFragment extends DialogFragment implements ChooseKitchenDialogFragmentMVP.View {

    private static final String CHOSEN_FOOD_TYPE_ARG = "CHOSEN";
    private static final String TAG = "diag";

    public ChooseKitchenDialogFragment() {
    }

    private ChooseKitchenDialogFragmentMVP.Presenter presenter;
    private OnChooseKitchenCallback onChooseKitchenCallback;

    @BindView(R.id.kitchen_type_list_view)
    ListView kitchenList;

    public static ChooseKitchenDialogFragment newInstance(List<KitchenType> chosen) {
        ChooseKitchenDialogFragment f = new ChooseKitchenDialogFragment();
        Bundle args = new Bundle();
        args.putIntArray(CHOSEN_FOOD_TYPE_ARG, KitchenType.toIntegerArray(chosen));
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_kitchen, container);
        getDialog().setTitle(R.string.choose_kitchen);
        ButterKnife.bind(this, view);
        List<KitchenType> chosen = KitchenType.fromParcel(getArguments().getIntArray(CHOSEN_FOOD_TYPE_ARG));
        Log.d(TAG, "onCreateView: current chosen foodtype inside dialog = " + Arrays.toString(chosen.toArray()));
        presenter = new ChooseKitchenTypePresenterImpl(this,
                Arrays.asList(KitchenType.values()),
                chosen);
        presenter.createArrayAdapter();
        return view;
    }

    @Override
    public void injectArrayAdapter(List<FoodTypeAndChosenStatus> statusList) {
        FoodTypeArrayAdapter foodTypeArrayAdapter = new FoodTypeArrayAdapter(getActivity(), -1, statusList, presenter);
        kitchenList.setAdapter(foodTypeArrayAdapter);
    }

    @Override
    public void onFoodTypeChosenChange(FoodTypeAndChosenStatus foodTypeAndChosenStatus) {
        if (onChooseKitchenCallback != null) {
            onChooseKitchenCallback.chooseKitchen(foodTypeAndChosenStatus.getKitchenType(),foodTypeAndChosenStatus.isChosen());
        }
    }

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
    public void onDetach() {
        super.onDetach();
        onChooseKitchenCallback = null;
    }

    @OnClick(R.id.choose_kitchen_dialog_ok)
    void okDialogPressed(View view) {
        dismiss();
    }
}
