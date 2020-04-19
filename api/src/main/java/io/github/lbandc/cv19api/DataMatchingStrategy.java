package io.github.lbandc.cv19api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

class DataMatchingStrategy {

	private final List<Row> rows;
	private final SortedSet<Row> sortedSet;

	DataMatchingStrategy(List<Row> rows) {
		this.rows = Collections.unmodifiableList(rows);
		this.sortedSet = new TreeSet<Row>(rows);
	}

	Row getRow(int index) {
		return this.rows.get(index);
	}

	Cell getCell(int rowIndex, int colIndex) {
		return this.getRow(rowIndex).getCell(new ColumnIndex(colIndex));
	}

	Cell getFirstRegionCell() throws IOException {
		for (Row row : this.sortedSet) {
			for (Cell cell : row) {
				if (this.getRegion(cell).isPresent()) {
					return cell;
				}
			}
		}
		throw new IOException("Data Sheet does not contain Region data");
	}

	RowIndex getLastSignificantRowIndex(RowIndex startingIndex) {
		int index = startingIndex.getValue();
		for (Row row : this.sortedSet) {
			for (Cell cell : row) {
				if (this.getRegion(cell).isPresent()) {
					index++;

				}
			}
		}
		return new RowIndex(index);
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

	Optional<Double> getDeathCount(Cell cell) {
		if (null == cell) {
			return Optional.empty();
		}
		try {
			return Optional.of(Double.valueOf(cell.toString()));
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
		for (Row row : this.sortedSet) {
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
