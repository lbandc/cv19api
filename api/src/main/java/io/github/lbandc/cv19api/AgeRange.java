package io.github.lbandc.cv19api;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

enum AgeRange {

	@JsonProperty("0-19")
	ZERO_TO_NINETEEN(0, 19), @JsonProperty("20-39")
	TWENTY_TO_THIRTY_NINE(20, 39), @JsonProperty("40-59")
	FOURTY_TO_FIFTY_NINE(40, 59), @JsonProperty("60-79")
	SIXTY_TO_SEVENTY_NINE(60, 79), @JsonProperty("80+")
	EIGHTY_PLUS(80, null), @JsonProperty("TBC")
	TBC(null, null);

	private final Integer start;
	private final Integer end;

	private AgeRange(Integer start, Integer end) {
		this.start = start;
		this.end = end;
		if (this.start == null && this.end != null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		if (this.start != null && this.end != null) {
			return String.valueOf(this.start) + "-" + String.valueOf(this.end);
		}
		if (this.start != null && this.end == null) {
			return String.valueOf(this.start) + '+';
		}
		if (this.start == null && this.end == null) {
			return "TBC";
		}
		throw new RuntimeException();
	}

	static Optional<AgeRange> fromString(String value) {
		List<AgeRange> list = EnumSet.allOf(AgeRange.class).stream().filter(range -> range.toString().equals(value))
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(list.get(0));

	}
}
