package cel.dev.restaurants.persistanceimpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Database creator helper
 *  uses the strings defined in RestaurantDbSchema to create the database
 * */
public class RestaurantDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "restaurants.db";


    public RestaurantDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /** Creates the database using the generateCreateDatabaseQuery in the RestaurantDbSchema class
     *  which generates the SQL needed to create the database
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RestaurantDbSchema.generateCreateDatabaseQuery());
    }

    /** Drops the table if it exists and creates the table again
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(RestaurantDbSchema.generateDropTableQuery());
        onCreate(db);
    }

    /** Drops the table if it exists and creates the table again
     * */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
