package cel.dev.restaurants.ChooseKitchenDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cel.dev.restaurants.Model.FoodType;
import cel.dev.restaurants.R;
import cel.dev.restaurants.Utils.CollectionUtils;

public class ChooseKitchenDialogFragment extends DialogFragment implements ChooseKitchenDialogFragmentMVP.View {

    private static final String ALL_FOOD_TYPE_ARG = "ALL";
    private static final String CHOSEN_FOOD_TYPE_ARG = "CHOSEN";
    private static final String TAG = "diag";

    public ChooseKitchenDialogFragment() {
    }

    private ChooseKitchenDialogFragmentMVP.Presenter presenter;
    private OnChooseKitchenCallback onChooseKitchenCallback;

    @BindView(R.id.kitchen_type_list_view)
    ListView kitchenList;

    public static ChooseKitchenDialogFragment newInstance(List<FoodType> all, List<FoodType> chosen) {
        ChooseKitchenDialogFragment f = new ChooseKitchenDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ALL_FOOD_TYPE_ARG, CollectionUtils.listToParceableArray(all));
        args.putParcelableArray(CHOSEN_FOOD_TYPE_ARG, CollectionUtils.listToParceableArray(chosen));
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_kitchen, container);
        getDialog().setTitle(R.string.choose_kitchen);
        ButterKnife.bind(this, view);
        presenter = new ChooseKitchenTypePresenterImpl(this,
                CollectionUtils.parceableToList(getArguments().getParcelableArray(ALL_FOOD_TYPE_ARG), FoodType.class),
                CollectionUtils.parceableToList(getArguments().getParcelableArray(CHOSEN_FOOD_TYPE_ARG), FoodType.class));
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
            onChooseKitchenCallback.chooseKitchen(foodTypeAndChosenStatus.getFoodType(),foodTypeAndChosenStatus.isChosen());
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
}
