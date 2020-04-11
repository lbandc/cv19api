package io.github.lbandc.cv19api;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("testData")
class FunctionalApiTests extends AbstractFunctionalTest {

	@Test
	public void testDeathsSummaryByTrust() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths").param("date", "2020-04-08"))
				.andExpect(status().isOk())
				.andExpect(content().json(fromFile("responses/get-api-v1-deaths.json")))
				.andDo(document("api/v1/deaths/get", preprocessResponse(prettyPrint())));
	}

	@Test
	public void testDeathsSummaryByRegion() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths/regions").param("date", "2020-04-08"))
				.andExpect(status().isOk())
				.andExpect(content().json(fromFile("responses/get-api-v1-deaths-regions.json")))
				.andDo(document("api/v1/deaths/regions/get", preprocessResponse(prettyPrint())));
	}

	@Test
	public void testTrust() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/rest/trusts/{code}", "RDD"))
				.andExpect(status().isOk())
				.andExpect(mvcResult -> {
					String response = mvcResult.getResponse().getContentAsString();
					JSONAssert.assertEquals(fromFile("responses/get-api-v1-rest-trusts-RDD.json"), response,
							new CustomComparator(JSONCompareMode.LENIENT,
							new Customization("lastUpdatedUtc", (o1, o2) -> true)));
				})
				.andDo(document("api/v1/rest/trusts/one/get", preprocessResponse(prettyPrint())));
	}

}
