package onethreeseven.geo.projection;

/**
 * Defines a polar equidistant projection centered on a specified pole.
 * Note: does not map continuously around the poles.
 * @author Luke Bermingham
 */
public class ProjectionPolarEquidistant extends AbstractGeographicProjection {

    private final boolean isSouthPole;
    private final double equatorialRadius;

    /**
     * Set the projection centered at the North Pole.
     */
    public ProjectionPolarEquidistant(){
        this(false, Constants.WGS84_EQUATORIAL_RADIUS);
    }

    public ProjectionPolarEquidistant(boolean isSouthPole, double equatorialRadius) {
        super(ProjectionLimits.FULL_SPHERE);
        this.isSouthPole = isSouthPole;
        this.equatorialRadius = equatorialRadius;
    }

    public double[] geographicToCartesian(double latitude, double longitude) {
        // Formulae taken from "Map Projections -- A Working Manual", Snyder, USGS paper 1395, pg. 195.
        if ((!isSouthPole && latitude == 90) || (isSouthPole && latitude == -90))
            return new double[]{0,0};

        double a = equatorialRadius * (Math.PI / 2 + Math.toRadians(latitude) * (isSouthPole ? 1 : -1));
        double lonRads = Math.toRadians(longitude);
        double x = a * Math.sin(lonRads);
        double y = a * Math.cos(lonRads) * (isSouthPole ? 1 : -1);

        return new double[]{x, y};
    }

    public double[] cartesianToGeographic(double[] cart) {
        // Formulae taken from "Map Projections -- A Working Manual", Snyder, USGS paper 1395, pg. 196.

        double rho = Math.sqrt(cart[0] * cart[0] + cart[1] * cart[1]);
        if (rho < 1.0e-4){
            return new double[]{isSouthPole ? -90 : 90, 0};
        }

        double c = rho / equatorialRadius;
        if (c > Math.PI) // map cartesian points beyond the projections radius to the edge of the projection
            c = Math.PI;

        double lat = Math.asin(Math.cos(c) * (isSouthPole ? -1 : 1));
        double lon = Math.atan2(cart[0], cart[1] * (isSouthPole ? 1 : -1)); // use atan2(x,y) instead of atan(x/y)

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
        return "PolarEquidistant";
    }


}
