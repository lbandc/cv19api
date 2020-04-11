package io.github.lbandc.cv19api;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Region {
    @JsonProperty("East of England")
    EAST_OF_ENGLAND,
    @JsonProperty("London")
    LONDON,
    @JsonProperty("Midlands")
    MIDLANDS,
    @JsonProperty("North East and Yorkshire")
    NORTH_EAST_AND_YORKSHIRE,
    @JsonProperty("North West")
    NORTH_WEST,
    @JsonProperty("South East")
    SOUTH_EAST,
    @JsonProperty("South West")
    SOUTH_WEST;
}
