package io.github.lbandc.cv19api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ColumnIndexTests {

	@Test
	public void testColumnIndexCannotBeNegative() {

		assertThrows(IllegalArgumentException.class, () -> {
			new ColumnIndex(-1);
		});

	}

	@Test
	public void testDistantColumnIndexIsNotConsideredAdjacent() {

		ColumnIndex a = new ColumnIndex(0);
		ColumnIndex b = new ColumnIndex(2);

		assertFalse(a.isAdjacentTo(b));
		assertFalse(b.isAdjacentTo(a));

	}

	@Test
	public void testNextConsecutiveIndexColumnIndexIsConsideredAdjacent() {

		ColumnIndex a = new ColumnIndex(0);
		ColumnIndex b = new ColumnIndex(1);

		assertTrue(a.isAdjacentTo(b));
		assertTrue(b.isAdjacentTo(a));

	}

	@Test
	public void testRight() {

		ColumnIndex a = new ColumnIndex(2);
		ColumnIndex b = new ColumnIndex(3);
		ColumnIndex c = new ColumnIndex(4);

		assertTrue(b.isToTheRightOf(a));
		assertTrue(c.isToTheRightOf(a));
		assertTrue(c.isToTheRightOf(b));

		assertFalse(a.isToTheRightOf(b));
		assertFalse(a.isToTheRightOf(c));
		assertFalse(b.isToTheRightOf(c));

		assertFalse(a.isToTheRightOf(a));

	}

	@Test
	public void testLeft() {

		ColumnIndex a = new ColumnIndex(2);
		ColumnIndex b = new ColumnIndex(3);
		ColumnIndex c = new ColumnIndex(4);

		assertTrue(a.isToTheLeftOf(b));
		assertTrue(b.isToTheLeftOf(c));
		assertTrue(a.isToTheLeftOf(c));

		assertFalse(b.isToTheLeftOf(a));
		assertFalse(c.isToTheLeftOf(a));
		assertFalse(c.isToTheLeftOf(b));

		assertFalse(a.isToTheLeftOf(a));

	}
}
