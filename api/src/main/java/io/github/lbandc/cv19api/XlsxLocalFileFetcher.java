package io.github.lbandc.cv19api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

import org.springframework.core.io.ClassPathResource;

import lombok.ToString;

@ToString
class XlsxLocalFileFetcher extends AbstractXlsxFileReader implements XlsxFileFetcher {

	XlsxLocalFileFetcher(final LocalDate date) {
		super(date);

	}

	public InputStream fetch() {

		try {
			return new FileInputStream(new ClassPathResource(this.getFilePath()).getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public String getSource() {
		return this.getFilePath();
	}

}
