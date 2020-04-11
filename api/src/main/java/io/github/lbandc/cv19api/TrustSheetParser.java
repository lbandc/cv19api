package io.github.lbandc.cv19api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class TrustSheetParser {

	private final File file;
	private final URL url;
	private final String source;

	TrustSheetParser(File file) {
		this.file = file;
		this.source = file.getAbsolutePath();
		this.url = null;
	}

	TrustSheetParser(URL url) {
		this.url = url;
		this.source = url.toString();
		this.file = null;
	}

	List<Trust> parse() throws IOException, InvalidFormatException {

		XSSFWorkbook workbook;
		if (this.url != null) {
			BufferedInputStream inputStream = new BufferedInputStream(this.url.openStream());
			workbook = new XSSFWorkbook(inputStream);
		} else {
			workbook = new XSSFWorkbook(this.file);
		}

		XSSFSheet sheet = workbook.getSheetAt(1);
		ExcelDataFinderStrategy dataFinder = new ExcelDataFinderStrategy(sheet);
		CellAddress firstDateCellAddress = dataFinder.findFirstDateCellAddress();
		CellAddress firstRegionCellAddress = dataFinder.findFirstRegionCellAddress();
		List<Trust> models = new ArrayList<Trust>();
		int startingRow = firstRegionCellAddress.getRow();
		int lastRow = dataFinder.lastSignificantRow(startingRow);

		for (int i = startingRow; i < lastRow; i++) {
			var model = this.extractEntryFromStartingRegionCell(dataFinder, i, firstRegionCellAddress.getColumn(),
					firstDateCellAddress.getRow());
			models.add(model);
		}

		workbook.close();
		return models;

	}

	private Trust extractEntryFromStartingRegionCell(ExcelDataFinderStrategy dataFinder, int rowIndex,
			int regionColIndex, int dateRow) throws IOException {

		// first significant column
		if (!dataFinder.isRegionCell(rowIndex, regionColIndex)) {
			return null;
		}
		Row row = dataFinder.getRow(rowIndex);
		Region region = null;
		String code = null;
		String name = null;
		Map<LocalDate, Integer> deaths = new TreeMap<LocalDate, Integer>();

		for (Cell cell : row) {
			if (cell.getColumnIndex() < regionColIndex) {
				continue;
			}

			if (dataFinder.isRegionCell(cell.getRowIndex(), cell.getColumnIndex())) {
				region = Region.forName(cell.getStringCellValue());

			} else if (dataFinder.isCodeCell(cell)) {
				code = cell.getStringCellValue();
			} else if (dataFinder.isTrustNameCell(cell)) {
				name = cell.getStringCellValue();
			}

			else {
				Cell dateCell = dataFinder.getRow(dateRow).getCell(cell.getColumnIndex());

				if (!dataFinder.isDateCell(dateCell)) {
					continue;
				} else {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM yyyy");
					deaths.put(LocalDate.parse(dateCell.getStringCellValue().trim() + " 2020", formatter),
							(int) cell.getNumericCellValue());

				}
			}
		}
		if (code == null || name == null || region == null) {
			throw new IOException("Invalid data");
		}

		return new Trust(code, Instant.now(), this.source, name, region, deaths);
	}
}
