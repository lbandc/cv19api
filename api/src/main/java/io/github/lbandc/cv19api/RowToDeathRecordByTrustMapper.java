package io.github.lbandc.cv19api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class RowToDeathRecordByTrustMapper {

	private final DataMatchingStrategy dataFinder;
	private final Sheet sheet;

	public RowToDeathRecordByTrustMapper(final DataMatchingStrategy dataFinder, final Sheet sheet) {

		this.dataFinder = dataFinder;
		this.sheet = sheet;
	}

	List<DeathRecordByTrust> map(final Row row, String source, LocalDate recordedOn) {

		Cell firstDateCell;
		try {
			firstDateCell = this.dataFinder.getFirstDateCell();
		} catch (IOException e) {
			throw new RuntimeException();
		}

		Region region = null;
		String code = null;
		String name = null;
		Integer deathCount = null;
		LocalDate dayOfDeath = null;
		List<DeathRecordByTrust> models = new ArrayList<>();
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

			var optDeathCount = dataFinder.getDeathCount(cell, this.sheet.getRow(firstDateCell.getRowIndex()));

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
