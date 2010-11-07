package at.bxm.running.graph.map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GoogleMapProvider implements MapProvider {
	private static final double LATITUDE_MAX = 85.0511287798066;
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * Maps a GPS longitude value to a Google Maps x coordinate
	 * 
	 * Calculation: at zoom level "z", the earth is divided into 2^z tiles of equal size, whereas the
	 * first tile (x=0) starts at longitude "-180" and the last tile (x=2^z-1) ends at longitude
	 * "180".
	 */
	protected int calculateX(double longitude, int zoomlevel) {
		double tileCount = Math.pow(2, zoomlevel);
		double x = (longitude + 180) // range 0..360 (west to east)
						/ 360 // range 0..1
						* tileCount;
		logger.debug("lon " + longitude + ", zoom " + zoomlevel + " -> x=" + x);
		return (int)Math.floor(x);
	}

	/**
	 * Maps a GPS latitude value to a Google Maps y coordinate
	 * 
	 * Calculation: at zoom level "z", the earth is divided into 2^z tiles whose sizes are determined
	 * by the <a href="http://en.wikipedia.org/wiki/Mercator_projection">Mercator projection</a>. The
	 * first tile (x=0) starts at latitude "85" and the last tile (x=2^z-1) ends at latitude "-85".
	 */
	protected int calculateY(double latitude, int zoomlevel) {
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

	private void assertLongitude(double longitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("Longitude value out of range: " + longitude);
		}
	}

	private void assertLatitude(double latitude) {
		if (latitude < -LATITUDE_MAX || latitude > LATITUDE_MAX) {
			throw new IllegalArgumentException("Latitude value out of range: " + latitude);
		}
	}

	@Override
	public MapLayout<GoogleMapTile> getLayout(double longitudeMin, double latitudeMin,
					double longitudeMax, double latitudeMax, double resolution) {
		assertLongitude(longitudeMin);
		assertLongitude(longitudeMax);
		assertLatitude(latitudeMin);
		assertLatitude(latitudeMax);
		// TODO implement zoomlevel/resolution mapping
		int zoomlevel = (int)resolution;
		int xMin = calculateX(longitudeMin, zoomlevel);
		int yMin = calculateY(latitudeMin, zoomlevel);
		int xMax = calculateX(longitudeMax, zoomlevel);
		int yMax = calculateY(latitudeMax, zoomlevel);
		DefaultMapLayout<GoogleMapTile> layout = new DefaultMapLayout<GoogleMapTile>(256, 256);
		for (int x = xMin, i = 0; x <= xMax; x++, i++) {
			for (int y = yMin, j = 0; y <= yMax; y++, j++) {
				layout.addTile(i, j, new GoogleMapTile(zoomlevel, x, y));
			}
		}
		return layout;
	}

	private static class GoogleMapTile extends CachedMapTile {
		private final Log logger = LogFactory.getLog(getClass());
		/** range 6-18 */
		private final int zoom;
		private final int x;
		private final int y;

		public GoogleMapTile(int zoom, int x, int y) {
			this.zoom = Math.min(Math.max(zoom, 6), 18);
			this.x = x;
			this.y = y;
		}

		@Override
		public byte[] loadImage() throws IOException {
			// FIXME do this right
			HttpClient httpclient = new DefaultHttpClient();
			int serverNo = (int)Math.floor(Math.random() * 4);
			HttpGet req = new HttpGet("http://khm" + serverNo + ".google.com/kh/v=45&hl=en&x=" + x
							+ "&y=" + y + "&z=" + zoom + "&s=Galileo");
			logger.debug("Downloading: " + req.getURI());
			HttpResponse response = httpclient.execute(req);
			InputStream in = response.getEntity().getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1000];
			int read;
			while ((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			response.getEntity().consumeContent();
			return out.toByteArray();
		}

		@Override
		protected String getCacheFile() {
			return "z" + zoom + "x" + x + "y" + y + ".jpg";
		}

	}

}
