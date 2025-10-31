package com.diegog.devopsExercise.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.io.Decoders;

public class JwtUtil {

	private static Key keyFromEnv() {
		String b64 = System.getProperty("JWT_SECRET_B64");
		if (b64 == null || b64.isBlank())
			b64 = System.getenv("JWT_SECRET_B64");
		if (b64 != null && !b64.isBlank())
			return Keys.hmacShaKeyFor(Decoders.BASE64.decode(b64));
		String raw = System.getProperty("JWT_SECRET", System.getenv("JWT_SECRET"));
		if (raw != null && raw.length() >= 32)
			return Keys.hmacShaKeyFor(raw.getBytes(StandardCharsets.UTF_8));
		throw new IllegalStateException("Missing strong JWT secret");
	}

	public static Jws<Claims> validate(String token, String secretIgnored) {
		return Jwts.parserBuilder().setSigningKey(keyFromEnv()).build().parseClaimsJws(token);
	}

	public static String mint(int ttlSec, String secretIgnored) {
		Instant now = Instant.now();
		return Jwts.builder().setIssuer("devops-exercise").setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusSeconds(ttlSec))).setId(UUID.randomUUID().toString())
				.signWith(keyFromEnv(), SignatureAlgorithm.HS256).compact();
	}

}
