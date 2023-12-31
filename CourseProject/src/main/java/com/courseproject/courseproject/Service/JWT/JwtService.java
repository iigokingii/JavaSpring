package com.courseproject.courseproject.Service.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	
	@Value("${jwt_secret}")
	String jwtSecretKey;
	//@Value("${jwtExpirationMS}")
	Long jwtExpirationMS = 3600000L;
	public String extractUserName(String token) {
		System.out.println("[JwtService]-extractUserName");
		return extractClaim(token, Claims::getSubject);
	}
	
	public String generateToken(UserDetails userDetails) {
		
		System.out.println("[JwtService]-generateToken");
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		System.out.println("[JwtService]-isTokenValid");
		
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		System.out.println("[JwtService]-extractClaim");
		
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	}
	
	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		System.out.println("[JwtService]-generateToken");
		
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMS))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private boolean isTokenExpired(String token) {
		System.out.println("[JwtService]-isTokenExpired");
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		System.out.println("[JwtService]-extractExpiration");
		return extractClaim(token, Claims::getExpiration);
	}
	
	private Claims extractAllClaims(String token) {
		System.out.println("[JwtService]-extractAllClaims");
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Key getSigningKey() {
		System.out.println("[JwtService]-getSigningKey");
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
