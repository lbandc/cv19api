package io.github.lbandc.cv19api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
	private final XSSFWorkbook workbook;
	private final XSSFSheet sheet;
	private final LocalDate recordedOn;

	TrustSheetParser(File file, LocalDate recordedOn) throws InvalidFormatException, IOException {
		this.file = file;
		this.source = file.getAbsolutePath();
		this.url = null;
		this.workbook = new XSSFWorkbook(this.file);
		this.sheet = iniSheet();
		this.recordedOn = recordedOn;
	}

	TrustSheetParser(URL url, LocalDate recordedOn) throws IOException {
		this.url = url;
		this.source = url.toString();
		this.file = null;
		BufferedInputStream inputStream = new BufferedInputStream(this.url.openStream());
		this.workbook = new XSSFWorkbook(inputStream);
		this.sheet = iniSheet();
		this.recordedOn = recordedOn;

	}

	private XSSFSheet iniSheet() throws IOException {
		XSSFSheet sheet = null;
		for (int s = 0; s < this.workbook.getNumberOfSheets(); s++) {
			XSSFSheet sht = this.workbook.getSheetAt(s);
			if (sht.getSheetName().contains("by trust")) {
				sheet = sht;
			}

		}
		if (sheet == null) {
			this.workbook.close();
			throw new IOException("Not a valid Trust data sheet");
		}
		return sheet;
	}

	List<DeathRecordByTrust> parse() throws IOException, InvalidFormatException {

		ExcelDataFinderStrategy dataFinder = new ExcelDataFinderStrategy(sheet);
		CellAddress firstDateCellAddress = dataFinder.findFirstDateCellAddress();
		CellAddress firstRegionCellAddress = dataFinder.findFirstRegionCellAddress();
		List<DeathRecordByTrust> models = new ArrayList<DeathRecordByTrust>();
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

	private DeathRecordByTrust extractEntryFromStartingRegionCell(ExcelDataFinderStrategy dataFinder, int rowIndex,
			int regionColIndex, int dateRow) throws IOException {

		// first significant column
		if (!dataFinder.isRegionCell(rowIndex, regionColIndex)) {
			return null;
		}
		Row row = dataFinder.getRow(rowIndex);
		Region region = null;
		String code = null;
		String name = null;
		Integer deathCount = null;
		LocalDate dayOfDeath = null;
		Map<LocalDate, Integer> deaths = new TreeMap<LocalDate, Integer>();

		// todo extract this out into testable strategies
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
					// System.out.println("DEATHS " + " " + region + " " + code + " " + name + " " +
					// cell.toString());
					String dateString = dateCell.toString().trim();
					if (!dateString.endsWith("-2020")) {
						dateString = dateString + "-2020";
					}

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
					dayOfDeath = LocalDate.parse(dateString, formatter);
					deathCount = (int) cell.getNumericCellValue();

				}
			}
		}
		if (code == null || name == null || region == null || dayOfDeath == null || deathCount == null) {
			throw new IOException("Invalid data");
		}

		Trust trust = Trust.builder().code(code).name(name).region(region).build();
		DeathRecordByTrust record = DeathRecordByTrust.builder().dayOfDeath(dayOfDeath).deaths(deathCount).trust(trust)
				.recordedOn(this.recordedOn).source(new Ingest(this.source)).build();
		return record;
	}
}
