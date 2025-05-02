package com.rbouaro.authorization.config;

import org.springframework.stereotype.Component;

@Component
public class SecurityGuard {


	public boolean validateHelloMessage(String message) {
		return message.toLowerCase().contains("hello ");
	}
}