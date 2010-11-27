package at.bxm.running.maps.providers;

import static at.bxm.running.maps.providers.GoogleMapsConverter.*;
import at.bxm.running.maps.CachedMapTile;
import at.bxm.running.maps.DefaultMapLayout;
import at.bxm.running.maps.MapLayout;
import at.bxm.running.maps.MapProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public abstract class GoogleMaps implements MapProvider {

	private final HttpClient httpclient = new DefaultHttpClient();

	@Override
	public MapLayout<GoogleMapTile> getLayout(double latNorth, double latSouth, double lonEast,
					double lonWest, double resolution) {
		// TODO implement zoomlevel/resolution mapping
		int zoomlevel = (int)resolution;
		int yMin = toY(latNorth, zoomlevel);
		int yMax = toY(latSouth, zoomlevel);
		int xMax = toX(lonEast, zoomlevel);
		int xMin = toX(lonWest, zoomlevel);
		DefaultMapLayout<GoogleMapTile> layout = new DefaultMapLayout<GoogleMapTile>(toLatitude(yMin,
						zoomlevel), toLatitude(yMax + 1, zoomlevel), toLongitude(xMax + 1, zoomlevel),
						toLongitude(xMin, zoomlevel), 256, 256);
		for (int x = xMin, col = 0; x <= xMax; x++, col++) {
			for (int y = yMin, row = 0; y <= yMax; y++, row++) {
				layout.addTile(row, col, new GoogleMapTile(zoomlevel, x, y));
			}
		}
		return layout;
	}

	abstract URI getUrl(int x, int y, int zoom);

	abstract int getMinZoomLevel();

	abstract int getMaxZoomLevel();

	private class GoogleMapTile extends CachedMapTile {
		private final Logger logger = Logger.getLogger(getClass());
		private final int zoom;
		private final int x;
		private final int y;

		public GoogleMapTile(int zoom, int x, int y) {
			super(GoogleMaps.this.getClass().getSimpleName());
			this.zoom = Math.min(Math.max(zoom, getMinZoomLevel()), getMaxZoomLevel());
			this.x = x;
			this.y = y;
		}

		@Override
		public byte[] loadImage() throws IOException {
			// TODO use connection timeouts?
			HttpGet req = new HttpGet(getUrl(x, y, zoom));

			// http://mt3.google.com/vt/lyrs=&hl=en&x=8935&y=5686&z=14&s=Galileo

			logger.debug("Downloading: " + req.getURI());
			HttpResponse response = httpclient.execute(req);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.warn("Request to " + req.getURI() + " returned " + response.getStatusLine());
				req.abort();
				return new byte[0];
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1000];
			int read;
			InputStream in = null;
			try {
				in = response.getEntity().getContent();
				while ((read = in.read(buffer)) > 0) {
					out.write(buffer, 0, read);
				}
			} finally {
				try {
					in.close();
				} catch (Exception ignore) {}
			}
			//response.getEntity().consumeContent();
			return out.toByteArray();
		}

		@Override
		protected String getCacheFile() {
			return "z" + zoom + "x" + x + "y" + y + ".jpg";
		}

	}

}
