package at.bxm.running.graph.map;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DefaultMapLayout<T extends MapTile> implements MapLayout<T> {

	private final List<List<T>> tiles = new ArrayList<List<T>>();
	private final double latNorth;
	private final double latSouth;
	private final double lonEast;
	private final double lonWest;
	private final int tileWidth;
	private final int tileHeight;
	private int tileRows;
	private int tileColumns;

	public DefaultMapLayout(double latNorth, double latSouth, double lonEast, double lonWest,
					int tileWidth, int tileHeight) {
		this.latNorth = latNorth;
		this.latSouth = latSouth;
		this.lonEast = lonEast;
		this.lonWest = lonWest;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	@Override
	public double getLatNorth() {
		return latNorth;
	}

	@Override
	public double getLatSouth() {
		return latSouth;
	}

	@Override
	public double getLonEast() {
		return lonEast;
	}

	@Override
	public double getLonWest() {
		return lonWest;
	}

	@Override
	public int getTileWidth() {
		return tileWidth;
	}

	@Override
	public int getTileHeight() {
		return tileHeight;
	}

	@Override
	public int getTileRows() {
		return tileRows;
	}

	@Override
	public int getTileColumns() {
		return tileColumns;
	}

	@Override
	public T getTile(int row, int column) {
		return tiles.get(row).get(column);
	}

	public void addTile(int row, int column, T value) {
		for (int i = tiles.size(); i <= row; tiles.add(null), i++);
		List<T> tileRow = tiles.get(row);
		if (tileRow == null) {
			tileRow = new ArrayList<T>();
			tiles.set(row, tileRow);
		}
		for (int i = tileRow.size(); i <= column; tileRow.add(null), i++);
		tileRow.set(column, value);
		tileRows = Math.max(tileRows, tiles.size());
		tileColumns = Math.max(tileColumns, tileRow.size());
	}

}
