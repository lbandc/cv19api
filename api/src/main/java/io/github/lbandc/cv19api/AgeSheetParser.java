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
class AgeSheetParser {

	private final String source;
	private final Sheet sheet;
	private final LocalDate recordedOn;

	AgeSheetParser(Sheet sheet, LocalDate recordedOn, String source) {

		this.recordedOn = recordedOn;
		this.source = source;
		this.sheet = sheet;

	}

	private Optional<RowIndex> getFirstAgeRowIndex(Sheet sheet) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (CellDataMatcher.getAgeRange(cell).isPresent()) {
					return Optional.of(cell.getRowIndex());
				}
			}
		}
		return Optional.empty();
	}

	private Optional<RowIndex> getLastAgeRowIndex(Sheet sheet) {
		int lastRow = 0;
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (CellDataMatcher.getAgeRange(cell).isPresent()) {
					lastRow = row.getIndexAsInt();
				}
				if (CellDataMatcher.getRegion(cell).isPresent()) {
					lastRow = row.getIndexAsInt();

				}
			}
		}
		return Optional.of(new RowIndex(lastRow));
	}

	List<DeathRecordByAge> parse() throws IOException {

		GeneralDataMatchingStrategy dataMatcher = new GeneralDataMatchingStrategy(this.sheet);

		Optional<RowIndex> startingRow = dataMatcher.getSignificantRowIndex(t -> {

			return getFirstAgeRowIndex(this.sheet);

		});
		Optional<RowIndex> lastRow = dataMatcher.getSignificantRowIndex(t -> {

			return getLastAgeRowIndex(this.sheet);

		});
		if (startingRow.isEmpty() || lastRow.isEmpty()) {
			throw new IOException("No significant Trust data found");
		}

		RowToDeathRecordByAgeMapper mapper = new RowToDeathRecordByAgeMapper(this.sheet,
				dataMatcher.getFirstDateCell());
		List<DeathRecordByAge> models = new ArrayList<>();
		this.sheet.getSortedSubListOfRows(startingRow.get(), lastRow.get()).forEach(row -> {
			models.addAll(mapper.map(row, this.source, this.recordedOn));
		});

		return models;

	}

}
