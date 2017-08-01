package cel.dev.restaurants.showrestaurants.restaurantsrecycleview;

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cel.dev.restaurants.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.restaurant_card)
    CardView cardView;
    @BindView(R.id.restaurant_name)
    TextView restaurantName;
    @BindView(R.id.restaurant_rating_text)
    TextView ratingText;
    @BindView(R.id.restaurant_image)
    ImageView restaurantImage;
    @BindView(R.id.show_restaurant_btn)
    ImageButton openRestaurantBtn;
    @BindView(R.id.delete_restaurant_btn)
    ImageButton deleteButton;
    @BindView(R.id.edit_restaurant_btn)
    ImageButton editButton;

    public RestaurantViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, itemView);

    }


}
