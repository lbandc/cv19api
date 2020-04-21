package io.github.lbandc.cv19api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data sheet that will allow us to breakdown deaths by trust and by region
 * 
 */
class SheetParser {

	private final String source;
	private final Sheet sheet;
	private final LocalDate recordedOn;

	SheetParser(Sheet sheet, LocalDate recordedOn, String source) {

		this.recordedOn = recordedOn;
		this.source = source;
		this.sheet = sheet;

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
