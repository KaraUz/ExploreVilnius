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
import lt.ex.karolis.explorevilnius.utils.JsonReaders.PlaceDetailJsonReader;
import lt.ex.karolis.explorevilnius.utils.JsonReaders.PlaceJsonReader;

/**
 * Created by Karolis on 2017-05-23.
 */

public class HttpRequestUtils {

    public static List<Place> requestPlaceUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        try {
            return new PlaceJsonReader().convert(in);
        } finally {
            in.close();
            urlConnection.disconnect();
        }
    }

    public static List<String> requestPlaceDetailsUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        try {
            return new PlaceDetailJsonReader().convert(in);
        } finally {
            in.close();
            urlConnection.disconnect();
        }
    }

    public static Bitmap requestIconUrl(URL url) throws IOException{
        HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream in = connection.getInputStream();
        try {
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        }finally {
            connection.disconnect();
            in.close();
        }
    }

    public static Bitmap requestPlaceImageUrl(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream in = connection.getInputStream();
        BufferedInputStream bin = new BufferedInputStream(in);
        try {
            return BitmapFactory.decodeStream(bin);
        } finally {
            connection.disconnect();
            in.close();
            bin.close();
        }
    }
}
