package io.github.lbandc.cv19api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.context.annotation.Profile;
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

	@PostConstruct
	void onStartup() {
		// get yesterday's file
		log.info("Ingesting today's file on startup");
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
			log.info("Preparing to ingest {}", url.toString());
			Ingest ingest;
			if (!ingestRepository.existsByUrl(url.toString())) { // TODO this might need to change for today's datax
				ingest = ingestRepository.save(new Ingest(url.toString(), Instant.now()));
			} else {
				log.warn("Already ingested {}. Skipping.", url.toString());
				return;
			}

			List<Trust> models = new TrustSheetParser(url).parse();
			List<Trust> merged = models.stream().map(trust -> {
				trust = mergeIfExists(trust);
				trust.getSources().add(ingest);
				return trust;
			}).collect(Collectors.toList());

			trustRepository.saveAll(merged);
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

	private Trust mergeIfExists(Trust trust) {
		if (trustRepository.existsById(trust.getCode())) {
			Trust existing = trustRepository.findById(trust.getCode()).get();
			trust.getDeaths().forEach((k, v) -> existing.getDeaths().merge(k, v, Integer::sum));
			return existing;
		} else {
			return trust;
		}
	}
}
