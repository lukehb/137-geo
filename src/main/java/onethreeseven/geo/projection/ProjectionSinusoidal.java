package onethreeseven.geo.projection;

/**
 * Provides a Sinusoidal spherical projection.
 * @author Luke Bermingham
 */
public class ProjectionSinusoidal extends AbstractGeographicProjection {

    private final double equatorialRadius;

    public ProjectionSinusoidal() {
        this(Constants.WGS84_EQUATORIAL_RADIUS);
    }

    public ProjectionSinusoidal(double equatorialRadius){
        super(ProjectionLimits.FULL_SPHERE);
        this.equatorialRadius = equatorialRadius;
    }

    public double[] geographicToCartesian(double latitude, double longitude) {
        double latRads = Math.toRadians(latitude);
        double latCos = Math.cos(latRads);
        double x = latCos > 0 ? equatorialRadius * Math.toRadians(longitude) * latCos : 0;
        double y = equatorialRadius * latRads;
        return new double[]{x, y};
    }

    public double[] cartesianToGeographic(double[] cart) {
        double latRadians = cart[1] / equatorialRadius;
        latRadians = projectionLimits.clamp(latRadians, -Math.PI / 2, Math.PI / 2);

        double latCos = Math.cos(latRadians);
        double lonRadians = latCos > 0 ? cart[0] / (equatorialRadius * latCos) : 0;
        lonRadians = projectionLimits.clamp(lonRadians, -Math.PI, Math.PI);

        return new double[]{
                Math.toDegrees(latRadians),
                Math.toDegrees(lonRadians)
        };
    }
}
