package onethreeseven.geo.projection;

/**
 * Provides a Mercator projection of an ellipsoidal globe.
 * @author Luke Bermingham
 */
public class ProjectionMercator extends AbstractGeographicProjection {

    private final double xOffset;
    private final double equatorialRadius;
    private final double eccentricity;
    private final double eccentricitySquared;

    public ProjectionMercator(double xOffset, double equatorialRadius, double eccentricitySquared){
        super(new ProjectionLimits(-78, 78, -180, 180));
        this.xOffset = xOffset;
        this.equatorialRadius = equatorialRadius;
        this.eccentricitySquared = eccentricitySquared;
        this.eccentricity = Math.sqrt(eccentricitySquared);
    }

    public ProjectionMercator() {
        this(0, Constants.WGS84_EQUATORIAL_RADIUS, Constants.WGS84_ES);
    }

    @Override
    public double[] geographicToCartesian(double latitude, double longitude) {
        latitude = this.projectionLimits.clampLatitude(latitude);
        longitude = this.projectionLimits.clampLongitude(longitude);

        // See "Map Projections: A Working Manual", page 44 for the source of the below formulas.

        double x = equatorialRadius * Math.toRadians(longitude) + xOffset;

        double sinPhi = Math.sin(Math.toRadians(latitude));
        double s = ((1 + sinPhi) / (1 - sinPhi)) * Math.pow((1 - eccentricity * sinPhi) / (1 + eccentricity * sinPhi), eccentricity);
        double y = 0.5 * equatorialRadius * Math.log(s);

        return new double[]{x, y};
    }

    @Override
    public double[] cartesianToGeographic(double[] cart) {
        double ecc2 = eccentricitySquared;
        double ecc4 = ecc2 * ecc2;
        double ecc6 = ecc4 * ecc2;
        double ecc8 = ecc6 * ecc2;
        double t = Math.pow(Math.E, -cart[1] / equatorialRadius);

        double A = Math.PI / 2 - 2 * Math.atan(t);
        double B = ecc2 / 2 + 5 * ecc4 / 24 + ecc6 / 12 + 13 * ecc8 / 360;
        double C = 7 * ecc4 / 48 + 29 * ecc6 / 240 + 811 * ecc8 / 11520;
        double D = 7 * ecc6 / 120 + 81 * ecc8 / 1120;
        double E = 4279 * ecc8 / 161280;

        double Ap = A - C + E;
        double Bp = B - 3 * D;
        double Cp = 2 * C - 8 * E;
        double Dp = 4 * D;
        double Ep = 8 * E;

        double s2p = Math.sin(2 * A);

        double lat = Ap + s2p * (Bp + s2p * (Cp + s2p * (Dp + Ep * s2p)));
        double lon = (cart[0] - xOffset) / equatorialRadius;
        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        return new double[]{lat, lon};
    }
}
