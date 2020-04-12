package io.github.lbandc.cv19api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileRetriever {

	private static String URI = "https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/";
	@Autowired
	private TrustRepository repo;

	@PostConstruct
	void onStartup() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM yyyy");
		// String[] previous = { "02", "03", "04", "05", "06", "07", "08", "09", "10" };
		String[] previous = { "10" };

		for (String day : previous) {
			LocalDate d = LocalDate.parse(day + "-" + "Apr" + " 2020", formatter);
			this.fetchFile(d);
		}
		// this.fetchTodaysFile();
	}

	@Scheduled(cron = "0 10,14,17,21 * * * *")
	public void fetchTodaysFile() {
		this.fetchFile(LocalDate.now());
	}

	public void fetchFile(LocalDate now) {

		try {
			String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
			String filePath = "COVID-19-daily-announced-deaths-" + String.valueOf(now.getDayOfMonth()) + "-"
					+ now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "-2020.xlsx";
			URL url = new URL(URI + String.valueOf(now.getYear()) + "/" + month + "/" + filePath);
			List<Trust> models = new TrustSheetParser(url).parse();
			models.forEach(trust -> {
				this.repo.save(trust);
			});
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
