package io.github.lbandc.cv19api;

import lombok.Value;

@Value
class RowIndex {

	private final int value;

	RowIndex(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value must be a positive integer");
		}
		this.value = value;
	}

	boolean isAdjacentTo(RowIndex other) {
		return this.value - other.value == 1 || this.value - other.value == -1;
	}

	boolean isAbove(RowIndex other) {
		return other != null && (other.getValue() > this.getValue());
	}

	boolean isBelow(RowIndex other) {
		return other != null && (other.getValue() < this.getValue());
	}

}
