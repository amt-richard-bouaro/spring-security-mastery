package com.rbouaro.authenticationserver.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.AbstractSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Entity class for storing OAuth2 registered client information in the database.
 * This class provides mapping between Spring Security's RegisteredClient and the database entity.
 */
@Entity
@Table(name = "registered_clients")
@Setter
@Getter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredClientEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String clientId;

	@Column
	private Instant clientIdIssuedAt;

	@Column
	private String clientSecret;

	@Column
	private Instant clientSecretExpiresAt;

	@Column
	private String clientName;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "client_auth_methods", joinColumns = @JoinColumn(name = "client_id"))
	@Column(name = "auth_method")
	private Set<String> clientAuthenticationMethods = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
	@Column(name = "grant_type")
	private Set<String> authorizationGrantTypes = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "client_redirect_uris", joinColumns = @JoinColumn(name = "client_id"))
	@Column(name = "redirect_uri")
	private Set<String> redirectUris = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "client_post_logout_uris", joinColumns = @JoinColumn(name = "client_id"))
	@Column(name = "post_logout_uri")
	private Set<String> postLogoutRedirectUris = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "client_scopes", joinColumns = @JoinColumn(name = "client_id"))
	@Column(name = "scope")
	private Set<String> scopes = new HashSet<>();

	@Column(columnDefinition = "TEXT")
	private String clientSettings;

	@Column(columnDefinition = "TEXT")
	private String tokenSettings;

	private static final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());

	/**
	 * Converts this entity to a Spring Security RegisteredClient object.
	 *
	 * @return the RegisteredClient object
	 */
	public RegisteredClient toRegisteredClient() {
		RegisteredClient.Builder builder = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId(clientId)
				.clientIdIssuedAt(clientIdIssuedAt != null ? clientIdIssuedAt : Instant.now())
				.clientSecret(clientSecret)
				.clientName(clientName);

		if (clientSecretExpiresAt != null) {
			builder.clientSecretExpiresAt(clientSecretExpiresAt);
		}

		// Add authentication methods
		clientAuthenticationMethods.forEach(method ->
				builder.clientAuthenticationMethod(new ClientAuthenticationMethod(method)));

		// Add grant types
		authorizationGrantTypes.forEach(grantType ->
				builder.authorizationGrantType(new AuthorizationGrantType(grantType)));

		// Add URIs and scopes
		redirectUris.forEach(builder::redirectUri);
		postLogoutRedirectUris.forEach(builder::postLogoutRedirectUri);
		scopes.forEach(builder::scope);

		// Default settings
		ClientSettings defaultClientSettings = ClientSettings.builder()
				.requireAuthorizationConsent(true)
				.requireProofKey(true)
				.build();
		TokenSettings defaultTokenSettings = TokenSettings.builder()
				.accessTokenTimeToLive(Duration.ofHours(1))
				.refreshTokenTimeToLive(Duration.ofDays(30))
				.reuseRefreshTokens(false)
				.build();

		// Apply client settings
		applySettings(
				clientSettings,
				ClientSettings.class,
				builder::clientSettings,
				defaultClientSettings
		);

		// Apply token settings
		applySettings(
				tokenSettings,
				TokenSettings.class,
				builder::tokenSettings,
				defaultTokenSettings
		);

		return builder.build();
	}

	/**
	 * Converts a Spring Security RegisteredClient to a RegisteredClientEntity.
	 *
	 * @param registeredClient the RegisteredClient to convert
	 * @return the RegisteredClientEntity
	 */
	public static RegisteredClientEntity fromRegisteredClient(RegisteredClient registeredClient) {
		RegisteredClientEntity entity = RegisteredClientEntity.builder()
				.clientId(registeredClient.getClientId())
				.clientIdIssuedAt(registeredClient.getClientIdIssuedAt())
				.clientSecret(registeredClient.getClientSecret())
				.clientName(registeredClient.getClientName())
				.clientSecretExpiresAt(registeredClient.getClientSecretExpiresAt())
				.clientAuthenticationMethods(
						registeredClient.getClientAuthenticationMethods().stream()
								.map(ClientAuthenticationMethod::getValue)
								.collect(Collectors.toSet()))
				.authorizationGrantTypes(
						registeredClient.getAuthorizationGrantTypes().stream()
								.map(AuthorizationGrantType::getValue)
								.collect(Collectors.toSet()))
				.redirectUris(registeredClient.getRedirectUris())
				.postLogoutRedirectUris(registeredClient.getPostLogoutRedirectUris())
				.scopes(registeredClient.getScopes())
				.build();

		try {
			// Serialize client settings
			entity.setClientSettings(objectMapper.writeValueAsString(
					registeredClient.getClientSettings().getSettings()));

			// Serialize token settings
			entity.setTokenSettings(objectMapper.writeValueAsString(
					registeredClient.getTokenSettings().getSettings()));
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize settings for client: {}", registeredClient.getClientId(), e);
			throw new IllegalStateException("Failed to serialize client settings", e);
		}

		return entity;
	}

	/**
	 * Applies settings from JSON to the RegisteredClient builder.
	 *
	 * @param settingsJson    the JSON string containing settings
	 * @param settingsType    the type of settings (ClientSettings or TokenSettings)
	 * @param settingsApplier consumer to apply the settings
	 * @param defaultSettings default settings to use if parsing fails
	 * @param <T>             the type of settings
	 */
