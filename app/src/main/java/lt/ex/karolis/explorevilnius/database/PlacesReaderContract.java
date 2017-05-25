package lt.ex.karolis.explorevilnius.database;

import android.provider.BaseColumns;

/**
 * Created by Karolis on 2017-05-25.
 */

public class PlacesReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PlacesReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class PlacesEntry implements BaseColumns {
        public static final String TABLE_NAME = "Places";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_PHOTO_REFERENCE = "photoReference";
        public static final String COLUMN_NAME_PLACE_DETAIL = "placeDetail";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }
}