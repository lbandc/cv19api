package io.github.lbandc.cv19api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import lombok.Value;

@Value
class Sheet implements Iterable<Row> {

	private final List<Row> rows;
	private final Map<RowIndex, Row> map;

	Sheet(List<Row> rows) {
		this.rows = Collections.unmodifiableList(rows);
		final Map<RowIndex, Row> map = new HashMap<RowIndex, Row>();
		this.rows.forEach(r -> {
			map.put(r.getIndex(), r);
		});
		this.map = Collections.unmodifiableMap(map);

	}

	Row getRow(RowIndex idx) {
		return this.map.get(idx);
	}

	Cell getCell(int rowIndex, int colIndex) {
		return this.getRow(new RowIndex(rowIndex)).getCell(new ColumnIndex(colIndex));
	}

	List<Row> getSubListOfRows(RowIndex start, RowIndex end) {
		List<Row> list = new ArrayList<Row>();
		for (int i = start.getValue(); i <= end.getValue(); i++) {
			if (this.map.containsKey(new RowIndex(i))) {
				list.add(map.get(new RowIndex(i)));
			}
		}
		Collections.sort(list, new RowComparator());
		return list;
	}

	@Override
	public Iterator<Row> iterator() {

		return new TreeSet<Row>(this.rows).iterator();
	}
}
