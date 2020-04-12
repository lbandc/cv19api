package io.github.lbandc.cv19api;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;

class ExcelDataFinderStrategy {

	private XSSFSheet sheet;

	ExcelDataFinderStrategy(XSSFSheet sheet) {
		this.sheet = sheet;
	}

	Row getRow(int index) {
		return this.sheet.getRow(index);
	}

	Cell getCell(int rowIndex, int colIndex) {
		return this.getRow(rowIndex).getCell(colIndex);
	}

	CellAddress findFirstRegionCellAddress() throws IOException {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (this.isRegionCell(cell.getRowIndex(), cell.getColumnIndex())) {
					return cell.getAddress();
				}
			}
		}
		throw new IOException("Data Sheet does not contain Region data");
	}

	int lastSignificantRow(int startingRow) {
		int index = startingRow;
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (this.isRegionCell(cell.getRowIndex(), cell.getColumnIndex())) {
					index++;

				}
			}
		}
		return index;
	}

	boolean isRegionCell(int rowIndex, int colIndex) {

		var cell = this.sheet.getRow(rowIndex).getCell(colIndex);

		if (!cell.getCellType().equals(CellType.STRING)) {
			return false;
		}
		try {
			Region.forName(cell.getStringCellValue().trim());
			return true;

		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	boolean isCodeCell(Cell cell) {
		return cell.getCellType().equals(CellType.STRING)
				&& cell.getStringCellValue().trim().matches("^([0-9A-Z]{3,6})$");

	}

	boolean isTrustNameCell(Cell cell) {
		return cell.getCellType().equals(CellType.STRING)
				// && cell.getStringCellValue().trim().matches("^[–',.!?\\-\\sA-Z]+$");
				&& cell.getStringCellValue().trim().matches("^[A-Z]+.*$") && cell.getStringCellValue().length() > 10;

	}

	boolean isDateCell(Cell cell) {
		if (null == cell)
			return false;
		if (cell.getCellType().equals(CellType.NUMERIC) || cell.getCellType().equals(CellType.STRING)) {

			return cell.toString().toLowerCase().trim().matches("^[0-9]{2}-[A-Za-z]{3}-2020$");

		}
		return false;
	}

	CellAddress findFirstDateCellAddress() throws IOException {
		for (Row row : sheet) {
			if (row.getRowNum() < 11)
				continue;
			for (Cell cell : row) {
				if (this.isDateCell(cell)) {
					return cell.getAddress();
				}
			}
		}
		throw new IOException("Invalid Data Sheet");
	}

}
