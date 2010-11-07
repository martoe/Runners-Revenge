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

	protected int calculateX(double longitude, int zoomlevel) {
		double tileCount = Math.pow(2, zoomlevel);
		double x = (longitude + 180) // range 0..360 (west to east)
						/ 360 // range 0..1
						* tileCount;
		logger.debug("lon " + longitude + ", zoom " + zoomlevel + " -> x=" + x);
		return (int)Math.floor(x);
	}

	protected int calculateY(double latitude, int zoomlevel) {
		// FIXME teilung 1/3 : 2/3
		double tileCount = Math.pow(2, zoomlevel);
		double y = (LATITUDE_MAX - latitude) // range 0..180 (north to south)
						/ (LATITUDE_MAX * 2) // range 0..1
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
		// TODO implement
		int zoomlevel = (int)resolution;
		int xMin = calculateX(longitudeMin, zoomlevel);
		int yMin = calculateY(latitudeMin, zoomlevel);
		int xMax = calculateX(longitudeMax, zoomlevel);
		int yMax = calculateY(latitudeMax, zoomlevel);
		// mapping GPS to Google x/y:
		// lon 0 = greenwich, -180 = westende, 180 = ostende
		// lat 0 = äquator, 90 = nordpol, -90 = südpol
		// zoomlevel z: einteilung in 2^z * 2^z images
		// bsp zoomlevel 17: 131000*131000 images, lon/lat 0/0 bei 65500/65500
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
