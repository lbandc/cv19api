package io.github.lbandc.cv19api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Component;

@Component
public class FileRetriever {

	public void fetchTodaysFile() {

		try {
			var now = LocalDate.now();
			URL url = new URL("https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/"
					+ String.valueOf(now.getYear()) + "/" + now.getMonth());
			var models = new TrustSheetParser(url).parse();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
