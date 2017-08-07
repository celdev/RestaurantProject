package cel.dev.restaurants.utils;

public class Values {

    public static final int ON_SAVE_RESTAURANT_IMAGE_COMPRESS_QUALITY = 100;
    public static final long ON_EXPAND_COLLAPSE_EXPANDABLE_VIEW_ANIMATION_TIME = 300;

    public static final float DEFAULT_ZOOM_LEVEL = 14f;
    public static final float DEFAULT_RANGE_VALUE = 0.01f;
    public static final float MINIMUM_RANGE_VALUE = 0.001f;

    public static final String SHARED_PREFERENCES = "cel.dev.restaurants";



    public static class SharePreferenceKeys {
        public static final String RANGE_KEY = SHARED_PREFERENCES + ".range";
    }

}
