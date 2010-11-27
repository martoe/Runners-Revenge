package at.bxm.running.maps;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 * Base class for MapTiles that use a local file cache
 * 
 * TODO use a caching framework (Ehcache?)
 */
public abstract class CachedMapTile implements MapTile {

	protected final Logger logger = Logger.getLogger(getClass());
	private static final String PROP_CACHEDIR = "at.bxm.running.maps.cachedir";
	private final String cacheName;
	private byte[] image;

	public CachedMapTile(String cacheName) {
		this.cacheName = cacheName;
	}

	@Override
	public final byte[] getImage() throws IOException {
		if (image == null) {
			image = readFromCache();
			if (image == null) {
				logger.debug("Loading image for " + this);
				image = loadImage();
				writeToCache();
			}
		}
		if (image.length == 0) {
			throw new IOException("Invalid image: " + cacheName);
		}
		return image;
	}

	@Override
	public final InputStream getImageStream() throws IOException {
		return new ByteArrayInputStream(getImage());
	}

	protected abstract String getCacheFile();

	protected abstract byte[] loadImage() throws IOException;

	private byte[] readFromCache() throws IOException {
		File source = new File(getCacheDir(), getCacheFile());
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

	private void writeToCache() throws IOException {
		if (image != null) {
			File target = new File(getCacheDir(), getCacheFile());
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
	}

	// TODO better use a local var
	private File getCacheDir() throws IOException {
		String dir = System.getProperty(PROP_CACHEDIR);
		if (dir == null) {
			dir = System.getProperty("user.home") + "/.running/cache";
		}
		File cacheDir = new File(dir + "/" + cacheName);
		cacheDir.mkdirs();
		if (!cacheDir.exists()) {
			throw new IOException("Cache directory doesn't exist: " + cacheDir.getAbsolutePath());
		}
		// TODO other error handling
		return cacheDir;
	}

}
