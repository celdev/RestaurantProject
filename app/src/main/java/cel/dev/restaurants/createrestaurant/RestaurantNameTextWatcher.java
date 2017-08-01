package cel.dev.restaurants.createrestaurant;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

class RestaurantNameTextWatcher implements TextWatcher {

    private TextView placeholderWhite;

    RestaurantNameTextWatcher(TextView placeholderWhite) {
        this.placeholderWhite = placeholderWhite;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        placeholderWhite.setText(s.toString());
    }
}
