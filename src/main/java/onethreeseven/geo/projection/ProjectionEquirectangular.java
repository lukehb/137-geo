package onethreeseven.geo.projection;

/**
 * Implements an Equirectangular projection, also known as Equidistant Cylindrical, Plate Carree and Rectangular. The
 * projected globe is spherical, not ellipsoidal.
 * @author Luke Bermingham
 */
public class ProjectionEquirectangular extends AbstractGeographicProjection {

    private final double equatorialRadius;
    private final double xOffset;

    public ProjectionEquirectangular() {
        this(Constants.WGS84_EQUATORIAL_RADIUS, 0);
    }

    public ProjectionEquirectangular(double equatorialRadius, double xOffset){
        super(ProjectionLimits.FULL_SPHERE);
        this.equatorialRadius = equatorialRadius;
        this.xOffset = xOffset;
    }

    public double[] geographicToCartesian(double latitude, double longitude) {
        latitude = this.projectionLimits.clampLatitude(latitude);
        longitude = this.projectionLimits.clampLongitude(longitude);
        return new double[]{
                equatorialRadius * Math.toRadians(longitude) + xOffset,
                equatorialRadius * Math.toRadians(latitude)
        };
    }

    public double[] cartesianToGeographic(double[] cart) {
        return new double[]{
                Math.toDegrees(cart[1] / equatorialRadius),
                Math.toDegrees((cart[0] - xOffset) / equatorialRadius)
        };
    }
}
