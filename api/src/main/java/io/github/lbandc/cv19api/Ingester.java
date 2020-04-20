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

	private final TrustRepository trustRepository;
	private final IngestRepository ingestRepository;
	private final DeathRecordByTrustRepository deathRecordRepository;

	@Transactional(rollbackOn = IOException.class)
	public void ingest(LocalDate fileDate, XlsxFileFetcher fileReader) throws IOException {

		log.info("Preparing to ingest {}", fileReader.getSource());
		if (ingestRepository.existsByUrl(fileReader.getSource())) {
			log.warn("Already ingested {}. Skipping.", fileReader.getSource());
			return;
		} else {
			this.ingestRepository.save(new Ingest(fileReader.getSource()));
		}
		List<DeathRecordByTrust> models = new TrustSheetParser(fileReader.fetch(), fileDate, fileReader.getSource())
				.parse();
		for (DeathRecordByTrust record : models) {

			// deal with duplicate trust code records data errors
			// TODO extract this out
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
			Ingest existingIngest = ingestRepository.findByUrl(fileReader.getSource());
			if (null != existingIngest) {
				record.setSource(existingIngest);
			}
			this.deathRecordRepository.save(record);
		}

	}
}
