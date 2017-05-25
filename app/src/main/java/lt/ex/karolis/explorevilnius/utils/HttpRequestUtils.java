package lt.ex.karolis.explorevilnius.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lt.ex.karolis.explorevilnius.dataobjects.Place;
import lt.ex.karolis.explorevilnius.utils.JsonReaders.PlaceJsonReader;

/**
 * Created by Karolis on 2017-05-23.
 */

public class HttpRequestUtils {

    public static List<Place> requestPlaceUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return new PlaceJsonReader().convert(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Bitmap requestIconUrl(URL url) throws IOException{
        HttpURLConnection connection = connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        } finally {
            connection.disconnect();
        }
    }

    public static Bitmap requestPlaceImageUrl(URL url) throws IOException{
        HttpURLConnection connection = connection = (HttpURLConnection) url.openConnection();
        try {
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            return BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            // Log exception
            return null;
        } finally {
            connection.disconnect();
        }
    }
}
