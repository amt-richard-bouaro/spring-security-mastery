package com.rbouaro.multiauthenticationfilter.security.manager;

import com.rbouaro.multiauthenticationfilter.security.provider.ApiKeyAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

	private final String apiKey;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		ApiKeyAuthenticationProvider apiKeyAuthenticationProvider = new ApiKeyAuthenticationProvider(apiKey);

		if (apiKeyAuthenticationProvider.supports(authentication.getClass())) {
			return apiKeyAuthenticationProvider.authenticate(authentication);
		}
		throw new BadCredentialsException("Invalid authentication type");
	}
}