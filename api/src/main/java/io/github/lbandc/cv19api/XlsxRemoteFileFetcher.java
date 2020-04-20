package io.github.lbandc.cv19api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import lombok.ToString;

@ToString
class XlsxRemoteFileFetcher extends AbstractXlsxFileReader implements XlsxFileFetcher {

	XlsxRemoteFileFetcher(final LocalDate date) {

		super(date);

	}

	public InputStream fetch() {

		try {
			URL url = new URL(this.getUrl());
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
