package io.github.lbandc.cv19api;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class Ingester {

	public static String TRUST_SHEET_NAME = "by trust";
	public static String AGE_SHEET_NAME = "by age";
	private final TrustRepository trustRepository;
	private final IngestRepository ingestRepository;
	private final DeathRecordByTrustRepository deathRecordRepository;
	private final DeathRecordByAgeRepository ageRepository;

	@Transactional(rollbackOn = IOException.class)
	public void ingest(LocalDate fileDate, XlsxFileFetcher fileFetcher) throws IOException {

		log.info("Preparing to ingest {}", fileFetcher.getSource());
		if (ingestRepository.existsByUrl(fileFetcher.getSource())) {
			log.warn("Already ingested {}. Skipping.", fileFetcher.getSource());
			return;
		} else {
			this.ingestRepository.save(new Ingest(fileFetcher.getSource()));
		}

		Sheet trustSheet = new XlsxSheetMapper(fileFetcher.fetch(), "by trust").getSheet();
		Sheet ageSheet = new XlsxSheetMapper(fileFetcher.fetch(), "by age").getSheet();

		List<DeathRecordByTrust> trustModels = new TrustSheetParser(trustSheet, fileDate, fileFetcher.getSource())
				.parse();
		List<DeathRecordByAge> ageModels = new AgeSheetParser(ageSheet, fileDate, fileFetcher.getSource()).parse();
		for (DeathRecordByTrust record : trustModels) {

			this.ingestTrustRecord(record, fileFetcher.getSource());
		}
		for (DeathRecordByAge record : ageModels) {

			this.ingestAgeRecord(record, fileFetcher.getSource());
		}

	}

	private void ingestTrustRecord(DeathRecordByTrust record, String source) {

		DeathRecordByTrust existingRecord = this.deathRecordRepository.findByTrustAndRecordedOnAndDayOfDeath(
				record.getTrust(), record.getRecordedOn(), record.getDayOfDeath());
		if (existingRecord != null) {
			log.warn("This record already exists {}", record);
			existingRecord.setDeaths(existingRecord.getDeaths() + record.getDeaths());
			this.deathRecordRepository.save(existingRecord);
			return;
		}
		if (trustRepository.existsById(record.getTrust().getCode())) {
			Trust existingTrust = trustRepository.findById(record.getTrust().getCode()).get();
			record.setTrust(existingTrust);
		} else {
			record.setTrust(trustRepository.save(record.getTrust()));
		}
		Ingest existingIngest = ingestRepository.findByUrl(source);

		record.setSource(existingIngest);

		this.deathRecordRepository.save(record);
	}

	private void ingestAgeRecord(DeathRecordByAge record, String source) {

		DeathRecordByAge existingRecord = this.ageRepository.findByAgeRangeAndRecordedOnAndDayOfDeath(
				record.getAgeRange(), record.getRecordedOn(), record.getDayOfDeath());
		if (existingRecord != null) {
			log.warn("This record already exists {}", record);
			return;
		}
		Ingest existingIngest = ingestRepository.findByUrl(source);

		record.setSource(existingIngest);

		this.ageRepository.save(record);
	}
}
