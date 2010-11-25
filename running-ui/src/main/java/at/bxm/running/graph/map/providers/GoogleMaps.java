package at.bxm.running.graph.map.providers;

import static at.bxm.running.graph.map.providers.GoogleMapsConverter.*;
import at.bxm.running.graph.map.CachedMapTile;
import at.bxm.running.graph.map.DefaultMapLayout;
import at.bxm.running.graph.map.MapLayout;
import at.bxm.running.graph.map.MapProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

@Deprecated
public abstract class GoogleMaps implements MapProvider {

	@Override
	public MapLayout<GoogleMapTile> getLayout(double latNorth, double latSouth, double lonEast,
					double lonWest, double resolution) {
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
			HttpURLConnection conn = (HttpURLConnection)getUrl(x, y, zoom).toURL().openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1000];
			int read;
			while ((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			conn.disconnect();
			return out.toByteArray();
		}

		@Override
		protected String getCacheFile() {
			return "z" + zoom + "x" + x + "y" + y + ".jpg";
		}

	}

}
