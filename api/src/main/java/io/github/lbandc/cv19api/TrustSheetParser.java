package io.github.lbandc.cv19api;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Data sheet that will allow us to breakdown deaths by trust and by region
 * 
 */
class TrustSheetParser {

	private final String source;
	private final XSSFWorkbook workbook;
	private final XSSFSheet sheet;
	private final LocalDate recordedOn;
	private final List<Row> rows;

	TrustSheetParser(InputStream inputStream, LocalDate recordedOn, String source) throws IOException {

		if (null == source || null == recordedOn || null == inputStream) {
			throw new IllegalArgumentException();
		}

		this.recordedOn = recordedOn;
		this.source = source;
		this.workbook = new XSSFWorkbook(inputStream);
		this.sheet = iniSheet();
		this.rows = new ArrayList<Row>(new XlsxSheetToRowMapper(this.sheet).getRows());
		Collections.sort(this.rows);

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

		DataMatchingStrategy dataFinder = new DataMatchingStrategy(new ArrayList<Row>(this.rows));
		Cell firstDateCell = dataFinder.getFirstDateCell();
		Cell firstRegionCell = dataFinder.getFirstRegionCell();

		RowIndex startingRow = firstRegionCell.getRowIndex();
		RowIndex lastRow = dataFinder.getLastSignificantRowIndex(startingRow);

		List<DeathRecordByTrust> models = new ArrayList<>();

		for (int r = startingRow.getValue(); r <= lastRow.getValue(); r++) {
			Row row = this.rows.get(r);
			Region region = null;
			String code = null;
			String name = null;
			Integer deathCount = null;
			LocalDate dayOfDeath = null;

			for (Cell cell : row) {
				Optional<Region> optRegion = dataFinder.getRegion(cell);
				if (optRegion.isPresent()) {
					region = optRegion.get();
				}
				Optional<String> optCode = dataFinder.getCode(cell);
				if (optCode.isPresent()) {
					code = optCode.get();
				}
				Optional<String> optTrustName = dataFinder.getTrustName(cell);
				if (optTrustName.isPresent()) {
					name = optTrustName.get();
				}

				if (cell.isInSameColumnAs(firstDateCell)) {
					System.out.println("Below " + cell.getColumnIndex() + " " + cell.toString());
					var optDeathCount = dataFinder.getDeathCount(cell);
					System.out.println(firstDateCell.getRowIndex());
					Row dateRow = this.rows.get(firstDateCell.getRowIndex().getValue());
					if (optDeathCount.isPresent()) {
						deathCount = (int) optDeathCount.get().doubleValue();
						var dateCell = dateRow.getCell(cell.getColumnIndex());
						System.out.println(dateCell.toString());
						dayOfDeath = dateRow.getCell(dateCell.getColumnIndex()).toLocalDate().get();
					}
				}

			}

			Trust trust = Trust.builder().code(code).name(name).region(region).build();
			DeathRecordByTrust record = DeathRecordByTrust.builder().dayOfDeath(dayOfDeath).deaths(deathCount)
					.trust(trust).recordedOn(this.recordedOn).source(new Ingest(this.source)).build();
			models.add(record);

		}
		return models;

	}

}
