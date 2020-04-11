package io.github.lbandc.cv19api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Autowired
	TrustRepository trustRepository;

	@PostConstruct
	public void addTrust() {
		Trust trust = Trust.builder()
				.code("RDD")
				.name("BASILDON AND THURROCK UNIVERSITY HOSPITALS NHS FOUNDATION TRUST")
				.region(Region.EAST_OF_ENGLAND)
				.deaths(Map.of(
						LocalDate.now().minusDays(7), 30,
						LocalDate.now().minusDays(6), 34,
						LocalDate.now().minusDays(5), 38,
						LocalDate.now().minusDays(4), 46,
						LocalDate.now().minusDays(3), 68,
						LocalDate.now().minusDays(2), 102,
						LocalDate.now().minusDays(1), 174,
						LocalDate.now(), 244
				))
				.build();

		Trust trust2 = Trust.builder()
				.code("RC1")
				.name("BEDFORD HOSPITAL NHS TRUST")
				.region(Region.EAST_OF_ENGLAND)
				.deaths(Map.of(
						LocalDate.now().minusDays(7), 30,
						LocalDate.now().minusDays(6), 34,
						LocalDate.now().minusDays(5), 38,
						LocalDate.now().minusDays(4), 46,
						LocalDate.now().minusDays(3), 68,
						LocalDate.now().minusDays(2), 102,
						LocalDate.now().minusDays(1), 174,
						LocalDate.now(), 244
				))
				.build();

		trustRepository.saveAll(List.of(trust, trust2));
	}

}
