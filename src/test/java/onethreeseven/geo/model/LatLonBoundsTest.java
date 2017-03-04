package onethreeseven.geo.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests {@link LatLonBounds}
 * @author Luke Bermingham
 */
public class LatLonBoundsTest {

    private static final LatLonBounds testBounds = new LatLonBounds(-37, 37, -137, 137);

    @Test
    public void contains() throws Exception {
        Assert.assertTrue(testBounds.contains(0,0));
    }

    @Test
    public void contains1() throws Exception {
        Assert.assertTrue(testBounds.contains(new LatLonBounds(-10, 10, 10, 10)));
    }

    @Test
    public void intersects() throws Exception {
        Assert.assertTrue(testBounds.intersects(new LatLonBounds(20, 50, 120, 150)));
    }

    @Test
    public void testMultiConstructor() throws Exception {
        LatLonBounds expanded = new LatLonBounds(testBounds, new LatLonBounds(0, 0, -175, 175));
        Assert.assertEquals(-37.0, expanded.getMinLat(), 1e-05);
        Assert.assertEquals(37.0, expanded.getMaxLat(), 1e-05);
        Assert.assertEquals(-175.0, expanded.getMinLon(), 1e-05);
        Assert.assertEquals(175.0, expanded.getMaxLon(), 1e-05);
    }

    @Test
    public void expand() throws Exception {
        LatLonBounds expanded = testBounds.expand(new LatLonBounds(0, 0, -175, 175));
        Assert.assertEquals(-37.0, expanded.getMinLat(), 1e-05);
        Assert.assertEquals(37.0, expanded.getMaxLat(), 1e-05);
        Assert.assertEquals(-175.0, expanded.getMinLon(), 1e-05);
        Assert.assertEquals(175.0, expanded.getMaxLon(), 1e-05);
    }

    @Test
    public void getMinLat() throws Exception {
        Assert.assertEquals(-37.0, testBounds.getMinLat(), 1e-05);
    }

    @Test
    public void getMaxLat() throws Exception {
        Assert.assertEquals(37.0, testBounds.getMaxLat(), 1e-05);
    }

    @Test
    public void getMinLon() throws Exception {
        Assert.assertEquals(-137.0, testBounds.getMinLon(), 1e-05);
    }

    @Test
    public void getMaxLon() throws Exception {
        Assert.assertEquals(137.0, testBounds.getMaxLon(), 1e-05);
    }

}