package io.github.lbandc.cv19api;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Profile("!testData")
@Slf4j
public class Ingestor {
//
//	private final TrustRepository trustRepository;
//	private final IngestRepository ingestRepository;
//	private final DeathRecordByTrustRepository deathRecordRepository;
//
//	@Transactional
//	public void ingest(LocalDate now, XlsxFileReader fileReader) {
//
//		log.info("Preparing to ingest {}", fileReader.getSource());
//		if (!ingestRepository.existsByUrl(fileReader.getSource())) { // TODO this might need to change for today's
//																		// datax
//			ingestRepository.save(new Ingest(fileReader.getSource()));
//		} else {
//			log.warn("Already ingested {}. Skipping.", fileReader.getSource());
//			return;
//		}
//		List<DeathRecordByTrust> models;
//		if (file != null) {
//			models = new TrustSheetParser(file, now).parse();
//		} else {
//			models = new TrustSheetParser(url, now).parse();
//		}
//
//		for (DeathRecordByTrust record : models) {
//
//			// deal with duplicate trust code records data errors
//			DeathRecordByTrust existingRecord = this.deathRecordRepository.findByTrustAndRecordedOnAndDayOfDeath(
//					record.getTrust(), record.getRecordedOn(), record.getDayOfDeath());
//			if (existingRecord != null) {
//				log.warn("This record already exists {}", record);
//				existingRecord.setDeaths(existingRecord.getDeaths() + record.getDeaths());
//				this.deathRecordRepository.save(existingRecord);
//				continue;
//			}
//			if (trustRepository.existsById(record.getTrust().getCode())) {
//				Trust existingTrust = trustRepository.findById(record.getTrust().getCode()).get();
//				record.setTrust(existingTrust);
//			} else {
//				record.setTrust(trustRepository.save(record.getTrust()));
//			}
//			Ingest existingIngest = ingestRepository.findByUrl(url.toString());
//			record.setSource(existingIngest);
//			this.deathRecordRepository.save(record);
//
//		}
//	}
}
