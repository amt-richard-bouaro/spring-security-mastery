package com.rbouaro.multiauthenticationfilter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        String apiKey
) {
}