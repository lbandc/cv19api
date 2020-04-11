package io.github.lbandc.cv19api;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.core.io.ClassPathResource;

@JsonTest
public class XlsDocumentTests {

	@Test
	public void testBasics() throws IOException, InvalidFormatException {

		File file = new ClassPathResource("COVID-19-daily-announced-deaths-2-April-2020.xlsx").getFile();
		new XlsDocument().processFile(file);
	}

}
