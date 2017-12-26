# 137-geo
A tiny geography library in Java: map projections, coordinate conversion etc. Mostly refactored from Nasa WorldWind.

Example usage for converting geographic coordinates into cartesian coordinates:

```java
ProjectionMercator p = new ProjectionMercator();
double lat = 13.7;
double lon = 137.7;
//from a mercator projection to cartesian coordinates (assuming WGS84)
double[] xy = p.geographicToCartesian(lat, lon);
double[] latlon = p.cartesianToGeographic(xy);

//from lat/lon to UTM coordinates and back
int utmZone = 1;
p = new ProjectionUTM(1);
//...and so on
```
 [ ![Download](https://api.bintray.com/packages/lukehb/137-geo/137-geo/images/download.svg) ](https://bintray.com/lukehb/137-geo/137-geo/_latestVersion)

You can use this project as a dependency like so:

```groovy
repositories {
    maven{url 'https://dl.bintray.com/lukehb/137-geo'} //hosted on bintray
}

dependencies {
    compile 'onethreeseven:geo:0.0.3'
}
```
