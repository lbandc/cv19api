package io.github.lbandc.cv19api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RowIndexTests {

	@Test
	public void testRowIndexCannotBeNegative() {

		assertThrows(IllegalArgumentException.class, () -> {
			new RowIndex(-1);
		});

	}

	@Test
	public void testDistantRowIndexIsNotConsideredAdjacent() {

		RowIndex a = new RowIndex(0);
		RowIndex b = new RowIndex(2);

		assertFalse(a.isAdjacentTo(b));
		assertFalse(b.isAdjacentTo(a));

	}

	@Test
	public void testNextConsecutiveIndexRowIndexIsConsideredAdjacent() {

		RowIndex a = new RowIndex(0);
		RowIndex b = new RowIndex(1);

		assertTrue(a.isAdjacentTo(b));
		assertTrue(b.isAdjacentTo(a));

	}

	@Test
	public void testAbove() {

		RowIndex a = new RowIndex(2);
		RowIndex b = new RowIndex(3);
		RowIndex c = new RowIndex(4);

		assertTrue(b.isBelow(a));
		assertTrue(c.isBelow(a));
		assertTrue(c.isBelow(b));

		assertFalse(a.isBelow(b));
		assertFalse(a.isBelow(c));
		assertFalse(b.isBelow(c));

		assertFalse(a.isBelow(a));

	}

	@Test
	public void testBelow() {

		RowIndex a = new RowIndex(2);
		RowIndex b = new RowIndex(3);
		RowIndex c = new RowIndex(4);

		assertTrue(a.isAbove(b));
		assertTrue(b.isAbove(c));
		assertTrue(a.isAbove(c));

		assertFalse(b.isAbove(a));
		assertFalse(c.isAbove(a));
		assertFalse(c.isAbove(b));

		assertFalse(a.isAbove(a));

	}
}
