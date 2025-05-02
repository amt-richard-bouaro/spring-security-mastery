package com.rbouaro.multiauthenticationfilter.security.provider;

import com.rbouaro.multiauthenticationfilter.security.authentication.ApiKeyAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

	/*  This is the API key that will be used to authenticate the requests
	This should be stored in a secure place, such as an environment variable or a configuration file

	Similar implementation could be done by fetching the API key from a database or a remote service for
	authentication.

	*/
	private final String apiKey;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			ApiKeyAuthentication apiKeyAuthentication = (ApiKeyAuthentication) authentication;
			if (apiKey.equals(apiKeyAuthentication.getApiKey())) {
				apiKeyAuthentication.setAuthenticated(true);
				return apiKeyAuthentication;
			}

		} catch (ClassCastException e) {
		 throw new InternalAuthenticationServiceException("Could not cast to ApiKeyAuthentication", e);
		}

		// If the API key is invalid, throw an exception
		throw new BadCredentialsException("Invalid API key");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(ApiKeyAuthentication.class);
	}
}