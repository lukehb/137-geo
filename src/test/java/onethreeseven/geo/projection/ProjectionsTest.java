package onethreeseven.geo.projection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Test all projection in this package.
 * @author Luke Bermingham
 */
public class ProjectionsTest {

    private static final int nCoordsToGenerate = 10;
    private static final double delta = 0.5;
    private static final Random r = new Random();

    public static double[][] generateTestCoordinates(AbstractGeographicProjection projection){
        ProjectionLimits limits = projection.projectionLimits;
        double lonStep = limits.getDeltaLongitude()/(nCoordsToGenerate+1);
        double latStep = limits.getDeltaLatitude()/(nCoordsToGenerate+1);
        double[][] coords = new double[nCoordsToGenerate][];
        for (int i = 1; i < nCoordsToGenerate+1; i++) {
            double lon = limits.getMinLongitude() + (i * lonStep);
            double lat = limits.getMinLatitude() + (i * latStep);
            coords[i-1] = new double[]{lat, lon};
        }
        return coords;
    }

    @Test
    public void testAllProjections() throws Exception {

        AbstractGeographicProjection[] projections = new AbstractGeographicProjection[]{
                new ProjectionPolarEquidistant(true, Constants.WGS84_EQUATORIAL_RADIUS),
                new ProjectionTransverseMercator(),
                new ProjectionEquirectangular(),
                new ProjectionMercator(),
                new ProjectionModifiedSinusoidal(),
                new ProjectionSinusoidal(),
                new ProjectionUPS(true, Constants.WGS84_EQUATORIAL_RADIUS, Constants.WGS84_ES, 0),
                new ProjectionUTM(r.nextInt(60)+1)
        };

        for (AbstractGeographicProjection p : projections) {
            System.out.println("Testing: " + p.getClass().getSimpleName());

            if(p instanceof ProjectionPolarEquidistant){
                System.out.println("Is south pole: " + ((ProjectionPolarEquidistant) p).isSouthPole());
            }
            if(p instanceof ProjectionUPS){
                System.out.println("Is south pole: " + ((ProjectionUPS) p).isSouthPole());
            }

            double[][] latlons = generateTestCoordinates(p);
            for (double[] latlon : latlons) {
                double[] xy = p.geographicToCartesian(latlon[0], latlon[1]);
                double[] latlonOut = p.cartesianToGeographic(xy);
                Assert.assertEquals(latlon[0], latlonOut[0], delta);
                Assert.assertEquals(latlon[1], latlonOut[1], delta);
            }
        }
    }





}