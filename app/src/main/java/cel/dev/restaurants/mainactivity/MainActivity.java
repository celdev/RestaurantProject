package cel.dev.restaurants.mainactivity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainActivityMVP.View {

    private MainActivityMVP.Presenter presenter;
    @BindView(R.id.navigation) BottomNavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this, this);
        navigationView.setOnNavigationItemSelectedListener(this);
        presenter.handleSavedInstanceState(savedInstanceState);
        presenter.loadFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return presenter.tabPressed(item.getItemId());
    }

    @OnClick(R.id.fab)
    public void fabClick(View view){
        presenter.fabPressed();
    }

    @Override
    public void setFragment(Fragment fragment, @DrawableRes int fabIconDrawableId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
        fab.setImageDrawable(getDrawable(fabIconDrawableId));
    }

}
