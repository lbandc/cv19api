package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class TrustSheetParserTests {

	@Test
	public void testBasics() throws IOException, InvalidFormatException {

		for (int n = 10; n <= 10; n++) {
			File file = new ClassPathResource(
					"COVID-19-daily-announced-deaths-" + String.valueOf(n) + "-April-2020.xlsx").getFile();
			System.out.println("Testing File: " + file.getName());
			List<Trust> models = new TrustSheetParser(file).parse();
			assertThat(models).isNotEmpty();
			for (Trust t : models) {
				// System.out.println(t);
			}
		}
	}

}
