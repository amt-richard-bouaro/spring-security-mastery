package com.rbouaro.multiauthenticationfilter.security;

import com.rbouaro.multiauthenticationfilter.security.filter.ApiKeyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(basic -> basic.init(http))
                .addFilterBefore(apiKeyFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                        req.anyRequest().authenticated()
                )
                .build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        var users = new InMemoryUserDetailsManager();

        // For the purpose of this example, in-memory user details is being used.
        users.createUser(User.withUsername("user").password("password").passwordEncoder(pass-> new BCryptPasswordEncoder().encode(pass)).build());
        users.createUser(User.withUsername("admin").password("root").passwordEncoder(pass-> new BCryptPasswordEncoder().encode(pass)).build());
        return users;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}