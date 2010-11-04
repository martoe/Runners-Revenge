package at.bxm.running.graph.map;

import java.io.IOException;

public interface MapTile {
	String getUniqueFilename();

	byte[] loadImage() throws IOException;
}
