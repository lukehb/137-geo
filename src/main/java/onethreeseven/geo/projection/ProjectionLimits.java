package onethreeseven.geo.projection;

/**
 * The geographic limits of the projection.
 * @author Luke Bermingham
 */
public class ProjectionLimits {


    /**
     * A coordinates of a full, perfect sphere.
     */
    public static ProjectionLimits FULL_SPHERE = new ProjectionLimits(-90, 90, -180, 180);

    private final double minLatitude;
    private final double maxLatitude;
    private final double minLongitude;
    private final double maxLongitude;

    public ProjectionLimits(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        if(maxLongitude > 180 || minLongitude < -180 || maxLatitude > 90 || minLatitude < -90){
            throw new IllegalArgumentException("Coordinates must be in the interval lat[-90,90] and lon[-180,180].");
        }
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
    }

    public double clampLatitude(double latitude){
        return clamp(latitude, minLatitude, maxLatitude);
    }

    public double clampLongitude(double longitude){
        return clamp(longitude, minLongitude, maxLongitude);
    }

    public double clamp(double v, double min, double max)
    {
        return v < min ? min : v > max ? max : v;
    }


    /**
     * Make a projection limits from a given central meridian and width.
     * @param centralMeridian the central meridian in degree
     * @param width the width to project either side of the meridian in degrees
     * @return the projection limits
     */
    public static ProjectionLimits from(double centralMeridian, double width)
    {
        double minLon = centralMeridian - width;
        if (minLon < -180)
            minLon = -180;

        double maxLon = centralMeridian + width;
        if (maxLon > 180)
            maxLon = 180;

        return new ProjectionLimits(-82, 86, minLon, maxLon);
    }

    public double getDeltaLongitude(){
        return maxLongitude - minLongitude;
    }

    public double getDeltaLatitude(){
        return maxLatitude - minLatitude;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }
}
