package at.bxm.running.graph.map;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO wiring stuff
public class MapService {

	private final Log logger = LogFactory.getLog(getClass());
	private MapProvider mapProvider;

	public void setMapProvider(MapProvider value) {
		mapProvider = value;
	}

	// TODO right now this is only a test method
	public void loadImages(double longitudeMin, double latitudeMin, double longituteMax,
					double latitiudeMax, double resolution) throws IOException {
		MapLayout<?> layout = mapProvider.getLayout(longitudeMin, latitudeMin, longituteMax,
						latitiudeMax, resolution);
		for (int row = 0; row < layout.getTileRows(); row++) {
			for (int col = 0; col < layout.getTileColumns(); col++) {
				MapTile tile = layout.getTile(row, col);
				byte[] image = readFromCache(tile);
				if (image == null) {
					image = tile.loadImage();
					writeToCache(tile, image);
				}
			}
		}
	}

	private byte[] readFromCache(MapTile tile) throws IOException {
		File source = new File(getCacheDir(), tile.getUniqueFilename());
		if (!source.exists()) {
			return null;
		}
		logger.debug("Reading from cache: " + source.getAbsolutePath());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(source));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buffer = new byte[1000];
			int read;
			while ((read = in.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			return out.toByteArray();
		} finally {
			try {
				in.close();
			} catch (Exception ignore) {}
		}
	}

	private void writeToCache(MapTile tile, byte[] image) throws IOException {
		File target = new File(getCacheDir(), tile.getUniqueFilename());
		logger.debug("Writing " + image.length + " bytes to " + target.getAbsolutePath());
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(target));
			out.write(image);
		} finally {
			try {
				out.close();
			} catch (Exception ignore) {}
		}
	}

	// TODO better use a local var
	private File getCacheDir() throws IOException {
		File cacheDir = new File("cache/" + mapProvider.getClass().getName());
		cacheDir.mkdirs();
		if (!cacheDir.exists()) {
			throw new IOException("Cache directory doesn't exist: " + cacheDir.getAbsolutePath());
		}
		// TODO other error handling
		return cacheDir;
	}

}
