package cel.dev.restaurants.repository;

import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.BUDGET_TYPES;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.FAVORITE;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.HAS_IMAGE;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.ID;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.IMAGE;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.KITCHEN_TYPES;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.LOCATION_LATITUDE;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.LOCATION_LONGITUDE;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.NAME;
import static cel.dev.restaurants.repository.RestaurantDbSchema.Table.Cols.RATING;

public class RestaurantDbSchema {

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


    public static String generateCreateDatabase() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("create table ")
                .append(Table.NAME)
                .append(" (")
                .append(ID).append(" integer primary key autoincrement, ")
                .append(NAME).append(", ")
                .append(HAS_IMAGE).append(" integer, ")
                .append(IMAGE).append(" blob, ")
                .append(RATING).append(", ")
                .append(BUDGET_TYPES).append(", ")
                .append(KITCHEN_TYPES).append(", ")
                .append(LOCATION_LATITUDE).append(", ")
                .append(LOCATION_LONGITUDE).append(", ")
                .append(FAVORITE).append(" integer")
                .append(")");
        return builder.toString();
    }

}
