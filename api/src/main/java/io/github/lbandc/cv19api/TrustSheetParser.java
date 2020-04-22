package io.github.lbandc.cv19api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data sheet that will allow us to breakdown deaths by trust and by region
 * 
 */
class TrustSheetParser {

	private final String source;
	private final Sheet sheet;
	private final LocalDate recordedOn;

	TrustSheetParser(Sheet sheet, LocalDate recordedOn, String source) {

		this.recordedOn = recordedOn;
		this.source = source;
		this.sheet = sheet;

	}

	private Optional<RowIndex> getFirstRegionRowIndex(Sheet sheet) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (CellDataMatcher.getRegion(cell).isPresent()) {
					return Optional.of(cell.getRowIndex());
				}
			}
		}
		return Optional.empty();
	}

	private Optional<RowIndex> getLastRegionRowIndex(Sheet sheet) {
		int lastRow = 0;
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (CellDataMatcher.getRegion(cell).isPresent()) {
					lastRow = row.getIndexAsInt();

				}
			}
		}
		return Optional.of(new RowIndex(lastRow));
	}

	List<DeathRecordByTrust> parse() throws IOException {

		GeneralDataMatchingStrategy dataMatcher = new GeneralDataMatchingStrategy(this.sheet);

		Optional<RowIndex> startingRow = dataMatcher.getSignificantRowIndex(t -> {

			return getFirstRegionRowIndex(this.sheet);

		});
		Optional<RowIndex> lastRow = dataMatcher.getSignificantRowIndex(t -> {

			return getLastRegionRowIndex(this.sheet);

		});
		if (startingRow.isEmpty() || lastRow.isEmpty()) {
			throw new IOException("No significant Trust data found");
		}

		RowToDeathRecordByTrustMapper mapper = new RowToDeathRecordByTrustMapper(this.sheet,
				dataMatcher.getFirstDateCell());
		List<DeathRecordByTrust> models = new ArrayList<>();

		this.sheet.getSortedSubListOfRows(startingRow.get(), lastRow.get()).forEach(row -> {
			models.addAll(mapper.map(row, this.source, this.recordedOn));
		});

		return models;

	}

}
