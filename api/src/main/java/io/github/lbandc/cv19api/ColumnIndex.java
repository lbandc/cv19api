package io.github.lbandc.cv19api;

import lombok.Value;

@Value
final class ColumnIndex {

	private final int value;

	public ColumnIndex(int value) {
		if (value < 0) {
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	boolean isAdjacentTo(ColumnIndex other) {
		return this.value - other.value == 1 || this.value - other.value == -1;
	}

	boolean isToTheLeftOf(ColumnIndex other) {
		return other != null && (other.getValue() > this.getValue());
	}

	boolean isToTheRightOf(ColumnIndex other) {
		return other != null && (other.getValue() < this.getValue());
	}

}
