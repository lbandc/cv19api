package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class RegionSheetParserTests {

	@Test
	public void testBasics() throws IOException, InvalidFormatException {

		File file = new ClassPathResource("COVID-19-daily-announced-deaths-2-April-2020.xlsx").getFile();
		List<RegionSheetParser.Model> models = new RegionSheetParser(file).parseRegionData();
		assertThat(models).isNotEmpty();
		assertThat(models.get(0).region).isEqualTo("East Of England");
		assertThat(models.get(models.size() - 1).region).isEqualTo("South West");
		assertThat(models.get(0).data.get(0).keySet().contains("01-Mar")).isTrue();
		assertThat(models.get(models.size() - 1).data.get(models.get(models.size() - 1).data.size() - 1).keySet()
				.contains("01-Apr")).isTrue();
	}

}
