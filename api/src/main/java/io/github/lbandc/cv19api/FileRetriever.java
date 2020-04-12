package io.github.lbandc.cv19api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
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

		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM yyyy");

		// get yesterday's file
		this.fetchFile(LocalDate.now().minusDays(1));
		this.repo.findAll().forEach(t -> {
			System.out.println(t.toString());
		});

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
				if (repo.existsById(trust.getCode())) {
					Trust existing = repo.findById(trust.getCode()).get();
					trust.getDeaths().putAll(existing.getDeaths());
				}
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
