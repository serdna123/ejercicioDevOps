package com.diegog.devopsExercise;

import com.diegog.devopsExercise.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DevopsControllerTest {
	@Autowired
	MockMvc mockMvc;
	private static final String API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c3f6c";

	@Test
	void postValidRequestShouldReturnOk() throws Exception {
		String jwt = JwtUtil.mint(60, null);
		String body = """
				{"message":"This is a test","to":"Juan Perez","from":"Rita Asturia","timeToLifeSec":45}
				""";
		mockMvc.perform(post("/DevOps").header("X-Parse-REST-API-Key", API_KEY).header("X-JWT-KWY", jwt)
				.contentType("application/json").content(body)).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hello Juan Perez your message will be sent"));
	}

	@Test
	void getShouldReturnError() throws Exception {
		mockMvc.perform(get("/DevOps")).andExpect(status().is4xxClientError());
	}
}
