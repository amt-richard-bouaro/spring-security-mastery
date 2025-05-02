package com.rbouaro.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig{


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.httpBasic( httpBasic -> httpBasic.init(http))
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests( auth -> auth
						.requestMatchers("/api/v1/resources/hello").hasRole("USER")
						.requestMatchers("/api/v1/resources/management/hello").hasRole("ADMIN")
						.requestMatchers("/api/v1/resources/goodbye").permitAll()
						.anyRequest().authenticated()
				)
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		var users = new InMemoryUserDetailsManager();

		var user001 = User.withUsername("user001")
				.password("password001").passwordEncoder(pass -> new BCryptPasswordEncoder().encode(pass))
				.authorities("read","ROLE_USER")
				.build();

		var user002 = User.withUsername("user002")
				.password("password002").passwordEncoder(pass -> new BCryptPasswordEncoder().encode(pass))
				.authorities("ROLE_ADMIN", "delete","read","write")
				.build();

		users.createUser(user001);
		users.createUser(user002);

		return users;
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}