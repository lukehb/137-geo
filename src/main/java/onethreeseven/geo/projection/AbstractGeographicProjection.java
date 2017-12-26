package onethreeseven.geo.projection;

/**
 * <p>
 * Defines an interface to project geographic coordinates to Cartesian coordinates (and back again).
 * </p>
 * Each implementation of this interface defines its own constructors, which may accept arguments that completely define
 * the projection.
 *
 * @author tag
 * @author luke
 * @version $Id$
 */
public abstract class AbstractGeographicProjection
{
    final ProjectionLimits projectionLimits;

    public AbstractGeographicProjection(ProjectionLimits projectionLimits) {
        this.projectionLimits = projectionLimits;
    }

    /**
     * <p>
     * Converts a geographic position to meters in Cartesian coordinates.
     * </p>
     * Note: The input arguments are not checked for <code>null</code> prior to being used. The caller
     * is expected do perform that check prior to calling this method.
     *
     * @param latitude        The latitude of the position.
     * @param longitude       The longitude of the position.
     *
     * @return The Cartesian point {x,y}, in meters, corresponding to the input position.
     */
    public abstract double[] geographicToCartesian(double latitude, double longitude);

    /**
     * <p>
     * Converts a Cartesian point in meters to a geographic position.
     * </p>
     * Note: The input arguments are not checked for <code>null</code> prior to being used. The caller
     * is expected do perform that check prior to calling this method.
     *
     * @param cart   The Cartesian point, in meters, like {x,y}.
     *
     * @return The geographic position in degrees, like: {lat, lon}.
     *
     */
    public abstract double[] cartesianToGeographic(double[] cart);

    public ProjectionLimits getProjectionLimits() {
        return projectionLimits;
    }
}
