package at.bxm.running.graph.map;

import java.util.ArrayList;
import java.util.List;

public class DefaultMapLayout<T extends MapTile> implements MapLayout<T> {

	private final List<List<T>> tiles = new ArrayList<List<T>>();
	private int tileRows;
	private int tileColumns;

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
