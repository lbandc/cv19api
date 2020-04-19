package io.github.lbandc.cv19api;

import java.io.InputStream;

interface XlsxFileReader {

	InputStream fetch();

	String getSource();
}