package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class RowToDeathRecordByAgeMapper {

	private final Sheet sheet;
	private final Cell firstDateCell;

	public RowToDeathRecordByAgeMapper(final Sheet sheet, final Cell firstDateCell) {

		this.sheet = sheet;
		this.firstDateCell = firstDateCell;
	}

	List<DeathRecordByAge> map(final Row row, String source, LocalDate recordedOn) {

		AgeRange ageRange = null;
		Integer deathCount = null;
		LocalDate dayOfDeath = null;
		List<DeathRecordByAge> models = new ArrayList<>();
		for (Cell cell : row) {

			Optional<AgeRange> optAge = CellDataMatcher.getAgeRange(cell);
			if (optAge.isPresent()) {
				ageRange = optAge.get();
			}

			var optDeathCount = CellDataMatcher.getDeathCount(cell,
					this.sheet.getRow(this.firstDateCell.getRowIndex()));

			Row dateRow = this.sheet.getRow(firstDateCell.getRowIndex());
			if (optDeathCount.isPresent()) {
				deathCount = (int) optDeathCount.get().doubleValue();
				var dateCell = dateRow.getCell(cell.getColumnIndex());
				dayOfDeath = dateRow.getCell(dateCell.getColumnIndex()).toLocalDate().get();

				DeathRecordByAge record = DeathRecordByAge.builder().dayOfDeath(dayOfDeath).deaths(deathCount)
						.ageRange(ageRange).recordedOn(recordedOn).source(new Ingest(source)).build();

				models.add(record);

			}

		}
		return models;
	}
}
