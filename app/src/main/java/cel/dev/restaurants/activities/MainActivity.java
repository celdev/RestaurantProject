package cel.dev.restaurants.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cel.dev.restaurants.R;
import cel.dev.restaurants.presenters.MainActivityPresenterImpl;
import cel.dev.restaurants.uicontracts.MainActivityMVP;
import cel.dev.restaurants.utils.AndroidUtils;

/** This activity is the MainActivity of this application
 *  The activity contains
 *      an appbar with an options-menu
 *      a BottomNavigationView with three options
 *      a Floating Action Button
 *      a Fragment
 *
 *  The BottomNavigationView allows the user to set which fragment to show in the activity
 *  There are three fragments
 *      Show all restaurants
 *      Show nearby restaurants
 *      Find a random restaurant
 *  The fragment is responsible for handling the Floating action button click, The
 *      Find a random restaurant-fragment doesn't need a floating action button so the button is hidden
 *      in this fragment
 *
 * */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainActivityMVP.View {

    public static final String TAG = "mainactivity";

    private MainActivityMVP.Presenter presenter;
    @BindView(R.id.navigation) BottomNavigationView navigationView;
    @BindView(R.id.fab) FloatingActionButton fab;
    private boolean menuInflated = false;
    private AlertDialog deleteAllDialog;
    private AlertDialog aboutDialog;

    /** Sets the layout to the layout defined in activity_main
     *  Binds Butterknife to this so that the @BindView above works
     *  Initializes the presenter for this activity and adds this object as a
     *  listener for the onNavigationItemSelected-event for the bottom navigation bar
     *
     *  Passes the savedInstanceState to the presenter so that the presenter can
     *  determine which fragment should be shown in the view.
     *
     *  If the saved instance state is null then then call the presenter to load the default fragment
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainActivityPresenterImpl(this, this);
        navigationView.setOnNavigationItemSelectedListener(this);
        presenter.handleSavedInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            presenter.loadFragment();
        }
    }

    /** Returns the current fragment
     * */
    @Override
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    /** Creates the options menu by inflating the app_bar_menu file
     *  Only creates the menu if it hasn't already been created
     *  To check if it's already has been created the menuInflated is set to true
     *  when the menu has been inflated.
     *  This is done since this method is called each time the menu is opened
     * */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!menuInflated) {
            getMenuInflater().inflate(R.menu.app_bar_menu, menu);
            menuInflated = true;
        }
        return true;
    }

    /** Shows the about dialog
     * if the dialog isn't null then dismiss it.
     * the dialog is saved in an instance variable so it can be dismissed in onDestroy
     * */
    @Override
    public void showAboutDialog() {
        if (aboutDialog != null) {
            aboutDialog.dismiss();
            aboutDialog = null;
        }
        aboutDialog = AndroidUtils.showAboutDialog(this);
    }

    /** This method is called when the user clicks an item in the options menu.
     *  Passes the id of the chosen item to the presenter.
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.menuItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows a dialog asking if the user is sure to delete all restaurants
     * If the user presses delete then all restaurants are deleted
     * If the user presses cancel then the dialog is dismissed
     * The dialog is stored in a instance variable so that the state of
     * the dialog (showing or not) can be recreated after an orientation change
     *
     * If the dialog isn't null then the dialog will be dismissed and set to null before
     * creating a new dialog.
     */
    @Override
    public void showDeleteAllRestaurantsDialog() {
        if (deleteAllDialog != null) {
            deleteAllDialog.dismiss();
            deleteAllDialog = null;
        }
        deleteAllDialog = new AlertDialog.Builder(this).setTitle(R.string.delete_all_restaurants)
                .setMessage(R.string.are_you_sure_delete_all)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteAllRestaurants();
                        deleteAllDialog = null;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllDialog = null;
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        deleteAllDialog = null;
                    }
                })
                .create();
        deleteAllDialog.show();
    }


    /** Hides and sets the Delete all restaurants dialog to null if it isn't already null
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deleteAllDialog != null) {
            deleteAllDialog.dismiss();
            deleteAllDialog = null;
        }
        if (aboutDialog != null) {
            aboutDialog.dismiss();
            aboutDialog = null;
        }
    }

    /** This method is called after all restaurants have been deleted
     *  and cause the show restaurants fragment to reload by pressing the restaurants tab
     *  in the bottom navigation bar
     * */
    @Override
    public void handleAfterDeleteRestaurants() {
        navigationView.setSelectedItemId(R.id.nav_restaurants);
    }

    /** Passes the bundle into the presenter so that the presenter can save it's state
     *  Also stores the state of the delete all restaurants dialog
     * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.bundle_delete_all_restaurants_dialog), deleteAllDialog != null);
        super.onSaveInstanceState(outState);
    }

    /** Checks if the delete all restaurants dialog was showing when the onSaveInstanceState was called
     *  and recreates the dialog if it was showing.
     * */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(getString(R.string.bundle_delete_all_restaurants_dialog), false)) {
                showDeleteAllRestaurantsDialog();
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /** This method is called when the user presses an item in the bottom navigation bar
     *  passes the id of the item to the presenter
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return presenter.tabPressed(item.getItemId(), null);
    }

    /** When the floating action button is pressed call the presenter to handle this press
     * */
    @OnClick(R.id.fab)
    public void fabClick(View view){
        presenter.fabPressed();
    }

    /** Sets the fragment and the icon of the floating action button to the
     *  fragment and drawable res id passed as parameters
     * */
    @Override
    public void setFragment(Fragment fragment, @DrawableRes int fabIconDrawableId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
        fab.setImageDrawable(getDrawable(fabIconDrawableId));
    }

    /** Hides or shows the floating action button depending on the parameter show
     * */
    @Override
    public void hideShowFAB(boolean show) {
        fab.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
