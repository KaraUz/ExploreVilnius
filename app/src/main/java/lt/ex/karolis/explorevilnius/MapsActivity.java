package lt.ex.karolis.explorevilnius;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import lt.ex.karolis.explorevilnius.database.Database;
import lt.ex.karolis.explorevilnius.database.PlacesReaderDbHelper;
import lt.ex.karolis.explorevilnius.dataobjects.Place;
import lt.ex.karolis.explorevilnius.utils.BitmapUtils;
import lt.ex.karolis.explorevilnius.utils.HttpRequestUtils;
import lt.ex.karolis.explorevilnius.utils.UrlStringBuilder.ImageHttpUrlBuilder;
import lt.ex.karolis.explorevilnius.utils.UrlStringBuilder.PlaceHttpUrlBuilder;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener{

    private PlacesReaderDbHelper mDbHelper;
    private Bitmap questionBitmap;
    private GoogleMap mMap;
    //private Database database;
    private WeakHashMap<Place,Marker> placeMap;
    private WeakHashMap<Marker, Place> markerMap;
    //raw map
    private static final String TAG = MapsActivity.class.getSimpleName();
    //for location
    private Location lastUpdateLocation = null;
    private final float MIN_DISTANCE_TO_VISIT = 100;
    private Location currentLocation;
    private LatLng currentLatLng;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Place services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        questionBitmap = BitmapFactory.decodeResource(getApplication().getResources(),R.mipmap.question_mark);
        questionBitmap = Bitmap.createScaledBitmap(questionBitmap,75,75,false);

        mDbHelper = new PlacesReaderDbHelper(this);

    }

    private void addVisitedPlaceMarker(Place place) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(place.getLocation().getLatitude(),place.getLocation().getLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.colorize(place.getBitmap(),63290)))
        );
        markerMap.put(marker, place);
        placeMap.put(place,marker);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
        markerMap = new WeakHashMap<>();
        placeMap = new WeakHashMap<>();
        //mMap.getUiSettings().setScrollGesturesEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        };

        //get nearby locations ass soon as current location received
        new RetrieveVisitedPlaces().execute();

        Log.i(TAG, "Location services connected.");
    }

    private void handleNewLocation(Location location) {
        currentLocation = location;
        if(lastUpdateLocation == null) lastUpdateLocation = currentLocation;
        if(lastUpdateLocation.distanceTo(currentLocation)>300){
            retrieveNearbyLocations();
        }
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        currentLatLng = new LatLng(currentLatitude, currentLongitude);

        isPlaceNearby();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

        Log.d(TAG, "New location: " + location.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void retrieveNearbyLocations(){
        String urlString =
                new PlaceHttpUrlBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                        .addLocation(currentLatLng)
                        .addRadius(1000)
                        .addTypes("point_of_interest")
                        .addKey(getResources().getString(R.string.google_maps_key))
                        .buildUrlString();
        try {
            new RetrievePlaces().execute(new URL(urlString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private Place isPlaceNearby(){
        for(Place place:markerMap.values()){
            if(currentLocation.distanceTo(place.getLocation())<MIN_DISTANCE_TO_VISIT && !place.isVisited()){
                if(VisitPlace(place))
                    return place;
            }
        }
        return null;
    }

    private boolean VisitPlace(Place place) {
        //if(database.insertPlace(place)) {
        if(mDbHelper.insertPlace(place)) {
            place.setVisited(true);
            new RetrieveIconImage().execute(place);
            Log.i(TAG, "place : "+ place.getName()+" inserted!");
            return true;
        }else{
            Log.e(TAG, "Failed to insert place!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Place place = markerMap.get(marker);
        if(place!= null && place.isVisited()){
            new DialogLoader().execute(place);
            return true;
        }
        return false;
    }

    private void setupMarkerDialog(Place place, Bitmap placeImageBitmap){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.details_dialog, null);
        Button startButt = (Button) mView.findViewById(R.id.buttStart);
        Button moreButt = (Button) mView.findViewById(R.id.buttMore);

        startButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be implemented!",Toast.LENGTH_SHORT).show();
            }
        });

        moreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"To be implemented!",Toast.LENGTH_SHORT).show();
            }
        });

        ImageView iconImage = (ImageView) mView.findViewById(R.id.imageIcon);
        iconImage.setImageBitmap(place.getBitmap());

        TextView tvName = (TextView) mView.findViewById(R.id.tvName);
        tvName.setText(place.getName());

        TextView tvType = (TextView) mView.findViewById(R.id.tvType);
        tvType.setText(place.getType().replaceAll("_"," "));

        ImageView placeImage = (ImageView) mView.findViewById(R.id.imagePlace);
        placeImage.setImageBitmap(placeImageBitmap);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    private class RetrieveVisitedPlaces  extends AsyncTask<Void, Void, List<Place>> {
        private Long startTime;
        @Override
        protected List<Place> doInBackground(Void... params) {
            startTime = System.currentTimeMillis();
            //database = new Database(getApplication().openOrCreateDatabase("place.db", Context.MODE_PRIVATE, null));
            //List<Place> places = database.getAllPlaces();
            List<Place> places = mDbHelper.readAllPlaces();

            Log.i(TAG, "places from db:");
            for(Place place:places){
                try {
                    place.setBitmap(HttpRequestUtils.requestIconUrl(new URL(place.getIcon())));
                    Log.i(TAG, place.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return places;
        }

        protected void onPostExecute(List<Place> result) {
            for (Place place : result){
                addVisitedPlaceMarker(place);
            }
            retrieveNearbyLocations();
            Log.i(TAG, "RetrieveVisitedPlaces background task finished " + (System.currentTimeMillis() - startTime) + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private class RetrievePlaces  extends AsyncTask<URL, Void, List<Place>> {
        private Long startTime;

        @Override
        protected List<Place> doInBackground(URL... params) {
            startTime = System.currentTimeMillis();
            List<Place> places = new ArrayList<>();
            for (URL param : params) {
                try {
                    places = HttpRequestUtils.requestPlaceUrl(param);
                    for (Place place : places) {
                        place.setBitmap(questionBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return places;
        }

        protected void onPostExecute(List<Place> result) {
            boolean createMarker = true;

            for (Place place : result) {
                for (Place pl : markerMap.values()) {
                    if (pl.getId().equals(place.getId())) {
                        createMarker = false;
                        break;
                    }
                }
                if (createMarker) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(questionBitmap))
                    );
                    markerMap.put(marker, place);
                    placeMap.put(place, marker);
                }
                createMarker = true;
            }
            isPlaceNearby();
            Log.i(TAG, "RetrievePlaces background task finished " + (System.currentTimeMillis() - startTime) + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private class RetrieveIconImage  extends AsyncTask<Place, Void, Place> {
        private Long startTime;

        @Override
        protected Place doInBackground(Place... params) {
            startTime = System.currentTimeMillis();
            Place place = params[0];
            try {
                place.setBitmap(HttpRequestUtils.requestIconUrl(new URL(place.getIcon())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return place;
        }

        protected void onPostExecute(Place result) {
            Marker marker = placeMap.get(result);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.colorize(result.getBitmap(), 63290)));
            Log.i(TAG, "RetrieveIconImage background task finished " + (System.currentTimeMillis() - startTime) + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private class DialogLoader extends AsyncTask<Place, Void, Bitmap>{

        private Long startTime;
        private Place place;
        @Override
        protected Bitmap doInBackground(Place... params) {
            startTime = System.currentTimeMillis();
            place = params[0];
            if(place.getPhotoReference() == null || place.getPhotoReference().isEmpty())
                return null;
            try {
                return HttpRequestUtils.requestPlaceImageUrl(new URL(
                        new ImageHttpUrlBuilder("https://maps.googleapis.com/maps/api/place/photo")
                                .addMaxWidth(540)
                                .addMaxHeight(297)
                                .addPhotoReference(place.getPhotoReference())
                                .addKey("AIzaSyD0BAqS6zWQQt_M6AylhamNZ31CkbbwsTQ")
                                .buildUrlString()
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            setupMarkerDialog(place,result);
            Log.i(TAG, "RetrieveIconImage background task finished " + (System.currentTimeMillis() - startTime) + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
}
