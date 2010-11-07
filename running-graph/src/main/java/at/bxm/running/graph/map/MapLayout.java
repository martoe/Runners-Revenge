package at.bxm.running.graph.map;

// TODO return actual GPS rectangle
public interface MapLayout<T extends MapTile> {

	int getTileRows();

	int getTileColumns();

	int getTileWidth();

	int getTileHeight();

	T getTile(int row, int column);

}
