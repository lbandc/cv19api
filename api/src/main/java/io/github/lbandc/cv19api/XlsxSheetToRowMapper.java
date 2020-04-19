package io.github.lbandc.cv19api;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.poi.xssf.usermodel.XSSFSheet;

class XlsxSheetToRowMapper {

	private final XSSFSheet sheet;

	public XlsxSheetToRowMapper(final XSSFSheet sheet) {
		if (null == sheet) {
			throw new IllegalArgumentException();
		}
		this.sheet = sheet;
	}

	SortedSet<Row> getRows() {
		List<Row> rows = new ArrayList<Row>();
		this.sheet.rowIterator().forEachRemaining(r -> {
			List<Cell> cells = new ArrayList<Cell>();
			r.cellIterator().forEachRemaining(c -> {
				if (null != c) {
					cells.add(new Cell(new org.javatuples.Pair<RowIndex, ColumnIndex>(new RowIndex(r.getRowNum()),
							new ColumnIndex(c.getColumnIndex())), r.getCell(c.getColumnIndex()).toString()));
				}

			});
			if (cells.size() > 0) {
				rows.add(new Row(new RowIndex(r.getRowNum()), cells));
			}
		});
		System.out.println(rows);
		return new TreeSet<Row>(rows);
	}
}
