package lt.ex.karolis.explorevilnius.utils.JsonReaders;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import lt.ex.karolis.explorevilnius.dataobjects.Place;

/**
 * Created by Karolis on 2017-05-25.
 */

public class PlaceDetailJsonReader implements JsonConverter<String> {

    @Override
    public List<String> convert(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readPlaceDetailResults(reader);
        } finally {
            reader.close();
        }
    }

    private List<String> readPlaceDetailResults(JsonReader reader) {
        return null;
    }
}
