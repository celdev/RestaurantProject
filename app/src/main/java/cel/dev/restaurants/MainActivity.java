package cel.dev.restaurants;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import cel.dev.restaurants.createrestaurant.CreateRestaurantActivity;
import cel.dev.restaurants.showrestaurants.RestaurantFragment;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_restaurants:
                    fragmentTransaction.replace(R.id.content, RestaurantFragment.newInstance());
                    fragmentTransaction.commit();
                    setFabIcon(R.drawable.ic_add_black_24dp);
                    return true;
                case R.id.nav_nearby:
                    fragmentTransaction.replace(R.id.content, NearbyFragment.newInstance());
                    fragmentTransaction.commit();
                    setFabIcon(R.drawable.ic_settings_black_24dp);
                    return true;
                case R.id.nav_find:
                    fragmentTransaction.replace(R.id.content, FindFragment.newInstance());
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,RestaurantFragment.newInstance()).commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateRestaurantActivity.class));
            }
        });
    }

    private void setFabIcon(int drawableId) {
        fab.setImageDrawable(getDrawable(drawableId));
    }

}
