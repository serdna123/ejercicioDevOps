package com.diegog.devopsExercise;

import com.diegog.devopsExercise.security.JwtUtil;

public class MintJwtRunner {
	public static void main(String[] args) {
		int ttl = (args.length > 0) ? Integer.parseInt(args[0]) : 120;
		String secret = System.getenv().getOrDefault("JWT_SECRET", "dev-secret-change-me");
		System.out.println(JwtUtil.mint(ttl, secret));
	}
}
