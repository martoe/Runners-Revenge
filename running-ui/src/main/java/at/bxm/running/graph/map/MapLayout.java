package at.bxm.running.graph.map;

@Deprecated
public interface MapLayout<T extends MapTile> {

	int getTileRows();

	int getTileColumns();

	int getTileWidth();

	int getTileHeight();

	/** @return the latitude of the upper (north) end of the northernmost tile */
	double getLatNorth();

	/** @return the latitude of the lower (south) end of the southernmost tile */
	double getLatSouth();

	/** @return the longitude of the right (east) end of the easternmost tile */
	double getLonEast();

	/** @return the longitude of the left (west) end of the westernmost tile */
	double getLonWest();

	T getTile(int row, int column);

}
