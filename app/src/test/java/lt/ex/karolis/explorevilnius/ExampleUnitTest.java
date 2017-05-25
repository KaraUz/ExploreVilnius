package lt.ex.karolis.explorevilnius;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import lt.ex.karolis.explorevilnius.dataobjects.Place;
import lt.ex.karolis.explorevilnius.utils.HttpRequestUtils;
import lt.ex.karolis.explorevilnius.utils.JsonReaders.PlaceJsonReader;
import lt.ex.karolis.explorevilnius.utils.UrlStringBuilder.ImageHttpUrlBuilder;
import lt.ex.karolis.explorevilnius.utils.UrlStringBuilder.PlaceHttpUrlBuilder;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void HttpUrlBuilderAppendsCorrectly(){
        String correctUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=54.704660,25.290730&radius=1000&types=point_of_interest&key=AIzaSyD0BAqS6zWQQt_M6AylhamNZ31CkbbwsTQ";
        String url = new PlaceHttpUrlBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .addLocation(new LatLng(54.704660,25.290730))
                .addRadius(1000)
                .addTypes("point_of_interest")
                .addKey("AIzaSyD0BAqS6zWQQt_M6AylhamNZ31CkbbwsTQ")
                .buildUrlString();

        assertTrue(correctUrl.equals(correctUrl));
    }

    @Test
    public void HttpImageUrlBuilderAppendsCorrectly(){
        String correctUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyD0BAqS6zWQQt_M6AylhamNZ31CkbbwsTQ";
        String url = new ImageHttpUrlBuilder("https://maps.googleapis.com/maps/api/place/photo")
                .addMaxWidth(400)
                .addPhotoReference("CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU")
                .addKey("AIzaSyD0BAqS6zWQQt_M6AylhamNZ31CkbbwsTQ")
                .buildUrlString();

        assertTrue(correctUrl.equals(correctUrl));
    }
}