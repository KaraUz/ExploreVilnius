package lt.ex.karolis.explorevilnius.utils.UrlStringBuilder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Karolis on 2017-05-23.
 */

public abstract class HttpUrlBuilder {
    private StringBuilder urlString;
    private int countOfParameters = 0;

    public HttpUrlBuilder(String urlStringWithoutParameters) {
        this.urlString = new StringBuilder(urlStringWithoutParameters);
    }

    public String buildUrlString() {
        return urlString.toString();
    }

    private char determineParameterSeparator(){
        countOfParameters++;
        return countOfParameters < 2 ? '?' : '&';
    }

    protected void appendParameter(String label, String parameter){
        urlString.append(determineParameterSeparator());
        urlString.append(label);
        urlString.append(parameter);
    }
}
