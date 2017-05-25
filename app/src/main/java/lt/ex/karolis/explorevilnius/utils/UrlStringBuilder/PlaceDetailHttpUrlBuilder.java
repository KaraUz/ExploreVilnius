package lt.ex.karolis.explorevilnius.utils.UrlStringBuilder;

/**
 * Created by Karolis on 2017-05-25.
 */

public class PlaceDetailHttpUrlBuilder extends HttpUrlBuilder {

    public PlaceDetailHttpUrlBuilder(String urlStringWithoutParameters) {
        super(urlStringWithoutParameters);
    }

    public PlaceDetailHttpUrlBuilder addPlaceId(String placeId){
        appendParameter("placeid=", placeId);
        return this;
    }

    public PlaceDetailHttpUrlBuilder addKey(String key){
        appendParameter("key=", key);
        return this;
    }

}
