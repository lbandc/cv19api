package io.github.lbandc.cv19api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("inmemory")
@Slf4j
class FunctionalApiTests extends AbstractFunctionalTest {

	@Autowired
	Ingester ingester;

	@Autowired
	TrustRepository trustRepository;

	@Autowired
	IngestRepository ingestRepository;

	@Autowired
	DeathRecordByTrustRepository deathRecordByTrustRepository;

	@Autowired
	DeathRecordByAgeRepository ageRecordRepository;

	@BeforeEach
	public void setUp() throws IOException {
		log.info("Ingesting 2nd April data for tests...");
		this.ingester.ingest(LocalDate.of(2020, 4, 2), new XlsxLocalFileFetcher(LocalDate.of(2020, 4, 2)));

	}

	@AfterEach
	public void tearDown() {
		// TODO why is the transaction not rolling back?
		this.deathRecordByTrustRepository.deleteAll();
		this.ageRecordRepository.deleteAll();
		this.trustRepository.deleteAll();
		this.ingestRepository.deleteAll();
	}

	@Test
	public void testDeathsSummary() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths").param("recordedOnTo", "2020-04-13")
				.param("from", "2020-03-15").param("to", "2020-04-30")).andExpect(status().isOk())
				.andExpect(content().json(fromFile("responses/get-api-v1-deaths.json")))
				.andDo(document("api/v1/deaths/get", preprocessResponse(prettyPrint())));
	}

	@Test
	public void testDeathsSummaryByRegion() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths/regions").param("recordedOnTo", "2020-04-13")
				.param("from", "2020-03-15").param("to", "2020-04-30")).andExpect(status().isOk())
				.andExpect(content().json(fromFile("responses/get-api-v1-deaths-regions.json")))
				.andDo(document("api/v1/deaths/regions/get", preprocessResponse(prettyPrint())));
	}

	@Test
	public void testTrust() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rest/trusts/{code}", "RDD")).andExpect(status().isOk())
				.andExpect(mvcResult -> {
					String response = mvcResult.getResponse().getContentAsString();
					JSONAssert.assertEquals(fromFile("responses/get-api-v1-rest-trusts-RDD.json"), response,
							new CustomComparator(JSONCompareMode.LENIENT,
									new Customization("lastUpdatedUtc", (o1, o2) -> true)));
				}).andDo(document("api/v1/rest/trusts/one/get", preprocessResponse(prettyPrint())));
	}

	@Test
	public void testPostIngestion() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/ingests/{fileDate}", "2020-04-02"))
				.andExpect(status().isOk()).andExpect(mvcResult -> {
					String response = mvcResult.getResponse().getContentAsString();

					assertEquals("{\"status\":\"OK\"}", response);
				});

	}

}
