package io.github.lbandc.cv19api;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import lombok.Value;

@Value
final class Row implements Iterable<Cell>, Comparable<Row> {

	private final Map<ColumnIndex, Cell> cells;
	private final RowIndex rowIndex;

	Row(RowIndex rowIndex, List<Cell> cells) {
		this.rowIndex = rowIndex;
		if (cells == null) {
			throw new NullPointerException();
		}
		if (cells.size() < 1) {
			throw new IllegalArgumentException("Empty row");
		}
		Map<ColumnIndex, Cell> map = new HashMap<ColumnIndex, Cell>();
		cells.forEach(c -> {
			if (c.getPosition().getValue0().equals(this.rowIndex)) {
				map.put(c.getColumnIndex(), c);
			}
		});
		this.cells = Collections.unmodifiableMap(map);
	}

	@Override
	public Iterator<Cell> iterator() {
		var set = new TreeSet<Cell>(new CellComparator());
		set.addAll(this.cells.values());
		return set.iterator();
	}

	RowIndex getIndex() {
		return this.rowIndex;
	}

	int getIndexAsInt() {
		return this.rowIndex.getValue();
	}

	boolean isAbove(Row row) {
		return this.rowIndex.isAbove(row.getRowIndex());
	}

	boolean isBelow(Row row) {
		return this.rowIndex.isBelow(row.getRowIndex());
	}

	Cell getCell(ColumnIndex idx) {
		return this.cells.get(idx);
	}

	@Override
	public int compareTo(Row other) {
		class RowComparator implements Comparator<Row> {

			@Override
			public int compare(Row o1, Row o2) {

				if (o1.isAbove(o2))
					return -1;
				if (o1.isBelow(o2))
					return 1;
				return 0;
			}
		}
		return new RowComparator().compare(this, other);
	}

}

final class CellComparator implements Comparator<Cell> {

	@Override
	public int compare(Cell o1, Cell o2) {

		if (o1.isInSamePositionAs(o2))
			return 0;
		if (o1.isToTheRightOf(o2))
			return 1;
		if (!o1.isToTheRightOf(o2))
			return -1;

		throw new IllegalStateException();

	}

}