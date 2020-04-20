package io.github.lbandc.cv19api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

class DataMatchingStrategy {

	private final Sheet sheet;

	DataMatchingStrategy(Sheet sheet) {
		this.sheet = sheet;
	}

	Cell getFirstRegionCell() throws IOException {
		for (Row row : this.sheet) {
			for (Cell cell : row) {
				if (this.getRegion(cell).isPresent()) {
					return cell;
				}
			}
		}
		throw new IOException("Data Sheet does not contain Region data");
	}

	RowIndex getLastSignificantRowIndex(RowIndex startingIndex) {
		int lastRow = startingIndex.getValue();
		for (Row row : this.sheet) {
			for (Cell cell : row) {
				if (this.getRegion(cell).isPresent()) {
					lastRow = row.getIndexAsInt();

				}
			}
		}
		return new RowIndex(lastRow);
	}

	Optional<Region> getRegion(Cell cell) {

		try {
			return Optional.of(Region.forName(cell.toString().trim()));

		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	Optional<String> getCode(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		return cell.toString().trim().matches("^([0-9A-Z]{3,6})$") ? Optional.of(cell.toString()) : Optional.empty();

	}

	Optional<String> getTrustName(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		return (cell.toString().trim().matches("^[A-Z]+.*$") && cell.toString().trim().length() > 10)
				? Optional.of(cell.toString())
				: Optional.empty();

	}

	Optional<Double> getDeathCount(Cell cell, Row dateRow) {

		try {
			if (this.getDate(dateRow.getCell(cell.getColumnIndex())).isPresent()) {
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

	Optional<LocalDate> getDate(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		return cell.toString().toLowerCase().trim().matches("^[0-9]{2}-[A-Za-z]{3}(-2020)?$") ? cell.toLocalDate()
				: Optional.empty();

	}

	Cell getFirstDateCell() throws IOException {
		for (Row row : this.sheet) {
			if (row.getRowIndex().getValue() < 11) {
				continue;
			}
			for (Cell cell : row) {
				if (cell.toLocalDate().isPresent()) {
					return cell;
				}
			}
		}
		throw new IOException("Invalid Data Sheet");
	}

}
