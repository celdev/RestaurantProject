package cel.dev.restaurants.CreateRestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cel.dev.restaurants.CreateRestaurant.RestaurantNameTextWatcher;
import cel.dev.restaurants.R;

public class CreateRestaurantActivity extends AppCompatActivity {


    @BindView(R.id.restaurant_name_field)
    EditText nameField;


    @BindView(R.id.restaurantNameWhitePlaceholder)
    TextView placeHolderWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        ButterKnife.bind(this);
        nameField.addTextChangedListener(new RestaurantNameTextWatcher(placeHolderWhite));
    }
}
