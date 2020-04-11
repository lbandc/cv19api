package com.cv19api.api;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("testData")
class FunctionalApiTests extends AbstractFunctionalTest {

	@Test
	public void testDeathsSummary() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deaths").param("date", "2020-04-08"))
				.andExpect(content().json(fromFile("responses/get-api-v1-deaths.json")))
				.andDo(document("api/v1/deaths/get", preprocessResponse(prettyPrint())));
	}

}