//	private <T> void applySettings(
//			String settingsJson,
//			Class<? extends AbstractSettings> settingsType,
//			Consumer<T> settingsApplier,
//			T defaultSettings) {
//		if (settingsJson != null && !settingsJson.trim().isEmpty()) {
//			try {
//				// Parse the JSON string to a Map
//				Map<String, Object> settingsMap = objectMapper.readValue(settingsJson, Map.class);
//
//				// Create the appropriate settings object
//				@SuppressWarnings("unchecked")
//				T settings = (T) (settingsType == ClientSettings.class
//						? ClientSettings.builder().settings(s -> s.putAll(settingsMap)).build()
//						: TokenSettings.builder().settings(s -> s.putAll(settingsMap)).build());
//
//
//
//				settingsApplier.accept(settings);
//			} catch (JsonProcessingException e) {
//				log.error("Failed to parse {} for clientId: {}", settingsType.getSimpleName(), clientId, e);
//				settingsApplier.accept(defaultSettings);
//			}
//		} else {
//			settingsApplier.accept(defaultSettings);
//		}
//	}
	private <T extends AbstractSettings> void applySettings(
			String settingsJson,
			Class<? extends AbstractSettings> settingsType,
			Consumer<T> settingsApplier,
			T defaultSettings) {
		if (settingsJson != null && !settingsJson.trim().isEmpty()) {
			try {
				// Parse the JSON string to a Map
				@SuppressWarnings("unchecked")
				Map<String, Object> settingsMap = objectMapper.readValue(settingsJson, Map.class);

				// Process settings based on type
				T settings;
				if (settingsType == TokenSettings.class) {
					// Convert specific fields to their expected types
					Map<String, Object> processedMap = new HashMap<>();
					settingsMap.forEach((key, value) -> {
						switch (key) {
							case "settings.token.access-token-time-to-live",
							     "settings.token.refresh-token-time-to-live",
							     "settings.token.authorization-code-time-to-live",
							     "settings.token.device-code-time-to-live":
								if (value instanceof Number numberValue) {
									long seconds = numberValue.longValue();
									processedMap.put(key, Duration.ofSeconds(seconds));
								} else {
									processedMap.put(key, value);
								}
								break;
							case "settings.token.access-token-format":
								if (value instanceof Map<?, ?> formatMap) {
									String formatValue = (String) formatMap.get("value");
									processedMap.put(key, new OAuth2TokenFormat(formatValue));
								} else {
									processedMap.put(key, value);
								}
								break;
							case "settings.token.id-token-signature-algorithm":
								if (value instanceof String stringValue) {
									processedMap.put(key, SignatureAlgorithm.from(stringValue));
								} else {
									processedMap.put(key, value);
								}
								break;
							default:
								processedMap.put(key, value);
						}
					});

					settings = (T) TokenSettings.builder()
							.settings(s -> s.putAll(processedMap))
							.build();
				} else {
					// For ClientSettings, use the map as-is
					settings = (T) ClientSettings.builder()
							.settings(s -> s.putAll(settingsMap))
							.build();
				}

				settingsApplier.accept(settings);
			} catch (JsonProcessingException e) {
				log.error("Failed to parse {} for clientId: {}", settingsType.getSimpleName(), clientId, e);
				settingsApplier.accept(defaultSettings);
			}
		} else {
			settingsApplier.accept(defaultSettings);
		}
	}
}