package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

abstract class AbstractXlsxFileReader {

	private static String BASE_URL = "https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/";
	private static String BASE_FILE_PATH = "COVID-19-daily-announced-deaths-";
	private final LocalDate date;
	private final String numericMonth;
	private final String numericYear;

	protected AbstractXlsxFileReader(final LocalDate date) {
		this.date = date;

		this.numericMonth = date.getMonthValue() < 10 ? "0" + date.getMonthValue()
				: String.valueOf(date.getMonthValue());

		this.numericYear = String.valueOf(date.getYear());

	}

	String getFilePath() {
		return BASE_FILE_PATH + date.getDayOfMonth() + "-"
				+ date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "-" + numericYear + ".xlsx";
	}

	String getUrl() {
		return BASE_URL + date.getYear() + "/" + numericMonth + "/" + this.getFilePath();
	}

	LocalDate getDate() {
		return this.date;
	}

	protected abstract String getSource();

}