package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.javatuples.Pair;

import lombok.NonNull;
import lombok.Value;

@Value
final class Cell {

	@NonNull
	private final Pair<RowIndex, ColumnIndex> position;
	@NonNull
	private final String value;

	private boolean isDate() {

		// Matches only a limited number of formats
		return this.value.toLowerCase().trim().matches("^[0-9]{1,2}-[A-Za-z]{3}(-[0-9]{4})?$");

	}

	RowIndex getRowIndex() {
		return this.getPosition().getValue0();
	}

	ColumnIndex getColumnIndex() {
		return this.getPosition().getValue1();
	}

	boolean isInSamePositionAs(Cell other) {
		return this.getPosition().equals(other.getPosition());
	}

	boolean isInSameColumnAs(Cell other) {
		return this.getColumnIndex().equals(other.getColumnIndex());
	}

	boolean isInSameRowAs(Cell other) {
		return this.getRowIndex().equals(other.getRowIndex());
	}

	boolean isAdjacentTo(Cell other) {

		if (null == other) {
			return false;
		}
		return other.getPosition().getValue0().isAdjacentTo(this.getPosition().getValue0())

				|| other.getPosition().getValue1().isAdjacentTo(this.getPosition().getValue1());
	}

	boolean isToTheRightOf(Cell other) {
		if (null == other) {
			return false;
		}
		return other.getColumnIndex().isToTheLeftOf(this.getColumnIndex());
	}

	boolean isBelow(Cell other) {
		return this.getRowIndex().isBelow(other.getRowIndex());
	}

	private boolean isNumeric() {

		try {
			Double.parseDouble(this.toString());
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public Optional<Double> toDouble() {
		if (!this.isNumeric()) {
			return Optional.empty();
		}

		return Optional.of(Double.parseDouble(this.toString()));

	}

	public Optional<LocalDate> toLocalDate() {

		if (!this.isDate()) {
			return Optional.empty();
		}

		String dateString = this.toString();
		if (!dateString.matches("^.*(-[0-9]{4})$")) {
			dateString = dateString + "-2020";
		}
		if (dateString.matches("^[1-9]{1}-.*$")) {
			dateString = "0" + dateString;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		return Optional.of(LocalDate.parse(dateString, formatter));
	}

	@Override
	public String toString() {
		return this.value.trim();
	}

}
