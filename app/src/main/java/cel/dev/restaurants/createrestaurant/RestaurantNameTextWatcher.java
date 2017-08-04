package cel.dev.restaurants.createrestaurant;

import android.text.Editable;
import android.text.TextWatcher;

import cel.dev.restaurants.createrestaurant.ImageFragment.ImageFragmentMVP;

class RestaurantNameTextWatcher implements TextWatcher {

    private ImageFragmentMVP.View hasTextView;

    RestaurantNameTextWatcher(ImageFragmentMVP.View hasTextView) {
        this.hasTextView = hasTextView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        hasTextView.setText(s.toString());
    }


}
