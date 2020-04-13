package io.github.lbandc.cv19api;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Profile("!testData")
@Slf4j
public class FileRetriever {

	private static String URI = "https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/";

	private final TrustRepository trustRepository;
	private final IngestRepository ingestRepository;
	private final DeathRecordByTrustRepository deathRecordRepository;

	@Scheduled(cron = "0 0,17,21,23 * * * *")
	public void fetchTodaysFile() {
		this.fetch(LocalDate.now(), null);
	}

	@Async
	public void fetchAsync(LocalDate now, File file) {
		fetch(now, file);
	}

	@Transactional
	public void fetch(LocalDate now, File file) {

		try {
			String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
			String filePath = "COVID-19-daily-announced-deaths-" + now.getDayOfMonth() + "-"
					+ now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "-2020.xlsx";
			URL url = new URL(URI + now.getYear() + "/" + month + "/" + filePath);
			log.info("Preparing to ingest {}", url.toString());
			if (!ingestRepository.existsByUrl(url.toString())) { // TODO this might need to change for today's datax
				ingestRepository.save(new Ingest(url.toString()));
			} else {
				log.warn("Already ingested {}. Skipping.", url.toString());
				return;
			}
			List<DeathRecordByTrust> models;
			if (file != null) {
				models = new TrustSheetParser(file, now).parse();
			} else {
				models = new TrustSheetParser(url, now).parse();
			}

			for (DeathRecordByTrust record : models) {

				// deal with duplicate trust code records data errors
				DeathRecordByTrust existingRecord = this.deathRecordRepository.findByTrustAndRecordedOnAndDayOfDeath(
						record.getTrust(), record.getRecordedOn(), record.getDayOfDeath());
				if (existingRecord != null) {
					log.warn("This record already exists {}", record);
					existingRecord.setDeaths(existingRecord.getDeaths() + record.getDeaths());
					this.deathRecordRepository.save(existingRecord);
					continue;
				}
				if (trustRepository.existsById(record.getTrust().getCode())) {
					Trust existingTrust = trustRepository.findById(record.getTrust().getCode()).get();
					record.setTrust(existingTrust);
				} else {
					record.setTrust(trustRepository.save(record.getTrust()));
				}
				Ingest existingIngest = ingestRepository.findByUrl(url.toString());
				record.setSource(existingIngest);
				this.deathRecordRepository.save(record);

			}
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
