package com.diegog.devopsExercise.controller;

import com.diegog.devopsExercise.dto.DevOpsRequest;
import com.diegog.devopsExercise.security.JwtUtil;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DevOpsController {
	private static final String API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c3f6c";
	private final String jwtSecret = System.getenv().getOrDefault("JWT_SECRET", "dev-secret-change-me");

	// Otros métodos → ERROR (405)
	@RequestMapping(value = "/DevOps", method = { RequestMethod.GET, RequestMethod.PUT, RequestMethod.PATCH,
			RequestMethod.DELETE })
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public @ResponseBody String wrongMethods() {
		return "ERROR";
	}

	@PostMapping("/DevOps")
	public ResponseEntity<?> postDevops(@RequestBody DevOpsRequest req,
			@RequestHeader(value = "X-Parse-REST-API-Key", required = false) String apiKey,
			@RequestHeader(value = "X-JWT-KWY", required = false) String jwt) {
		if (apiKey == null || !apiKey.equals("2f5ae96c-b558-4c7b-a590-a501ae1c3f6c")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
		}
		try {
			JwtUtil.validate(jwt, null); // si falta o es inválido, lanzará excepción
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
		}
		return ResponseEntity.ok(Map.of("message", "Hello " + req.getTo() + " your message will be sent"));
	}

	// Y por si algún otro handler lanza MissingRequestHeaderException igual mapea a
	// 401:
	@ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
	public ResponseEntity<?> onMissingHeader() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
	}
}
