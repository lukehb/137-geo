package onethreeseven.geo.projection;

/**
 * <p>
 * Provides a Transverse Mercator ellipsoidal projection using the WGS84 ellipsoid.
 * The projection's central meridian may be specified and defaults to the Prime Meridian
 * (0 longitude). By default, the projection computes values for 30 degrees either side
 * of the central meridian. Large widths may fail.
 * </p>
 * The projection limits are modified to reflect the central meridian and the width,
 * however the projection limits are clamped to a minimum of -180 degrees and a
 * maximum of +180 degrees. It's therefore not possible to display a band whose central
 * meridian is plus or minus 180.
 * @author Luke Bermingham
 */
public class ProjectionTransverseMercator extends AbstractGeographicProjection {

    private static final double DEFAULT_WIDTH = 30;
    private static final double DEFAULT_CENTRAL_MERIDIAN = 0;
    private static final double DEFAULT_CENTRAL_LATITUDE = 0;

    protected final double width;
    protected final double centralMeridian;
    protected final double centralLatitude;
    protected final double scale;
    protected final double equatorialRadius;
    protected final double polarRadius;
    protected final double falseNorthing;
    protected final double falseEasting;

    public ProjectionTransverseMercator() {
        this(DEFAULT_WIDTH,
            DEFAULT_CENTRAL_MERIDIAN,
            DEFAULT_CENTRAL_LATITUDE, 1,
            Constants.WGS84_EQUATORIAL_RADIUS,
            Constants.WGS84_POLAR_RADIUS, 0, 0);
    }

    /**
     * Create a projection using these TM parameters.
     * @param width the width of the projection in degrees.
     * @param centralMeridian the central meridian in degrees.
     * @param centralLatitude the central latitude in degrees.
     * @param scale the scale of the projection (1 is default).
     * @param equatorialRadius the equatorial radius of the globe.
     * @param polarRadius the polar radius of the globe.
     * @param falseNorthing the false northing origin in meters (default 0).
     * @param falseEasting the false easting origin in meters (default 0).
     */
    public ProjectionTransverseMercator(double width, double centralMeridian,
                                        double centralLatitude, double scale,
                                        double equatorialRadius, double polarRadius,
                                        double falseNorthing, double falseEasting) {
        super(ProjectionLimits.from(centralMeridian, width));
        this.width = width;
        this.centralMeridian = centralMeridian;
        this.centralLatitude = centralLatitude;
        this.scale = scale;
        this.equatorialRadius = equatorialRadius;
        this.polarRadius = polarRadius;
        this.falseNorthing = falseNorthing;
        this.falseEasting = falseEasting;
    }

    public double[] geographicToCartesian(double latitude, double longitude) {
        //clamp lat and lon
        latitude = projectionLimits.clampLatitude(latitude);
        longitude = projectionLimits.clampLongitude(longitude);

        return TMUtil.toEastingNorthing(latitude,
                longitude,
                equatorialRadius,
                polarRadius,
                centralLatitude,
                centralMeridian,
                falseEasting, falseNorthing, scale);
    }

    public double[] cartesianToGeographic(double[] cart) {
        return TMUtil.fromEastingNorthing(cart[0], cart[1],
                equatorialRadius, polarRadius, centralLatitude,
                centralMeridian, falseEasting, falseNorthing, scale);
    }

}
