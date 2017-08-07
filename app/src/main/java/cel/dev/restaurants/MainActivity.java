package cel.dev.restaurants;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import cel.dev.restaurants.mainfragments.FABFragmentHandler;
import cel.dev.restaurants.mainfragments.FindFragment;
import cel.dev.restaurants.mainfragments.nearbyrestaurants.NearbyFragment;
import cel.dev.restaurants.mainfragments.showrestaurants.RestaurantFragment;
import cel.dev.restaurants.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private int activeFragment;
    public static final String ACTIVE_FRAGMENT_KEY = "active_fragment";

    private FABFragmentHandler fabFragmentHandler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_restaurants:
                    activeFragment = R.id.nav_restaurants;
                    fragmentTransaction.replace(R.id.content, createFragment(id));
                    fragmentTransaction.commit();
                    setFabIcon(R.drawable.ic_add_black_24dp);
                    return true;
                case R.id.nav_nearby:
                    activeFragment = R.id.nav_nearby;
                    fragmentTransaction.replace(R.id.content, createFragment(id));
                    fragmentTransaction.commit();
                    setFabIcon(R.drawable.ic_settings_black_24dp);
                    return true;
                case R.id.nav_find:
                    activeFragment = R.id.nav_find;
                    fragmentTransaction.replace(R.id.content, createFragment(id));
                    fragmentTransaction.commit();
                    setFabIcon(R.drawable.ic_filter_list_black_24dp);
                    return true;
            }
            return false;
        }

    };
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment fragment;
        if (savedInstanceState != null) {
            activeFragment = savedInstanceState.getInt(ACTIVE_FRAGMENT_KEY);
            switch (activeFragment) {
                case R.id.nav_nearby:
                    fragment = NearbyFragment.newInstance();
                    break;
                case R.id.nav_find:
                    fragment = FindFragment.newInstance();
                    break;
                default:
                case R.id.nav_restaurants:
                    fragment = RestaurantFragment.newInstance();
                    break;
            }
        } else {
            fragment = RestaurantFragment.newInstance();
            activeFragment = R.id.nav_restaurants;
        }
        fabFragmentHandler = (FABFragmentHandler) fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commit();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabFragmentHandler.handleFABClick();
            }
        });
    }

    private void setFabIcon(int drawableId) {
        fab.setImageDrawable(getDrawable(drawableId));
    }

    private Fragment createFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_restaurants:
                fragment = RestaurantFragment.newInstance();
                break;
            case R.id.nav_nearby:
                fragment = NearbyFragment.newInstance();
                break;
            case R.id.nav_find:
                fragment = FindFragment.newInstance();
                break;
        }
        if (fragment != null && fragment instanceof FABFragmentHandler) {
            fabFragmentHandler = (FABFragmentHandler) fragment;
        } else {
            throw new RuntimeException("Fragment must implement fab handler");
        }
        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ACTIVE_FRAGMENT_KEY, activeFragment);
        super.onSaveInstanceState(outState);
    }
}
