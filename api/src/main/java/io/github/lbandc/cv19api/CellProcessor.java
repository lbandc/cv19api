package io.github.lbandc.cv19api;

class CellProcessor {

//	@Data
//	static class TrustResult<T> {
//		T value;
//	}
//
//	private final CellProcessor cell;
//	private final ExcelDataFinderStrategy dataFinder;
//
//	CellProcessor(org.apache.poi.ss.usermodel.Cell cell, int firstColumnIndex, ExcelDataFinderStrategy dataFinder) {
//		this.cell = cell;
//		this.dataFinder = dataFinder;
//		if (this.cell.getColumnIndex() < firstColumnIndex) {
//			throw new IllegalArgumentException("Not a valid Cell");
//		}
//	}
//
//	TrustResult process() {
//
//		if (this.dataFinder.isRegionCell(cell.getRowIndex(), cell.getColumnIndex())) {
//			region = Region.forName(cell.getStringCellValue());
//
//		} else if (dataFinder.isCodeCell(cell)) {
//			code = cell.getStringCellValue();
//		} else if (dataFinder.isTrustNameCell(cell)) {
//			name = cell.getStringCellValue();
//		}
//
//		else {
//			CellProcessor dateCell = dataFinder.getRow(dateRow).getCell(cell.getColumnIndex());
//
//			if (!dataFinder.isDateCell(dateCell)) {
//				continue;
//			} else {
//				// System.out.println("DEATHS " + " " + region + " " + code + " " + name + " " +
//				// cell.toString());
//				String dateString = dateCell.toString().trim();
//				if (!dateString.endsWith("-2020")) {
//					dateString = dateString + "-2020";
//				}
//
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
//				dayOfDeath = LocalDate.parse(dateString, formatter);
//				deathCount = (int) cell.getNumericCellValue();
//				if (code == null || name == null || region == null || dayOfDeath == null || deathCount == null) {
//					throw new IOException("Invalid data");
//				}
//
//				Trust trust = Trust.builder().code(code).name(name).region(region).build();
//				DeathRecordByTrust record = DeathRecordByTrust.builder().dayOfDeath(dayOfDeath).deaths(deathCount)
//						.trust(trust).recordedOn(this.recordedOn).source(new Ingest(this.source)).build();
//				models.add(record);
//
//			}
//		}
//
//	}
}
