package com.noodle.todolist.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
	
	@Setter
	private String encryptionSecret;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		if (request.getServletPath().equals("/api/login")
				|| request.getServletPath().equals("/api/v1/user/token/refresh")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					String providedToken = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256(encryptionSecret.getBytes());
					JWTVerifier jwtVerifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = jwtVerifier.verify(providedToken);
					
					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					Arrays.stream(roles).forEach(role -> {
						authorities.add(new SimpleGrantedAuthority(role));
					});
					
					UsernamePasswordAuthenticationToken authenticationToken =
							new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					
					filterChain.doFilter(request, response);
					
				} catch (Exception e) {
					logger.error("Error logging in: {}", e);
					response.setHeader("login-error", e.getMessage());
					response.setStatus(FORBIDDEN.value());
					
					Map<String, String> errorMap = new HashMap<>();
					errorMap.put("error-message", e.getMessage());
					response.setContentType(APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), errorMap);
				}
			} else {
				filterChain.doFilter(request, response);
			}
			
			
		}
		
	}
}
