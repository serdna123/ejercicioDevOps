package com.diegog.devopsExercise;

import org.junit.jupiter.api.BeforeAll;

public class TestSecurityConfig {
	@BeforeAll
	static void setUpSecret() {
		// 32 bytes en Base64 (ejemplo seguro)
		System.setProperty("JWT_SECRET_B64", "d/3jY5k7gnrQ1qYc5W3c0Qw2Sg2kGm3w5mU5pE0kqvA=");
	}
}