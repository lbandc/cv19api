package io.github.lbandc.cv19api;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

class GeneralDataMatchingStrategy implements DataMatchingStrategy {

	private final Sheet sheet;

	GeneralDataMatchingStrategy(Sheet sheet) {
		this.sheet = sheet;
	}

	public Optional<RowIndex> getSignificantRowIndex(Function<Sheet, Optional<RowIndex>> func) throws IOException {
		try {
			return func.apply(this.sheet);
		} catch (Exception e) {
			throw new IOException("Data Sheet does not contain significant data", e);
		}

	}

	public Cell getFirstDateCell() throws IOException {
		for (Row row : this.sheet) {
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
