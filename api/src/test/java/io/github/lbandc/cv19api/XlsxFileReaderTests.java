package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class XlsxFileReaderTests {

	@Test
	public void testExistingLocalSampleFileIsRead() {
		XlsxRemoteFileReader fileReader = new XlsxRemoteFileReader(LocalDate.of(2020, 4, 2));
		assertThat(fileReader.fetch()).isInstanceOf(InputStream.class);
		assertThat(fileReader.getFilePath()).isEqualTo("COVID-19-daily-announced-deaths-2-April-2020.xlsx");
	}
}
