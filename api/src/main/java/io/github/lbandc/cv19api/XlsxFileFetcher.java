package io.github.lbandc.cv19api;

import java.io.InputStream;

interface XlsxFileFetcher {

	InputStream fetch();

	String getSource();

	// TODO getFileDate();
}