package cel.dev.restaurants.createrestaurant;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

class RestaurantNameTextWatcher implements TextWatcher {

    private TextView placeholderWhite;
    private final CreateRestaurantMVP.UserInputInformationListener userInputInformationListener;

    RestaurantNameTextWatcher(TextView placeholderWhite, CreateRestaurantMVP.UserInputInformationListener userInputInformationListener) {
        this.placeholderWhite = placeholderWhite;
        this.userInputInformationListener = userInputInformationListener;
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
        userInputInformationListener.hasInputInformation(true);
    }
}
