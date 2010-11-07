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

	@Override
	public MapLayout<GoogleMapTile> getLayout(double longitudeMin, double latitudeMin,
					double longitudeMax, double latitiudeMax, double resolution) {
		// TODO implement

		// mapping GPS to Google x/y:
		// lon 0 = greenwich, -180 = westende, 180 = ostende
		// lat 0 = äquator, 90 = nordpol, -90 = südpol
		// zoomlevel z: einteilung in 2^z * 2^z images
		// bsp zoomlevel 17: 131000*131000 images, lon/lat 0/0 bei 65500/65500
		DefaultMapLayout<GoogleMapTile> layout = new DefaultMapLayout<GoogleMapTile>(256, 256);
		for (int x = (int)longitudeMin, i = 0; x <= longitudeMax; x++, i++) {
			for (int y = (int)latitudeMin, j = 0; y <= latitiudeMax; y++, j++) {
				layout.addTile(i, j, new GoogleMapTile((int)resolution, x, y));
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
