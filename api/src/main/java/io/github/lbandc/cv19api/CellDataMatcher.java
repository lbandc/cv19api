package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Optional;

final class CellDataMatcher {

	static Optional<Region> getRegion(Cell cell) {

		try {
			return Optional.of(Region.forName(cell.toString().trim()));

		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	static Optional<String> getCode(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		return cell.toString().trim().matches("^([0-9A-Z]{3,6})$") ? Optional.of(cell.toString()) : Optional.empty();

	}

	static Optional<String> getTrustName(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		return (cell.toString().trim().matches("^[A-Z]+.*$") && cell.toString().trim().length() > 10)
				? Optional.of(cell.toString())
				: Optional.empty();

	}

	static Optional<Double> getDeathCount(Cell cell, Row dateRow) {

		try {
			if (CellDataMatcher.getDate(dateRow.getCell(cell.getColumnIndex())).isPresent()) {
				Double val = (cell.toString() != null && cell.toString().length() > 0) ? Double.valueOf(cell.toString())
						: 0.0;
				return Optional.of(val);
			} else {
				return Optional.empty();
			}

		} catch (Exception e) {
			return Optional.empty();
		}
	}

	static Optional<LocalDate> getDate(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		return cell.toLocalDate();

	}

	static Optional<AgeRange> getAgeRange(Cell cell) {

		return AgeRange.fromString(cell.toString().trim());

	}

}
