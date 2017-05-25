package lt.ex.karolis.explorevilnius.dataobjects;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Karolis on 2017-05-22.
 */

public class Place {

    private String id;
    private Location location;
    private String icon;
    private Bitmap bitmap;
    private String name;
    private String type;
    private boolean visited = false;
    private String photoReference;


    public Place() {
    }

    public Place(String id, Location location, String icon, String name, String type, boolean visited, String photoReference) {
        this.id = id;
        this.location = location;
        this.icon = icon;
        this.name = name;
        this.type = type;
        this.visited = visited;
        this.photoReference = photoReference;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", location=" + location +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", visited=" + visited +
                '}';
    }
}
