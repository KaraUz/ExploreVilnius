package lt.ex.karolis.explorevilnius.utils.UrlStringBuilder;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Karolis on 2017-05-23.
 */

public class PlaceHttpUrlBuilder extends HttpUrlBuilder {

    public PlaceHttpUrlBuilder(String urlStringWithoutParameters) {
        super(urlStringWithoutParameters);
    }

    public PlaceHttpUrlBuilder addLocation(LatLng latLng){
        appendParameter("location=", latLng.latitude + "," +latLng.longitude);
        return this;
    }

    public PlaceHttpUrlBuilder addRadius(int radius){
        radius = radius < 1 ? 1 : radius;
        appendParameter("radius=", String.valueOf(radius));
        return this;
    }

    public PlaceHttpUrlBuilder addTypes(String types){
        appendParameter("types=", types);
        return this;
    }

    public PlaceHttpUrlBuilder addKey(String key){
        appendParameter("key=", key);
        return this;
    }
}
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=54.704660,25.290730&radius=1000&types=hospital&key=AIzaSyD0BAqS6zWQQt_M6AylhamNZ31CkbbwsTQ");
//url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + currentLatLng +
//        "&radius=1000&types=hospital&key=" + R.string.google_maps_key);