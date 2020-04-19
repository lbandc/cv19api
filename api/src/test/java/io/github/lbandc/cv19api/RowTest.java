package io.github.lbandc.cv19api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

public class RowTest {

	@Test
	public void testIterationIsInCellOrder() {

		Cell a = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(0)), "Hello");
		Cell b = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(1)), "Hello");
		Cell c = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(2)), "Hello");
		Cell d = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(3)), "Hello");
		Cell e = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(4)), "Hello");

		List<Cell> cells = new ArrayList<Cell>();
		cells.add(d);
		cells.add(a);
		cells.add(e);
		cells.add(c);
		cells.add(b);

		Row row = new Row(new RowIndex(3), cells);
		row.forEach(cell -> System.out.println(cell.getPosition()));
		Iterator<Cell> it = row.iterator();

		assertEquals(it.next(), a);
		assertEquals(it.next(), b);
		assertEquals(it.next(), c);
		assertEquals(it.next(), d);
		assertEquals(it.next(), e);

	}

	@Test
	public void testRowIterationOrder() {

		Cell a = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(0), new ColumnIndex(0)), "Hello");
		Cell b = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(2), new ColumnIndex(0)), "Hello");
		Cell c = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(4), new ColumnIndex(0)), "Hello");
		Cell d = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(3), new ColumnIndex(0)), "Hello");
		Cell e = new Cell(new Pair<RowIndex, ColumnIndex>(new RowIndex(1), new ColumnIndex(0)), "Hello");

		Row rowA = new Row(new RowIndex(0), List.of(a));
		Row rowB = new Row(new RowIndex(2), List.of(b));
		Row rowC = new Row(new RowIndex(4), List.of(c));
		Row rowD = new Row(new RowIndex(3), List.of(d));
		Row rowE = new Row(new RowIndex(1), List.of(e));

		TreeSet<Row> sortedSet = new TreeSet<>();
		sortedSet.add(rowA);
		sortedSet.add(rowB);
		sortedSet.add(rowC);
		sortedSet.add(rowD);
		sortedSet.add(rowE);

		var it = sortedSet.iterator();
		assertEquals(it.next(), rowA);
		assertEquals(it.next(), rowE);
		assertEquals(it.next(), rowB);
		assertEquals(it.next(), rowD);
		assertEquals(it.next(), rowC);

	}

}
