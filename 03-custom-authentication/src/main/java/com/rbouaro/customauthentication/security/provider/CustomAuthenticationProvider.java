package com.rbouaro.customauthentication.security.provider;

import com.rbouaro.customauthentication.properties.ApplicationProperties;
import com.rbouaro.customauthentication.security.authentication.CustomAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final ApplicationProperties properties;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthentication customAuthentication = (CustomAuthentication) authentication;
        if (properties.apiKey().equals(customAuthentication.getApiKey())) {
            return new CustomAuthentication(true, null);
        }

        throw new BadCredentialsException("Invalid API key");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.equals(authentication);
    }
}