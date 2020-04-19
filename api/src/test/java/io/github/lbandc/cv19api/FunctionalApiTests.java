package io.github.lbandc.cv19api;

import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("inmemory")
@Slf4j
class FunctionalApiTests extends AbstractFunctionalTest {

//	@MockBean
//	Ingestor fileRetriever;
//
//	@Autowired
//	TrustRepository trustRepository;
//
//	@Autowired
//	IngestRepository ingestRepository;
//
//	@Autowired
//	DeathRecordByTrustRepository deathRecordByTrustRepository;
//
//	@BeforeEach
//	public void setUp() throws IOException {
//		log.info("Ingesting 2nd April data for tests...");
//		Ingestor fr = new Ingestor(trustRepository, ingestRepository, deathRecordByTrustRepository);
//		var fileName = "COVID-19-daily-announced-deaths-2-April-2020.xlsx";
//		File file = new ClassPathResource(fileName).getFile();
//		fr.fetch(LocalDate.now(), file);
//	}
//
//	@Test
//	public void testDeathsSummary() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths"))
//				.andExpect(status().isOk()).andExpect(content().json(fromFile("responses/get-api-v1-deaths.json")))
//				.andDo(document("api/v1/deaths/get", preprocessResponse(prettyPrint())));
//	}
//
//	@Test
//	public void testDeathsSummaryByRegion() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths/regions"))
//				.andExpect(status().isOk())
//				.andExpect(content().json(fromFile("responses/get-api-v1-deaths-regions.json")))
//				.andDo(document("api/v1/deaths/regions/get", preprocessResponse(prettyPrint())));
//	}
//
//	@Test
//	public void testTrust() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rest/trusts/{code}", "RDD")).andExpect(status().isOk())
//				.andExpect(mvcResult -> {
//					String response = mvcResult.getResponse().getContentAsString();
//					JSONAssert.assertEquals(fromFile("responses/get-api-v1-rest-trusts-RDD.json"), response,
//							new CustomComparator(JSONCompareMode.LENIENT,
//									new Customization("lastUpdatedUtc", (o1, o2) -> true)));
//				}).andDo(document("api/v1/rest/trusts/one/get", preprocessResponse(prettyPrint())));
//	}
//
//	@Test
//	public void testPostIngestion() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/ingests/{fileDate}", "2020-04-02"))
//				.andExpect(status().isOk()).andExpect(mvcResult -> {
//					String response = mvcResult.getResponse().getContentAsString();
//
//					assertEquals("{\"status\":\"OK\"}", response);
//				});
//	}

}
