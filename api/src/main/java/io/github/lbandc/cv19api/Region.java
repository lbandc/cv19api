package io.github.lbandc.cv19api;

public enum Region {
	EAST_OF_ENGLAND("East Of England"), LONDON("London"), MIDLANDS("Midlands"),
	NORTH_EAST_AND_YORKSHIRE("North East and Yorkshire"), NORTH_WEST("North West"), SOUTH_EAST("South East"),
	SOUTH_WEST("South West");

	private final String name;

	Region(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	static Region forName(String name) {
		Region region;
		switch (name.toLowerCase().trim()) {
		case "east of england":
			region = EAST_OF_ENGLAND;
			break;

		case "london":
			region = EAST_OF_ENGLAND;
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
