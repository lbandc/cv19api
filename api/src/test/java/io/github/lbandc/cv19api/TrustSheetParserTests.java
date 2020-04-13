package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class TrustSheetParserTests {

	@Test
	public void testBasics() throws IOException, InvalidFormatException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy");
		for (int n = 10; n <= 10; n++) {
			String dateString = String.valueOf(n) + "-April-2020";
			var fileName = "COVID-19-daily-announced-deaths-" + dateString + ".xlsx";
			System.out.println(fileName);
			File file = new ClassPathResource(fileName).getFile();
			System.out.println("Testing File: " + file.getName());
			List<DeathRecordByTrust> models = new TrustSheetParser(file, LocalDate.parse(dateString, formatter))
					.parse();
			assertThat(models).isNotEmpty();

		}
	}

}
