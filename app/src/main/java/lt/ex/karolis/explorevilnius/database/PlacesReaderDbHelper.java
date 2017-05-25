package lt.ex.karolis.explorevilnius.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import lt.ex.karolis.explorevilnius.dataobjects.Place;

import static lt.ex.karolis.explorevilnius.database.PlacesReaderContract.PlacesEntry.*;

/**
 * Created by Karolis on 2017-05-25.
 */

public class PlacesReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    COLUMN_NAME_ID + " VARCHAR PRIMARY KEY," +
                    COLUMN_NAME_NAME + " VARCHAR," +
                    COLUMN_NAME_TYPE + " VARCHAR," +
                    COLUMN_NAME_ICON + " VARCHAR," +
                    COLUMN_NAME_PHOTO_REFERENCE + " VARCHAR," +
                    COLUMN_NAME_LATITUDE + " REAL," +
                    COLUMN_NAME_LONGITUDE + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public PlacesReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PlacesReaderDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertPlace(Place place){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_ID, place.getId());
        values.put(COLUMN_NAME_NAME, place.getName());
        values.put(COLUMN_NAME_ICON, place.getIcon());
        values.put(COLUMN_NAME_TYPE, place.getType());
        values.put(COLUMN_NAME_PHOTO_REFERENCE, place.getPhotoReference());
        values.put(COLUMN_NAME_LATITUDE, place.getLocation().getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, place.getLocation().getLongitude());

        long newId = db.insert(TABLE_NAME, null, values);
        if(newId>0) return true;
        else return false;
    }

    public List<Place> readAllPlaces() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_NAME,
                COLUMN_NAME_TYPE,
                COLUMN_NAME_ICON,
                COLUMN_NAME_PHOTO_REFERENCE,
                COLUMN_NAME_LATITUDE,
                COLUMN_NAME_LONGITUDE,
        };

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<Place> places = new ArrayList<>();
        while (cursor.moveToNext()) {
            Location location = new Location("Database");
            location.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NAME_LATITUDE)));
            location.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_NAME_LONGITUDE)));
            Place place = new Place(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ID)),
                    location,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ICON)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TYPE)),
                    true,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_PHOTO_REFERENCE))
            );
            places.add(place);
        }
        cursor.close();
        return places;
    }
}