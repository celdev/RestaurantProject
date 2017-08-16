package cel.dev.restaurants.repository.db;

import cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols;

import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.BUDGET_TYPES;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.FAVORITE;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.HAS_IMAGE;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.ID;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.IMAGE;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.KITCHEN_TYPES;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.LOCATION_LATITUDE;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.LOCATION_LONGITUDE;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.NAME;
import static cel.dev.restaurants.repository.db.RestaurantDbSchema.Table.Cols.RATING;

public class RestaurantDbSchema {

    /** This class contains Table name and column names
     * */
    public static class Table {
        public static final String NAME = "restaurants";

        public static class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";

            public static final String HAS_IMAGE = "has_image";
            public static final String IMAGE = "image";

            public static final String RATING = "rating";

            public static final String BUDGET_TYPES = "budget_types";
            public static final String KITCHEN_TYPES = "kitchen_types";

            public static final String LOCATION_LATITUDE = "latitude";
            public static final String LOCATION_LONGITUDE = "longitude";

            public static final String FAVORITE = "favorite";

        }
    }

    /** This class contains projections used when interacting with the database
     * */
    public static class Projections {

        public static final String[] PROJECTION_ALL =
                new String[]{
                        Cols.ID,
                        Cols.NAME,
                        Cols.HAS_IMAGE,
                        Cols.IMAGE,
                        Cols.RATING,
                        Cols.BUDGET_TYPES,
                        Cols.KITCHEN_TYPES,
                        Cols.LOCATION_LATITUDE,
                        Cols.LOCATION_LONGITUDE,
                        Cols.FAVORITE
                };
        public static final String[] PROJECTION_ALL_BUT_BYTE_IMAGE =
                new String[]{
                        Cols.ID,
                        Cols.NAME,
                        Cols.HAS_IMAGE,
                        Cols.RATING,
                        Cols.BUDGET_TYPES,
                        Cols.KITCHEN_TYPES,
                        Cols.LOCATION_LATITUDE,
                        Cols.LOCATION_LONGITUDE,
                        Cols.FAVORITE
                };
        public static final String[] PROJECTION_IMAGE = new String[]{
                        Cols.IMAGE
        };
    }

    /** Contains selection stings used when interacting with the database
     * */
    public static class Selections {

        public static final String SELECTION_ID = Cols.ID + "= ?";

    }


    /** The create database SQL string
     * */
    public static String generateCreateDatabaseQuery() {
        return "create table " +
                Table.NAME +
                " (" +
                ID + " integer primary key autoincrement, " +
                NAME + ", " +
                HAS_IMAGE + " integer, " +
                IMAGE + " blob, " +
                RATING + " REAL, " +
                BUDGET_TYPES + ", " +
                KITCHEN_TYPES + ", " +
                LOCATION_LATITUDE + " REAL, " +
                LOCATION_LONGITUDE + " REAL, " +
                FAVORITE + " integer" +
                ")";
    }

    /** The drop table database SQL string
     * */
    public static String generateDropTableQuery() {
        return "DROP TABLE IF EXISTS " + Table.NAME;
    }

}
