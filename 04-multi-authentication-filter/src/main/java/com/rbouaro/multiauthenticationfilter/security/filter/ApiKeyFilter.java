package com.rbouaro.multiauthenticationfilter.security.filter;

import com.rbouaro.multiauthenticationfilter.properties.ApplicationProperties;
import com.rbouaro.multiauthenticationfilter.security.authentication.ApiKeyAuthentication;
import com.rbouaro.multiauthenticationfilter.security.manager.CustomAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter is used to authenticate requests using an API key.
 * It checks if the request contains a valid API key in the header.
 * If the API key is valid, it allows the request to proceed.
 * Otherwise, it returns a 401 Unauthorized response.
 */

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

	private final ApplicationProperties applicationProperties;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		CustomAuthenticationManager customAuthenticationManager = new CustomAuthenticationManager(applicationProperties.apiKey());

		String apiKey = request.getHeader("x-api-key");

		if (apiKey == null || apiKey.isEmpty()) {
			filterChain.doFilter(request, response);
		}
		try {
			Authentication authentication = customAuthenticationManager.authenticate(new ApiKeyAuthentication(apiKey));
			if (authentication.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API key");
			}
		} catch (AuthenticationException e) {
			throw new AuthenticationServiceException("Authentication failed", e);
		}

	}
}