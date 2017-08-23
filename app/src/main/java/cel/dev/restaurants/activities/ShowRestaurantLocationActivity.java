package cel.dev.restaurants.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

/** This activity is the activity which both lets the user return a
 *  location to the Create/edit restaurant activity
 *  but also lets the user show the location of a restaurant
 *
 *  This activity has two modes
 *  SHOW LOCATION
 *  or
 *  RETURN LOCATION
 *
 *  the return location will try to return a location to the activity which started this activity
 *  with startActivityForResult
 *  the Show location will just show a location on the map
 * */
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
    private AlertDialog aboutDialog;

    /** Called when the activity is being created.
     *  Checks the mode of the activity by checking if the activity was started by
     *   1. startActivityForResult or 2. startActivity.
     *  If the activity was started by 1. then the mode will be to return a location
     *  If the activity was started by 2. then the mode will be to just show a location
     *
     *  Binds Butterknife so that the @BindView above works.
     *  Initializes the MapFragment by first fetching a reference to the fragment
     *  and then loads the map asynchronous using this as a listener for the onMapReady-callback
     * */
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
        //setUpViews changes the text of the button depending on the mode of this activity
        setUpViews();
        /*  If the bundle isn't null then it means that the starter of this activity passed in a
        *   location, handleBundle handles this location
        * */
        if (getIntent().getExtras() != null) {
            handleBundle(getIntent().getExtras());
        }
    }

    /** Called when the user presses an item in the options menu
     *  if the item is context_about_app the about dialog will be shown.
     *  the dialog is saved in an instance variable so it can be dismissed in onDestroy
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.context_about_app) {
            if (aboutDialog != null) {
                aboutDialog.dismiss();
                aboutDialog = null;
            }
            aboutDialog = AndroidUtils.showAboutDialog(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /** Depending on the mode, the button will display a different text
     * */
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

    /** This is called when the button is pressed
     *  if the mode is Show Location then the activity will just be finished and
     *  the user will be returned to the previous activity
     *
     *  else if the mode is to return a location then the
     *  if a marked has been added to the map the location of this marker till be returned to
     *  the activity which started this activity
     *  if no marker has been set a toast showing a message that no location has been set will be shown
     * */
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

    /** When the map is clicked a marker will be added to that location
     *  if a marked is already present when the map is pressed that marker will be removed
     * */
    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();
        marker = map.addMarker(new MarkerOptions().position(latLng));
    }


    /** Extracts location information from the bundle if present
     *  and adds a marker on the map if the map has been initialized
     *
     *  Since the map is loaded asynchronously I believe it's not impossible
     *  for this method and the onMapReady-method to be called in different order
     *
     *  This method checks if the map has been initialized and adds the marker if
     *  the bundle contained a location and the map was initialized
     *
     *  The onMapReady checks if the location has been set (as an instance variable)
     *  if so then the onMapReady will use this location to set the marker
     *
     *  This ensures the marker is always set regardless of the order of execution of
     *  handleBundle and onMapReady
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

    /** This method inflates the options menu
     *  the menuInflated boolean checks if the menu has already been inflated and makes
     *  sure the menu isn't inflated more than once
     *  since this method is called each time the menu is used
     * */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!menuInflated) {
            getMenuInflater().inflate(R.menu.app_bar_menu_no_delete, menu);
            menuInflated = true;
        }
        return true;
    }

    /** Called when the Google Map has finished initializing
     *  if the activity should return a position a onMapClickListener is added to the map
     *  if the location isn't null (the starter activity sent a location to this activity when
     *  starting this activity) then a marker will be added at that location
     * */
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

    /** Adds a marker att the location provided as the parameter
     *  also moves the camera to this location
     * */
    private void addMarkerAtLocation(LatLng latLng) {
        this.location = latLng;
        marker = map.addMarker(new MarkerOptions().position(location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Values.DEFAULT_ZOOM_LEVEL));

    }

    /** If this activity was started with startActivityForResult then the
     *  result will be set to cancel and the activity will be finished
     * */
    @Override
    public void onBackPressed() {
        if (MapActivityMode.RETURN_LOCATION.equals(mode)) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /** Dismisses the about dialog if it isn't null
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aboutDialog != null) {
            aboutDialog.dismiss();
        }
    }

    /** Represents the two different modes this activity is used fore
     * */
    private enum MapActivityMode {

        SHOW_LOCATION,
        RETURN_LOCATION,
    }
}

