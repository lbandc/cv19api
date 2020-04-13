package io.github.lbandc.cv19api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RepositoryTests {

	@Autowired
	private IngestRepository ingestRepo;
	@Autowired
	private DeathRecordByTrustRepository recordRepo;
	@Autowired
	private TrustRepository trustRepo;

	@Test
	public void testIngestCreateAndFind() {

		Ingest ingest = new Ingest("http://localhost/1");
		Ingest persistentIngest = this.ingestRepo.save(ingest);
		assertThat(persistentIngest.getId()).isNotEmpty();
		assertThat(persistentIngest.getCreatedAt()).isNotNull();
		assertThat(persistentIngest.getUpdatedAt()).isNotNull();
		assertThat(persistentIngest.getVersion()).isEqualTo(0);

	}

	@Test
	public void testTrustCreateAndFind() {

		Trust trust = Trust.builder().code("AAA").name("Local Trust").region(Region.LONDON).build();
		Trust persistentTrust = this.trustRepo.save(trust);

		assertThat(persistentTrust.getCode()).isNotEmpty();
	}

	@Test
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
}
