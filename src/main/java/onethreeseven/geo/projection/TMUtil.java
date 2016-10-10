package onethreeseven.geo.projection;

/**
 * Wrapper util around {@link TMCoordConverter}
 * @author Luke Bermingham
 */
public final class TMUtil {


    /**
     * Convert lat/long (degrees) into Transverse Mercator coordinates.
     *
     * @param latitude the latitude.
     * @param longitude the longitude.
     * @param equatorialRadius the equatorial radius of the globe.
     * @param polarRadius the polar radius of the globe.
     * @param originLatitude the origin latitude.
     * @param centralMeridian the central meridian longitude.
     * @param falseEasting easting value at the center of the projection in meters.
     * @param falseNorthing northing value at the center of the projection in meters.
     * @param scale scaling factor.
     * @return A double[]{easting, northing}.
     * @throws IllegalArgumentException if the conversion to TM coordinates fails.
     */
    public static double[] toEastingNorthing(double latitude, double longitude, double equatorialRadius, double polarRadius,
                                             double originLatitude, double centralMeridian,
                                             double falseEasting, double falseNorthing,
                                             double scale)
    {
        final TMCoordConverter converter = new TMCoordConverter();
        double flattening = (equatorialRadius - polarRadius) / equatorialRadius;

        long err = converter.setTransverseMercatorParameters(
                equatorialRadius,
                flattening,
                Math.toRadians(originLatitude),
                Math.toRadians(centralMeridian),
                falseEasting,
                falseNorthing,
                scale);

        if (err == TMCoordConverter.TRANMERC_NO_ERROR)
            err = converter.convertGeodeticToTransverseMercator(Math.toRadians(latitude), Math.toRadians(longitude));

        if (err != TMCoordConverter.TRANMERC_NO_ERROR && err != TMCoordConverter.TRANMERC_LON_WARNING)
        {
            throw new IllegalArgumentException("Bad parameters for utm conversion.");
        }
        return new double[]{converter.getEasting(), converter.getNorthing()};
    }

    /**
     * Create a lat/long pair given easting, northing and projection parameters.
     *
     * @param easting the easting distance value in meters.
     * @param northing the northing distance value in meters.
     * @param equatorialRadius the equatorial radius of the globe.
     * @param polarRadius the polar radius of the globe.
     * @param originLatitude the origin latitude.
     * @param centralMeridian the central meridian longitude.
     * @param falseEasting easting value at the center of the projection in meters.
     * @param falseNorthing northing value at the center of the projection in meters.
     * @param scale scaling factor.
     * @return a double[]{lat, long}.
     * @throws IllegalArgumentException if the conversion to geodetic coordinates fails.
     */
    public static double[] fromEastingNorthing(double easting, double northing, double equatorialRadius,
                                               double polarRadius, double originLatitude, double centralMeridian,
                                               double falseEasting, double falseNorthing,
                                               double scale)
    {
        final TMCoordConverter converter = new TMCoordConverter();
        double flattening = (equatorialRadius - polarRadius) / equatorialRadius;
        long err = converter.setTransverseMercatorParameters(equatorialRadius,
                flattening, Math.toRadians(originLatitude), Math.toRadians(centralMeridian),
                falseEasting, falseNorthing, scale);

        if (err == TMCoordConverter.TRANMERC_NO_ERROR){
            err = converter.convertTransverseMercatorToGeodetic(easting, northing);
        }

        if (err != TMCoordConverter.TRANMERC_NO_ERROR && err != TMCoordConverter.TRANMERC_LON_WARNING)
        {
            throw new IllegalArgumentException("bad parameters for utm conversion");
        }
        return new double[]{Math.toDegrees(converter.getLatitude()), Math.toDegrees(converter.getLongitude())};
    }


}
