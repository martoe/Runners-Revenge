package at.bxm.running.graph.map;

// TODO return actual GPS rectangle
public interface MapLayout<T extends MapTile> {

	int getTileRows();

	int getTileColumns();

	T getTile(int row, int column);

}
