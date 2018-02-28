package onethreeseven.geo.projection;

/**
 * Provides a Universal Polar Stereographic projection using the WGS84
 * ellipsoid and centered on a specified pole.
 * Note: does not map continuously around the poles.
 * @author Luke Bermingham
 */
public class ProjectionUPS extends AbstractGeographicProjection {

    private final boolean isSouthPole;
    private final double eccentricitySquared;
    private final double equatorialRadius;
    private final double xOffset;

    public ProjectionUPS(){
        this(false, Constants.WGS84_EQUATORIAL_RADIUS, Constants.WGS84_ES, 0);
    }

    public ProjectionUPS(boolean isSouthPole, double equatorialRadius,
                         double eccentricitySquared, double xOffset) {
        super(isSouthPole ?
            new ProjectionLimits(-90, 0, -180, 180) :
            new ProjectionLimits(0, 90, -180, 180)
        );
        this.isSouthPole = isSouthPole;
        this.eccentricitySquared = eccentricitySquared;
        this.equatorialRadius = equatorialRadius;
        this.xOffset = xOffset;
    }

    public double[] geographicToCartesian(double latitude, double longitude) {
        // Formulas taken from "Map Projections -- A Working Manual",
        // Snyder, USGS paper 1395, pg. 161.

        if ((!isSouthPole && latitude == 90) || (isSouthPole && latitude == -90)){
            return new double[]{0,0};
        }

        double latRads = Math.toRadians(latitude);
        double lonRads = Math.toRadians(longitude);

        if (!isSouthPole && latRads < 0){
            latRads = 0;
        }
        else if (isSouthPole && latRads > 0){
            latRads = 0;
        }

        // standard UPS scale factor -- see above reference pg.157, pp 2.
        double k0 = 0.994;
        double ecc = Math.sqrt(eccentricitySquared);
        double sp = Math.sin(latRads * (!isSouthPole ? 1 : -1));

        double t = Math.sqrt(((1 - sp) / (1 + sp)) * Math.pow((1 + ecc * sp) / (1 - ecc * sp), ecc));
        double s = Math.sqrt(Math.pow(1 + ecc, 1 + ecc) * Math.pow(1 - ecc, 1 - ecc));
        double r = 2 * equatorialRadius * k0 * t / s;

        double x = r * Math.sin(lonRads);
        double y = -r * Math.cos(lonRads) * (!isSouthPole ? 1 : -1);

        return new double[]{x, y};
    }

    public double[] cartesianToGeographic(double[] cart) {
        double x = (cart[0] - xOffset);
        double y = cart[1];

        double lon = Math.atan2(x, y * (!isSouthPole ? -1 : 1));

        double k0 = 0.994; // standard UPS scale factor -- see above reference pg.157, pp 2.
        double ecc = Math.sqrt(eccentricitySquared);
        double r = Math.sqrt(x * x + y * y);
        double s = Math.sqrt(Math.pow(1 + ecc, 1 + ecc) * Math.pow(1 - ecc, 1 - ecc));
        double t = r * s / (2 * equatorialRadius * k0);

        double ecc2 = eccentricitySquared;
        double ecc4 = ecc2 * ecc2;
        double ecc6 = ecc4 * ecc2;
        double ecc8 = ecc6 * ecc2;

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

        lat = lat * (!isSouthPole ? 1 : -1);

        return new double[]{
                Math.toDegrees(lat),
                Math.toDegrees(lon)
        };
    }

    public boolean isSouthPole() {
        return isSouthPole;
    }

    @Override
    public String toString(){
        return "UPS";
    }

}
