package at.bxm.running.graph.map;

public abstract class AbstractMapTile implements MapTile {

	private final String uniqueFilename;

	public AbstractMapTile(String uniqueFilename) {
		this.uniqueFilename = uniqueFilename;
	}

	@Override
	public String getUniqueFilename() {
		return uniqueFilename;
	}

}
