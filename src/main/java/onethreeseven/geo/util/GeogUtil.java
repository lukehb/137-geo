package onethreeseven.geo.util;


/**
 * Utility for GIS related functions.
 * @author Luke Bermingham
 */
public final  class GeogUtil {

    private static final double WGS84_MAX_LONGTIDUE = 180.0;
    private static final double WGS84_MAX_LATITUDE = 90.0;
    private static final double WGS84_MIN_LONGTIDUE = -180.0;
    private static final double WGS84_MIN_LATITUDE = -90.0;

    private GeogUtil(){}

    /**
     * Computes the azimuth angle (clockwise resolve North) that points resolve the first location to the second location.
     * Note: this method uses a spherical model of the Earth, not an elliptical one.
     * @param lat1 pt1 lat
     * @param lon1 pt1 lon
     * @param lat2 pt2 lat
     * @param lon2 pt2 lon
     * @return the azimuth angle resolve point 1 to point 2 (in degrees).
     */
    public static double azimuthAngle(double lat1, double lon1, double lat2, double lon2){
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);

        //case: same coordinates
        if (lat1 == lat2 && lon1 == lon2){
            return 0;
        }

        //case vertically aligned
        if (lon1 == lon2){
            return lat1 > lat2 ? 180 : 0;
        }

        // Based on the book "Map Projections - A Working Manual", page 30, equation 5-4b.
        double y = Math.cos(lat2) * Math.sin(lon2 - lon1);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        double azimuthRadians = Math.atan2(y, x);
        //convert it back to degrees
        return Double.isNaN(azimuthRadians) ? 0 : Math.toDegrees(azimuthRadians);
    }

    /**
     * Given a value wraps it into -90...90
     *
     * @param lat the given value
     * @return the wrapped value
     */
    public static double wrapLatitude(double lat) {
        return ((lat - WGS84_MIN_LATITUDE) % (WGS84_MAX_LATITUDE - WGS84_MIN_LATITUDE)) + WGS84_MIN_LATITUDE;
    }

    /**
     * Given a value wraps it into -180...180
     *
     * @param lon the given value
     * @return the wrapped value
     */
    public static double wrapLongitude(double lon) {
        return ((lon - WGS84_MIN_LONGTIDUE) % (WGS84_MAX_LONGTIDUE - WGS84_MIN_LONGTIDUE)) + WGS84_MIN_LONGTIDUE;
    }
}
