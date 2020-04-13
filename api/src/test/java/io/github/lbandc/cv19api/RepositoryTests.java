package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback
public class RepositoryTests {

	@Autowired
	private IngestRepository ingestRepo;
	@Autowired
	private DeathRecordByTrustRepository recordRepo;
	@Autowired
	private TrustRepository trustRepo;

	@Autowired
	private FileRetriever fileRetriever;

	@Test
	@Transactional
	public void testIngestCreateAndFind() {

		Ingest ingest = new Ingest("http://localhost/1");
		Ingest persistentIngest = this.ingestRepo.save(ingest);
		assertThat(persistentIngest.getId()).isNotEmpty();
		assertThat(persistentIngest.getCreatedAt()).isNotNull();
		assertThat(persistentIngest.getUpdatedAt()).isNotNull();
		assertThat(persistentIngest.getVersion()).isEqualTo(0);

	}

	@Test
	@Transactional
	public void testTrustCreateAndFind() {

		Trust trust = Trust.builder().code("AAA").name("Local Trust").region(Region.LONDON).build();
		Trust persistentTrust = this.trustRepo.save(trust);

		assertThat(persistentTrust.getCode()).isNotEmpty();
	}

	@Test
	@Transactional
	public void testDeathByTrustRecordCreateAndFind() {

		Ingest ingest = new Ingest("http://localhost/1");
		Ingest persistentIngest = this.ingestRepo.save(ingest);

		Trust trust = Trust.builder().code("AAA").name("Local Trust").region(Region.LONDON).build();
		Trust persistentTrust = this.trustRepo.save(trust);

		DeathRecordByTrust record = DeathRecordByTrust.builder().dayOfDeath(LocalDate.now())
				.dayOfDeath(LocalDate.now().minusDays(1)).deaths(10).build();
		record.setSource(persistentIngest);
		record.setTrust(persistentTrust);
		DeathRecordByTrust persistentRecord = this.recordRepo.save(record);
		assertThat(persistentRecord.getId()).isNotEmpty();
		assertThat(persistentRecord.getCreatedAt()).isNotNull();

	}

	@Test
	@Transactional
	public void testDeathsByDayAndByTrustProjection() throws IOException {
		String filePathA = "COVID-19-daily-announced-deaths-10-April-2020.xlsx";
		File fileA = new ClassPathResource(filePathA).getFile();
		String filePathB = "COVID-19-daily-announced-deaths-9-April-2020.xlsx";
		File fileB = new ClassPathResource(filePathB).getFile();
		this.fileRetriever.fetch(LocalDate.of(2020, 4, 10), fileA);
		this.fileRetriever.fetch(LocalDate.of(2020, 4, 9), fileB);
		this.recordRepo.getByDateAndByTrust(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 10),
				LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 1)).forEach(projection -> {
					System.out.println("DayOfDeath: " + projection.getDate() + " Trust: " + projection.getTrust()
							+ " Deaths: " + projection.getDeaths());
				});
	}
}
