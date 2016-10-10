package onethreeseven.geo.projection;

/**
 * Implements a TransverseMercator projection for a specified UTM zone.
 */
public class ProjectionUTM extends ProjectionTransverseMercator {

    private static final double projectionScale = 0.9996;

    private final int zone;

    public ProjectionUTM(int zone) {
        super(30, centralMeridianForZone(zone), 0,
                projectionScale, Constants.WGS84_EQUATORIAL_RADIUS,
                Constants.WGS84_POLAR_RADIUS, 0, 0);
        this.zone = zone;
    }

    public static double centralMeridianForZone(int zone)
    {
        if (zone < 1 || zone > 60)
        {
            throw new IllegalArgumentException("Utm zones must be [1,60].");
        }
        return (3 + (zone - 1) * 6) - (zone > 30 ? 360 : 0);
    }
}
