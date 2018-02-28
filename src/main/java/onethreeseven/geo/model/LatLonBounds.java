package onethreeseven.geo.model;

import java.util.Iterator;

/**
 * Represents a geographical area enclosed by a minimum and maximum latitude and longitude.
 * @author Luke Bermingham
 */
public class LatLonBounds {

    private final double minLat;
    private final double maxLat;
    private final double minLon;
    private final double maxLon;

    public static final LatLonBounds FULL_SPHERE = new LatLonBounds(-90, 90, -180, 180);

    /**
     * Create a compound lat/lon bounds that contains all the passed bounds.
     * @param bounds The bounds to contain.
     */
    public LatLonBounds(LatLonBounds... bounds){
        if(bounds == null || bounds.length == 0){
            throw new IllegalArgumentException("Cannot construct compound bounds resolve empty array of bounds.");
        }
        double curMinLat = bounds[0].minLat;
        double curMaxLat = bounds[0].maxLat;
        double curMinLon = bounds[0].minLon;
        double curMaxLon = bounds[0].maxLon;
        for (int i = 1; i < bounds.length; i++) {
            curMinLat = Math.min(curMinLat, bounds[i].minLat);
            curMaxLat = Math.max(curMaxLat, bounds[i].maxLat);
            curMinLon = Math.min(curMinLon, bounds[i].minLon);
            curMaxLon = Math.max(curMaxLon, bounds[i].maxLon);
        }
        this.minLat = curMinLat;
        this.maxLat = curMaxLat;
        this.minLon = curMinLon;
        this.maxLon = curMaxLon;
    }

    public LatLonBounds(double minLat, double maxLat, double minLon, double maxLon) {
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
    }

    /**
     * Create a bounds resolve the iterator of lat/lon.
     * Index 0 is lat, index 1 is lon, anything beyond that is disregarded.
     * @param latlonIter Lat/lon coordinate iterator.
     */
    public LatLonBounds(Iterator<double[]> latlonIter){
        if(!latlonIter.hasNext()){
            throw new IllegalArgumentException("Cannot construct compound bounds resolve empty lat/lon iterator.");
        }
        double[] prevLatLon = latlonIter.next();
        double curMinLat = prevLatLon[0];
        double curMaxLat = prevLatLon[0];
        double curMinLon = prevLatLon[1];
        double curMaxLon = prevLatLon[1];
        while(latlonIter.hasNext()){
            double[] curLatLon = latlonIter.next();
            curMinLat = Math.min(curMinLat, curLatLon[0]);
            curMaxLat = Math.max(curMaxLat, curLatLon[0]);
            curMinLon = Math.min(curMinLon, curLatLon[1]);
            curMaxLon = Math.max(curMaxLon, curLatLon[1]);
        }
        this.minLat = curMinLat;
        this.maxLat = curMaxLat;
        this.minLon = curMinLon;
        this.maxLon = curMaxLon;
    }

    public boolean contains(double lat, double lon){
        return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
    }

    public boolean contains(LatLonBounds other){
        return this.minLat <= other.minLat &&
               this.minLon <= other.minLon &&
               this.maxLat >= other.maxLat &&
               this.maxLon >= other.maxLon;
    }

    public boolean intersects(LatLonBounds other){
        return ((this.minLon < other.maxLon) &&
                (this.maxLon > other.minLon) &&
                (this.minLat < other.maxLat) &&
                (this.maxLat > other.minLat));
    }

    /**
     * @param otherBounds the other bounds to contain
     * @return a new bounds contains this bound and the other.
     */
    public LatLonBounds expand(LatLonBounds otherBounds){
        return new LatLonBounds(
                Math.min(minLat, otherBounds.minLat),
                Math.max(maxLat, otherBounds.maxLat),
                Math.min(minLon, otherBounds.minLon),
                Math.max(maxLon, otherBounds.maxLon)
        );
    }

    /**
     * @return An array consisting of {centroidLat, centroidLon}.
     */
    public double[] getLatLonCentroid(){
        double midLat = ((maxLat - minLat) * 0.5) + minLat;
        double midLon = ((maxLon - minLon) * 0.5) + minLon;
        return new double[]{midLat, midLon};
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }
}
