package io.github.lbandc.cv19api;

public enum Region {
    EAST_OF_ENGLAND("East Of England"),
    LONDON("London"),
    MIDLANDS("Midlands"),
    NORTH_EAST_AND_YORKSHIRE("North East and Yorkshire"),
    NORTH_WEST("North West"),
    SOUTH_EAST("South East"),
    SOUTH_WEST("South West");

    private final String name;

    Region(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
