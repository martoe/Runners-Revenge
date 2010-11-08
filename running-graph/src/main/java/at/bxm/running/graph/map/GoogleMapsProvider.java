package at.bxm.running.graph.map;

import static at.bxm.running.graph.map.GoogleMapsConverter.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GoogleMapsProvider implements MapProvider {
	private final Log logger = LogFactory.getLog(getClass());

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