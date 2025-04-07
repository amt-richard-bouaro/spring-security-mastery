package com.rbouaro.customauthentication.security.manager;

import com.rbouaro.customauthentication.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private final CustomAuthenticationProvider customAuthenticationProvider;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       if (customAuthenticationProvider.supports(authentication.getClass())) {
         return customAuthenticationProvider.authenticate(authentication);
       }

       throw new BadCredentialsException("Invalid authentication type");
    }
}