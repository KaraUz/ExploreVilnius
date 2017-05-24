package lt.ex.karolis.explorevilnius.utils.JsonReaders;

import android.location.Location;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lt.ex.karolis.explorevilnius.dataobjects.Place;

/**
 * Created by Karolis on 2017-05-23.
 */

public class PlaceJsonReader implements JsonConverter<Place> {
    private static final String TAG = PlaceJsonReader.class.getSimpleName();
    @Override
    public List<Place> convert(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readPlacesResults(reader);
        } finally {
            reader.close();
        }
    }

    private List<Place> readPlacesResults(JsonReader reader) throws IOException {
        List<Place> places = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("results")){
                reader.beginArray();
                while (reader.hasNext()){
                    places.add(readPlacesResult(reader));
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return places;
    }

    private Place readPlacesResult(JsonReader reader) throws IOException {
        Place place = new Place();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name){
                case "geometry": place.setLocation(readGeometry(reader));
                    Log.i(TAG, name + ": " + place.getLocation());
                    break;
                case "icon": place.setIcon(reader.nextString());
                    Log.i(TAG, name + ": " + place.getIcon());
                    break;
                case "name": place.setName(reader.nextString());
                    Log.i(TAG, name + ": " + place.getName());
                    break;
                case "place_id": place.setId(reader.nextString());
                    Log.i(TAG, name + ": " + place.getId());
                    break;
                case "types": place.setType(readTypes(reader));
                    Log.i(TAG, name + ": " + place.getType());
                    break;
                default: reader.skipValue();
            }
        }
        reader.endObject();
        return place;
    }
// edit to get more types
    private String readTypes(JsonReader reader) throws IOException {
        String type = "";
        reader.beginArray();
        if (reader.hasNext()) type = reader.nextString();
        while (reader.hasNext()) {
            reader.skipValue();
        }
        reader.endArray();
        return type;
    }

    private Location readGeometry(JsonReader reader) throws IOException {
        Location location = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name){
                case "location": location = readGeometryLocation(reader);
                    break;
                default: reader.skipValue();
            }
        }
        reader.endObject();
        return location;
    }

    private Location readGeometryLocation(JsonReader reader) throws IOException {
        Location location = new Location("Service provider");
        double lat = 0;
        double lng = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "lat":
                    lat = reader.nextDouble();
                    break;
                case "lng":
                    lng = reader.nextDouble();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }
}
