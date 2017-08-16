package cel.dev.restaurants.mainactivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;
import cel.dev.restaurants.utils.AndroidUtils;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainActivityMVP.View {

    public static final String TAG = "mainactivity";

    private MainActivityMVP.Presenter presenter;
    @BindView(R.id.navigation) BottomNavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;
    private boolean menuInflated = false;

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!menuInflated) {
            getMenuInflater().inflate(R.menu.app_bar_menu, menu);
            menuInflated = true;
        }
        return true;
    }

    @Override
    public void showAboutDialog() {
        AndroidUtils.showAboutDialog(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.contextMenuItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDeleteAllRestaurantsDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.delete_all_restaurants)
                .setMessage(R.string.are_you_sure_delete_all)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteAllRestaurants();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public void handleAfterDeleteRestaurants() {
        navigationView.setSelectedItemId(R.id.nav_restaurants);
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

    @Override
    public void hideShowFAB(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
