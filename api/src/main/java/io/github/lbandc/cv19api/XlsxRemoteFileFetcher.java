package io.github.lbandc.cv19api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
class XlsxRemoteFileFetcher extends AbstractXlsxFileReader implements XlsxFileFetcher {

	XlsxRemoteFileFetcher(final LocalDate date) {

		super(date);

	}

	public InputStream fetch() {

		try {
			URL url = new URL(this.getUrl());
			this.log.info("Fetching URL " + url.toString());
			return new BufferedInputStream(url.openStream());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getSource() {
		return this.getUrl();
	}

}
