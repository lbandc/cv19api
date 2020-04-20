package io.github.lbandc.cv19api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AgeRangeTests {

	@Test
	public void testFromString() {

		String a = "0-19";
		String b = "20-39";
		String c = "40-59";
		String d = "60-79";
		String e = "80+";
		String f = "TBC";

		assertEquals(AgeRange.fromString(a).get(), AgeRange.ZERO_TO_NINETEEN);
		assertEquals(AgeRange.fromString(b).get(), AgeRange.TWENTY_TO_THIRTY_NINE);
		assertEquals(AgeRange.fromString(c).get(), AgeRange.FOURTY_TO_FIFTY_NINE);
		assertEquals(AgeRange.fromString(d).get(), AgeRange.SIXTY_TO_SEVENTY_NINE);
		assertEquals(AgeRange.fromString(e).get(), AgeRange.EIGHTY_PLUS);
		assertEquals(AgeRange.fromString(f).get(), AgeRange.TBC);
		assertTrue(AgeRange.fromString("FooBar").isEmpty());

	}
}
