package lt.ex.karolis.explorevilnius.utils.JsonReaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Karolis on 2017-05-23.
 */

public interface JsonConverter <T>{
    List<T> convert(InputStream in) throws IOException;
}
