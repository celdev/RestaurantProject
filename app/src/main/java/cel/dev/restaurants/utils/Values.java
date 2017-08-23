package cel.dev.restaurants.utils;

/** This class contains some constants used in the application
 *  These are stored here and not in xml since some parts that may need to access these
 *  doesn't have (easy) access to a Context
 * */
public class Values {

    public static final int ON_SAVE_RESTAURANT_IMAGE_COMPRESS_QUALITY = 100;
    public static final long ON_EXPAND_COLLAPSE_EXPANDABLE_VIEW_ANIMATION_TIME = 300;

    public static final float DEFAULT_ZOOM_LEVEL = 14f;
    public static final float DEFAULT_RANGE_VALUE = 0.01f;
    public static final float MINIMUM_RANGE_VALUE = 0.001f;

    public static final String SHARED_PREFERENCES = "cel.dev.restaurants";

    public static class RandomiserDefaults {

        public static final double DEFAULT_RANGE = 0.1d;
        public static final double RANGE_STEP_ON_CLOSER_DECREASE = 0.025d;


    }

    public static class SharePreferenceKeys {
        public static final String RANGE_KEY = SHARED_PREFERENCES + ".range";
    }

}
