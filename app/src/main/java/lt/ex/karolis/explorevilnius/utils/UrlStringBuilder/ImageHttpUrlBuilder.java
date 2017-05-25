package lt.ex.karolis.explorevilnius.utils.UrlStringBuilder;

/**
 * Created by Karolis on 2017-05-25.
 */

public class ImageHttpUrlBuilder extends HttpUrlBuilder {
    public ImageHttpUrlBuilder(String urlStringWithoutParameters) {
        super(urlStringWithoutParameters);
    }

    public ImageHttpUrlBuilder addMaxHeight (int maxheight){
        maxheight = maxheight < 1 ? 1 : maxheight;
        appendParameter("maxheight=", String.valueOf(maxheight));
        return this;
    }

    public ImageHttpUrlBuilder addMaxWidth (int maxwidth){
        maxwidth = maxwidth < 1 ? 1 : maxwidth;
        appendParameter("maxwidth=", String.valueOf(maxwidth));
        return this;
    }

    public ImageHttpUrlBuilder addPhotoReference(String photoreference){
        appendParameter("photoreference=", photoreference);
        return this;
    }

    public ImageHttpUrlBuilder addKey(String key){
        appendParameter("key=", key);
        return this;
    }
}
