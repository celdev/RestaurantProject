package cel.dev.restaurants;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import cel.dev.restaurants.utils.AndroidUtils;
import cel.dev.restaurants.utils.Values;

public class ShowRestaurantLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    public static final String DATA_LATITUDE = "LAT";
    public static final String DATA_LONGITUDE = "LONG";
    private static final double defaultValue = -10000000.0;

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
                LatLng position = marker.getPosition();
                Bundle data = new Bundle();
                data.putDouble(DATA_LATITUDE, position.latitude);
                data.putDouble(DATA_LONGITUDE, position.longitude);
                setResult(RESULT_OK, new Intent().putExtras(data));
                finish();
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this);
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

