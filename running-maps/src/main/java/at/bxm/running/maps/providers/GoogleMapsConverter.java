package at.bxm.running.maps.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GoogleMapsConverter {
	private static final double LATITUDE_MAX = 85.0511287798066;
	private static final Logger logger = LoggerFactory.getLogger(GoogleMapsConverter.class);

	private GoogleMapsConverter() {
		// must not be instatiated
	}

	/**
	 * Maps a GPS longitude value to a Google Maps x coordinate
	 * 
	 * Calculation: at zoom level "z", the earth is divided into 2^z tiles of equal size, whereas the
	 * first tile (x=0) starts at longitude "-180" and the last tile (x=2^z-1) ends at longitude
	 * "180".
	 */
	public static int toX(double longitude, int zoomlevel) {
		checkLongitude(longitude);
		double tileCount = Math.pow(2, zoomlevel);
		double x = (longitude + 180) // range 0..360 (west to east)
						/ 360 // range 0..1
						* tileCount;
		logger.debug("lon " + longitude + ", zoom " + zoomlevel + " -> x=" + x);
		return (int)Math.floor(x);
	}

	public static double toLongitude(int x, int zoomlevel) {
		double tileCount = Math.pow(2, zoomlevel);
		double longitude = 360d * x / tileCount - 180;
		return longitude;
	}

	/**
	 * Maps a GPS latitude value to a Google Maps y coordinate
	 * 
	 * Calculation: at zoom level "z", the earth is divided into 2^z tiles whose sizes are determined
	 * by the <a href="http://en.wikipedia.org/wiki/Mercator_projection">Mercator projection</a>. The
	 * first tile (x=0) starts at latitude "85" and the last tile (x=2^z-1) ends at latitude "-85".
	 */
	public static int toY(double latitude, int zoomlevel) {
		// TODO this surely can be optimized...
		checkLatitude(latitude);
		double tileCount = Math.pow(2, zoomlevel);
		// turn degree into radiant:
		double latitude_rad = Math.PI * latitude / 180;
		// perform mercator projection - resulting range is PI..-PI (north to south):
		double mercator = Math.log((1 + Math.sin(latitude_rad)) / (1 - Math.sin(latitude_rad))) / 2;
		double y = (Math.PI - mercator) / Math.PI / 2 // range 0..1
						* tileCount;
		logger.debug("lat " + latitude + ", zoom " + zoomlevel + " -> y=" + y);
		return (int)Math.floor(y);
	}

	public static double toLatitude(int y, int zoomlevel) {
		// TODO this surely can be optimized...
		double tileCount = Math.pow(2, zoomlevel);
		// transform back to interval PI..-PI:
		double mercator = Math.PI - Math.PI * y * 2 / tileCount;
		// the Gudermannian function inverses the mercator projection:
		double latitude_rad = Math.atan(Math.sinh(mercator));
		// now convert from radiant to degree:
		double latitude = latitude_rad * 180 / Math.PI;
		return latitude;
	}

	public static void checkLongitude(double longitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("Longitude value out of range: " + longitude);
		}
	}

	public static void checkLatitude(double latitude) {
		if (latitude < -LATITUDE_MAX || latitude > LATITUDE_MAX) {
			throw new IllegalArgumentException("Latitude value out of range: " + latitude);
		}
	}

}
