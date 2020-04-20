package io.github.lbandc.cv19api;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Data sheet that will allow us to breakdown deaths by trust and by region
 * 
 */
class TrustSheetParser {

	private final String source;
	private final XSSFWorkbook workbook;
	private final Sheet sheet;
	private final LocalDate recordedOn;

	TrustSheetParser(InputStream inputStream, LocalDate recordedOn, String source) throws IOException {

		if (null == source || null == recordedOn || null == inputStream) {
			throw new IllegalArgumentException();
		}

		this.recordedOn = recordedOn;
		this.source = source;
		this.workbook = new XSSFWorkbook(inputStream);
		this.sheet = new XlsxSheetMapper(iniSheet()).getSheet();

	}

	private XSSFSheet iniSheet() throws IOException {
		if (this.workbook == null) {
			throw new IOException("Workbook not initialised");
		}
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

	List<DeathRecordByTrust> parse() throws IOException {

		DataMatchingStrategy dataFinder = new DataMatchingStrategy(this.sheet);

		RowIndex startingRow = dataFinder.getFirstRegionCell().getRowIndex();
		RowIndex lastRow = dataFinder.getLastSignificantRowIndex(startingRow);

		RowToDeathRecordByTrustMapper mapper = new RowToDeathRecordByTrustMapper(dataFinder, this.sheet);
		List<DeathRecordByTrust> models = new ArrayList<>();
		this.sheet.getSortedSubListOfRows(startingRow, lastRow).forEach(row -> {
			models.addAll(mapper.map(row, this.source, this.recordedOn));
		});

		return models;

	}

}
