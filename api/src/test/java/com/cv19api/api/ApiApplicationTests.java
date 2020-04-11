package com.cv19api.api;

import io.github.lbandc.cv19api.ApiApplication;
import io.github.lbandc.cv19api.Region;
import io.github.lbandc.cv19api.Trust;
import io.github.lbandc.cv19api.TrustRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ApiApplication.class)
@AutoConfigureMockMvc
class ApiApplicationTests {

	@Autowired
	private TrustRepository trustRepository;

	@Autowired
	private MockMvc mockMvc;

	@PostConstruct
	void insertData() {
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

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private String fromFile(String file) {
		return convertStreamToString(getClass().getClassLoader().getResourceAsStream(file));
	}

	@Test
	public void testSummary() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths").param("date", "2020-04-08"))
				.andExpect(content().json(fromFile("responses/get-api-v1-deaths.json")));
	}

}
