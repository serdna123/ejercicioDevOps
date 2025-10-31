package com.diegog.devopsExercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.annotation.Resource;
import com.diegog.devopsExercise.security.JwtUtil;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DevopsExerciseApplicationTests {

	@Resource
	private MockMvc mockMvc;

	private static final String API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c3f6c";

	@Value("${JWT_SECRET:dev-secret-change-me}")
	private String secret;

	@BeforeAll
	static void validateSecretPresent() {
		assert System.getProperty("JWT_SECRET_B64") != null;
	}

	@Test
	void post_ok() throws Exception {
		String jwt = JwtUtil.mint(60, null);
		String body = """
				{"message":"This is a test","to":"Juan Perez","from":"Rita Asturia","timeToLifeSec":45}
				""";
		mockMvc.perform(post("/DevOps").header("X-Parse-REST-API-Key", API_KEY).header("X-JWT-KWY", jwt)
				.contentType("application/json").content(body)).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hello Juan Perez your message will be sent"));
	}

	@Test
	void wrong_method_returns_error() throws Exception {
		mockMvc.perform(get("/DevOps")).andExpect(status().isMethodNotAllowed()).andExpect(content().string("ERROR"));
	}

	@Test
	void missing_api_key_401() throws Exception {
		String jwt = JwtUtil.mint(60, secret);
		mockMvc.perform(post("/DevOps").header("X-JWT-KWY", jwt).contentType(MediaType.APPLICATION_JSON)
				.content("{\"message\":\"m\",\"to\":\"t\",\"from\":\"f\",\"timeToLifeSec\":10}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void invalid_jwt_401() throws Exception {
		mockMvc.perform(post("/DevOps").header("X-Parse-REST-API-Key", API_KEY).header("X-JWT-KWY", "bad.token.here")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"message\":\"m\",\"to\":\"t\",\"from\":\"f\",\"timeToLifeSec\":10}"))
				.andExpect(status().isUnauthorized());
	}
}