package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AgeSheetParserTests {

	@Test
	public void testBasics() throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy");
		for (int n = 2; n <= 10; n++) {
			String dateString = String.valueOf(n) + "-April-2020";
			var fileName = "COVID-19-daily-announced-deaths-" + dateString + ".xlsx";
			var fileReader = new XlsxLocalFileFetcher(LocalDate.parse(dateString, formatter));
			Sheet sheet = new XlsxSheetMapper(fileReader.fetch(), Ingester.AGE_SHEET_NAME).getSheet();
			List<DeathRecordByAge> models = new AgeSheetParser(sheet, fileReader.getDate(), fileReader.getSource())
					.parse();

			assertThat(models).isNotEmpty();

		}
	}

}
