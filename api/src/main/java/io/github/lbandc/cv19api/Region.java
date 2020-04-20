package io.github.lbandc.cv19api;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Region {
	@JsonProperty("East of England")
	EAST_OF_ENGLAND, @JsonProperty("London")
	LONDON, @JsonProperty("Midlands")
	MIDLANDS, @JsonProperty("North East and Yorkshire")
	NORTH_EAST_AND_YORKSHIRE, @JsonProperty("North West")
	NORTH_WEST, @JsonProperty("South East")
	SOUTH_EAST, @JsonProperty("South West")
	SOUTH_WEST;

	static Region forName(String name) {
		Region region;
		switch (name.toLowerCase().trim()) {
		case "east of england":
			region = EAST_OF_ENGLAND;
			break;

		case "london":
			region = LONDON;
			break;

		case "midlands":
			region = MIDLANDS;
			break;

		case "north east and yorkshire":
			region = NORTH_EAST_AND_YORKSHIRE;
			break;

		case "north west":
			region = NORTH_WEST;
			break;

		case "south east":
			region = SOUTH_EAST;
			break;
		case "south west":
			region = SOUTH_WEST;
			break;

		default:
			throw new IllegalArgumentException("invalid Region " + name);
		}
		return region;
	}
}
