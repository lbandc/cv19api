package io.github.lbandc.cv19api;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

interface DataMatchingStrategy {

	Optional<RowIndex> getSignificantRowIndex(Function<Sheet, Optional<RowIndex>> func) throws IOException;

	Cell getFirstDateCell() throws IOException;
}