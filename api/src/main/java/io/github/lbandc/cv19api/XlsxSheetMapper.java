package io.github.lbandc.cv19api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class XlsxSheetMapper {

	private final XSSFSheet sheet;

	public XlsxSheetMapper(InputStream inputStream, String sheetName) throws IOException {
		this(new XSSFWorkbook(inputStream), sheetName);
	}

	public XlsxSheetMapper(final XSSFSheet sheet) {
		if (null == sheet) {
			throw new IllegalArgumentException();
		}
		this.sheet = sheet;
	}

	public XlsxSheetMapper(XSSFWorkbook workbook, String sheetName) throws IOException {
		if (workbook == null) {
			throw new IOException("Workbook not initialised");
		}
		XSSFSheet sheet = null;
		for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
			XSSFSheet sht = workbook.getSheetAt(s);
			if (sht.getSheetName().contains("by trust")) {
				sheet = sht;
			}

		}
		if (sheet == null) {
			workbook.close();
			throw new IOException("Not a valid data sheet for " + sheetName);
		}
		this.sheet = sheet;
	}

	Sheet getSheet() {
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

		return new Sheet(rows);
	}
}
