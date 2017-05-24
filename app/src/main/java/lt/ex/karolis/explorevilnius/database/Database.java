package lt.ex.karolis.explorevilnius.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lt.ex.karolis.explorevilnius.dataobjects.Place;

/**
 * Created by Karolis on 2017-05-23.
 */

public class Database implements Serializable{
        private SQLiteDatabase mydatabase;

        public Database(SQLiteDatabase db){
            mydatabase = db;
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Place(" +
                    "id VARCHAR," +
                    "name VARCHAR," +
                    "type VARCHAR," +
                    "icon VARCHAR," +
                    "latitude REAL," +
                    "longitude REAL)");
        }

        public List<Place> getAllPlaces(){
            List<Place> places = new ArrayList<>();

            Cursor resultSet = mydatabase.rawQuery("Select id, latitude, longitude, icon, name, type  from Place",null);
            resultSet.moveToFirst();
            if(resultSet == null || resultSet.getCount() == 0) return places;
            for (int i = 1; i<resultSet.getCount();i++) {
                Location location = new Location("Database");
                location.setLatitude(resultSet.getDouble(1));
                location.setLongitude(resultSet.getDouble(2));
                Place place = new Place(
                        resultSet.getString(0),
                        location,
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        true
                );
                places.add(place);
                resultSet.moveToNext();
            }
            return places;
        }

        public boolean insertPlace(Place place){
            if(place == null) return false;
            Cursor resultSet = mydatabase.rawQuery("Select id from place where id = '"+place.getId()+"'",null);
            if(resultSet.getCount()>0) return false;
            mydatabase.execSQL("INSERT INTO Place VALUES('"+place.getId() +
                    "', '" + place.getName() + "','" + place.getType() +
                    "','" + place.getIcon() + "', " + place.getLocation().getLatitude() +
                    ", " + place.getLocation().getLongitude() + ");");
            return true;
        }

        public void deletePlace(Place place){
            mydatabase.delete("Place", "id='" + place.getId() + "'", null);
        }
}
