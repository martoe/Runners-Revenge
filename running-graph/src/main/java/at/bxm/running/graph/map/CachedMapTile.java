package at.bxm.running.graph.map;

import java.io.IOException;

public abstract class CachedMapTile implements MapTile {

	private final String uniqueFilename;
	private byte[] image;

	public CachedMapTile(String uniqueFilename) {
		this.uniqueFilename = uniqueFilename;
	}

	@Override
	public String getUniqueFilename() {
		return uniqueFilename;
	}

	@Override
	public byte[] getImage() throws IOException {
		if (image == null) {
			image = loadImage();
		}
		return image;
	}

	protected abstract byte[] loadImage() throws IOException;

}
