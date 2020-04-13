package io.github.lbandc.cv19api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// xHrO7Mc5Pgm1MFpj
@Component
@AllArgsConstructor
@Profile("!testData")
@Slf4j
public class FileRetriever {

	private static String URI = "https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/";

	private final TrustRepository trustRepository;
	private final IngestRepository ingestRepository;
	private final DeathRecordByTrustRepository deathRepository;

	@PostConstruct
	void onStartup() {
		// get yesterday's file
		log.info("Injesting today's file on startup");
		this.fetchFile(LocalDate.now().minusDays(1));
	}

	@Scheduled(cron = "0 0,10,14,17,21 * * * *")
	public void fetchTodaysFile() {
		this.fetchFile(LocalDate.now());
	}

	@Transactional
	public void fetchFile(LocalDate now) {

		try {
			String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
			String filePath = "COVID-19-daily-announced-deaths-" + now.getDayOfMonth() + "-"
					+ now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "-2020.xlsx";
			URL url = new URL(URI + now.getYear() + "/" + month + "/" + filePath);
			System.out.println(url.toString());
			if (!ingestRepository.existsByUrl(url.toString())) { // TODO this might need to change for today's datax
				ingestRepository.save(new Ingest(url.toString()));
			} else {
				log.warn("Already ingested {}. Skipping.", url.toString());
				return;
			}

			List<DeathRecordByTrust> models = new TrustSheetParser(url, now).parse();
			// TODO deal with duplicate trust code records
			for (DeathRecordByTrust record : models) {
				if (trustRepository.existsById(record.getTrust().getCode())) {
					Trust existingTrust = trustRepository.findById(record.getTrust().getCode()).get();
					record.setTrust(existingTrust);
				} else {
					record.setTrust(trustRepository.save(record.getTrust()));
				}
				Ingest existingIngest = ingestRepository.findByUrl(url.toString());
				record.setSource(existingIngest);

			}

			deathRepository.saveAll(models);
			deathRepository.findAll().forEach(r -> {
				System.out.println(r.toString());
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
