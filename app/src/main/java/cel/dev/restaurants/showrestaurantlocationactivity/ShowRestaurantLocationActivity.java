package cel.dev.restaurants.showrestaurantlocationactivity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.Values;

public class ShowRestaurantLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static final String DATA_LATITUDE = "LAT";
    public static final String DATA_LONGITUDE = "LONG";
    private static final double defaultValue = -10000000.0;
    private boolean menuInflated = false;

    private GoogleMap map;

    private MapActivityMode mode;

    private Marker marker;

    private LatLng location;
    @BindView(R.id.map_bottom_button)
    Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurant_location);
        mode = AndroidUtils.activityStartedForResult(this)
                ? MapActivityMode.RETURN_LOCATION
                : MapActivityMode.SHOW_LOCATION;
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpViews();
        if (getIntent().getExtras() != null) {
            handleBundle(getIntent().getExtras());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.context_about_app) {
            AndroidUtils.showAboutDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpViews() {
        switch (mode) {
            case SHOW_LOCATION:
                mapButton.setText(R.string.cancel);
                break;
            case RETURN_LOCATION:
                mapButton.setText(R.string.return_location);
                break;
        }
    }

    @OnClick(R.id.map_bottom_button)
    void onMapButtonClicked(View view) {
        switch (mode) {
            case SHOW_LOCATION:
                finish();
                break;
            case RETURN_LOCATION:
                if (marker != null) {
                    LatLng position = marker.getPosition();
                    Bundle data = new Bundle();
                    data.putDouble(DATA_LATITUDE, position.latitude);
                    data.putDouble(DATA_LONGITUDE, position.longitude);
                    setResult(RESULT_OK, new Intent().putExtras(data));
                    finish();
                } else {
                    Toast.makeText(this,R.string.error_no_location,Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();
        marker = map.addMarker(new MarkerOptions().position(latLng));
    }


    /** Extracts location information from the bundle if present
     *  and adds a marker on the map if the map has been initialized
     *
     * */
    private void handleBundle(Bundle args) {
        double latitude = args.getDouble(DATA_LATITUDE, defaultValue);
        double longitude = args.getDouble(DATA_LONGITUDE, defaultValue);
        if (latitude == defaultValue || longitude == defaultValue) {
            Toast.makeText(this, R.string.no_position_found, Toast.LENGTH_SHORT).show();
        } else {
            location = new LatLng(latitude, longitude);
            if (marker == null && map != null) {
                addMarkerAtLocation(location);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!menuInflated) {
            getMenuInflater().inflate(R.menu.app_bar_menu_no_delete, menu);
            menuInflated = true;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (!MapActivityMode.SHOW_LOCATION.equals(mode)) {
            map.setOnMapClickListener(this);
        }
        if (location != null) {
            addMarkerAtLocation(location);
        }
    }

    private void addMarkerAtLocation(LatLng latLng) {
        this.location = latLng;
        marker = map.addMarker(new MarkerOptions().position(location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Values.DEFAULT_ZOOM_LEVEL));

    }

    @Override
    public void onBackPressed() {
        if (MapActivityMode.RETURN_LOCATION.equals(mode)) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private enum MapActivityMode {

        SHOW_LOCATION,
        RETURN_LOCATION,
    }
}

