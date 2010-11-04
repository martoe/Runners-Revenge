package at.bxm.running.graph.map;

public interface MapTile {
	String getUniqueFilename();

	byte[] loadImage();
}
