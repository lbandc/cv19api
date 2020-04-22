package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class RowToDeathRecordByTrustMapper {

	private final Sheet sheet;
	private final Cell firstDateCell;

	public RowToDeathRecordByTrustMapper(final Sheet sheet, final Cell firstDateCell) {

		this.sheet = sheet;
		this.firstDateCell = firstDateCell;
	}

	List<DeathRecordByTrust> map(final Row row, String source, LocalDate recordedOn) {

		Region region = null;
		String code = null;
		String name = null;
		Integer deathCount = null;
		LocalDate dayOfDeath = null;
		List<DeathRecordByTrust> models = new ArrayList<>();
		for (Cell cell : row) {

			Optional<Region> optRegion = CellDataMatcher.getRegion(cell);
			if (optRegion.isPresent()) {
				region = optRegion.get();
			}
			Optional<String> optCode = CellDataMatcher.getCode(cell);
			if (optCode.isPresent()) {
				code = optCode.get();
			}
			Optional<String> optTrustName = CellDataMatcher.getTrustName(cell);
			if (optTrustName.isPresent()) {
				name = optTrustName.get();
			}

			var optDeathCount = CellDataMatcher.getDeathCount(cell,
					this.sheet.getRow(this.firstDateCell.getRowIndex()));

			Row dateRow = this.sheet.getRow(firstDateCell.getRowIndex());
			if (optDeathCount.isPresent()) {
				deathCount = (int) optDeathCount.get().doubleValue();
				var dateCell = dateRow.getCell(cell.getColumnIndex());
				dayOfDeath = dateRow.getCell(dateCell.getColumnIndex()).toLocalDate().get();
				Trust trust = Trust.builder().code(code).name(name).region(region).build();
				DeathRecordByTrust record = DeathRecordByTrust.builder().dayOfDeath(dayOfDeath).deaths(deathCount)
						.trust(trust).recordedOn(recordedOn).source(new Ingest(source)).build();

				models.add(record);

			}

		}
		return models;
	}
}
