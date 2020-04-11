package io.github.lbandc.cv19api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
class XlsDocument {

	private final File file;

	static class Model {

		String region;
		List<Map<String, String>> data = new ArrayList();
	}

	XlsDocument(File file) {
		this.file = file;
	}

	List<Model> processToModel() throws IOException, InvalidFormatException {
		XSSFWorkbook workbook = new XSSFWorkbook(this.file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		CellAddress firstDateCellAddress = this.findFirstDateCellAddress(sheet);
		CellAddress firstRegionCellAddress = this.findFirstRegionCellAddress(sheet);
		List<Model> models = new ArrayList<Model>();
		int startingRow = firstRegionCellAddress.getRow();
		int lastRow = this.findLastRow(sheet, startingRow);

		for (int i = startingRow; i < lastRow; i++) {
			var model = this.extractEntryFromStartingRegionCell(sheet, i, firstRegionCellAddress.getColumn(),
					firstDateCellAddress.getRow());
			models.add(model);
		}

		workbook.close();
		return models;

	}

	private CellAddress findFirstRegionCellAddress(XSSFSheet sheet) throws IOException {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (this.isRegionCell(cell)) {
					return cell.getAddress();
				}
			}
		}
		throw new IOException("Invalid Data Sheet");
	}

	private int findLastRow(XSSFSheet sheet, int startingRow) throws IOException {
		int index = startingRow;
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (this.isRegionCell(cell)) {
					index++;

				}
			}
		}
		return index;
	}

	private boolean isRegionCell(Cell cell) {

		return cell.getCellType().equals(CellType.STRING) &&

				cell.getStringCellValue().toLowerCase().trim().matches("^(east|london|midlands|north|south).*$");

	}

	private boolean isDateCell(Cell cell) {

		return cell.getCellType().equals(CellType.STRING)
				&& cell.getStringCellValue().toLowerCase().trim().matches("^([0-9]{2})-.*$");

	}

	private CellAddress findFirstDateCellAddress(XSSFSheet sheet) throws IOException {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (this.isDateCell(cell)) {
					return cell.getAddress();
				}
			}
		}
		throw new IOException("Invalid Data Sheet");
	}

	private Model extractEntryFromStartingRegionCell(XSSFSheet sheet, int rowIndex, int regionColIndex, int dateRow)
			throws IOException {

		if (!this.isRegionCell(sheet.getRow(rowIndex).getCell(regionColIndex))) {
			return null;
		}
		Row row = sheet.getRow(rowIndex);
		Model model = new Model();

		for (Cell cell : row) {

			if (cell.getColumnIndex() < regionColIndex) {
				continue;
			}

			if (this.isRegionCell(cell)) {
				model.region = cell.getStringCellValue();

			} else {
				Cell dateCell = sheet.getRow(dateRow).getCell(cell.getColumnIndex());

				if (!this.isDateCell(dateCell)) {
					continue;
				} else {
					var map = new HashMap<String, String>();
					map.put(dateCell.getStringCellValue(), String.valueOf(cell.getNumericCellValue()));
					model.data.add(map);

				}
			}
		}

		return model;
	}
}
