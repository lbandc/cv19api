package io.github.lbandc.cv19api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

public class CellTests {

	@Test
	public void testEqaulity() {

		Cell a = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "Hello");
		Cell b = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "Hello");
		Cell c = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(2)), "Hello");

		assertEquals(a, b);
		assertNotEquals(a, c);

	}

	@Test
	public void testPositions() {

		Cell centre = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "Hello");
		Cell right = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(4)), "Hello");
		Cell left = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(2)), "Hello");
		Cell above = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(2), new ColumnIndex(3)), "Hello");
		Cell below = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(4), new ColumnIndex(3)), "Hello");

		Cell distant = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(6), new ColumnIndex(3)), "Hello");
		assertTrue(right.isAdjacentTo(centre));
		assertTrue(left.isAdjacentTo(centre));
		assertTrue(above.isAdjacentTo(centre));
		assertTrue(below.isAdjacentTo(centre));
		assertFalse(centre.isAdjacentTo(centre));
		assertFalse(centre.isAdjacentTo(distant));

	}

	@Test
	public void testDateString() {

		Cell a = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "1-Mar");
		Cell b = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "1-Mar-2020");
		Cell c = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "01-Mar");

		assertEquals(a.toLocalDate().get().getYear(), 2020);
		assertEquals(b.toLocalDate().get().getYear(), 2020);
		assertEquals(c.toLocalDate().get().getYear(), 2020);
		assertEquals(b.toLocalDate().get().getMonthValue(), 3);
		assertEquals(c.toLocalDate().get().getMonthValue(), 3);
		assertEquals(b.toLocalDate().get().getDayOfMonth(), 1);
		assertEquals(c.toLocalDate().get().getDayOfMonth(), 1);

	}
}
