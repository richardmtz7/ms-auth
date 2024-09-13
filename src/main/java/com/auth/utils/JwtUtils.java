package com.auth.utils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtils {
	
	@Value("${security.jwt.key}")
	private String privateKey;
	
	@Value("${security.jwt.generator}")
	private String userGenerator;
	
	public String createToken(Authentication authentication ) {
		Algorithm algorithm = Algorithm.HMAC256(privateKey);
		
		String username = authentication.getPrincipal().toString();
		String authorities = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		String jwtToken = JWT.create()
				.withIssuer(this.userGenerator)
				.withSubject(username)
				.withClaim("authorities", authorities )
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(new Date(System.currentTimeMillis()))
				.sign(algorithm);
		
		return jwtToken;
	}
	
	public DecodedJWT validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(privateKey);
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer(this.userGenerator)
					.build();
			
			return verifier.verify(token);
			
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException("Token invalid, not Authorized");
		}
	}
	
	public String extractUser(DecodedJWT decodedJWT) {
		return decodedJWT.getSubject().toString();
	}
	
	public Claim getSpecificClaim(DecodedJWT decodedJWT, String clainName) {
		return decodedJWT.getClaim(clainName);
	}
	
	public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
		return decodedJWT.getClaims();
	}
	
}
