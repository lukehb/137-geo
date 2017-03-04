package onethreeseven.geo.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing our geographic helper methods.
 * @author Luke Bermingham
 */
public class GeogUtilTest {

    @Test
    public void testAngleVerticallyAligned() throws Exception {
        double angle = GeogUtil.azimuthAngle(30, 15, 30.137, 15);
        Assert.assertTrue(angle == 0);

        //flip latitudes
        angle = GeogUtil.azimuthAngle(30.137, 15, 30, 15);
        Assert.assertTrue(angle == 180);
    }

    @Test
    public void testAngleHorizontallyAligned() throws Exception{
        double angle = GeogUtil.azimuthAngle(10, 37, 10, 37.731);
        angle = Math.round(angle);
        Assert.assertTrue(angle == 90);
    }

    @Test
    public void testDownwardAngle() throws Exception{
        double angle = GeogUtil.azimuthAngle(10, 37, 9.98, 37.01);
        Assert.assertTrue(angle > 90);
    }

    @Test
    public void testLatWrapping() throws Exception {
        double wrappedLat = GeogUtil.wrapLatitude(100);
        Assert.assertEquals(-80, wrappedLat, 1e-06);
    }

    @Test
    public void testLonWrapping() throws Exception {
        double wrappedLat = GeogUtil.wrapLongitude(200);
        Assert.assertEquals(-160, wrappedLat, 1e-06);
    }
}