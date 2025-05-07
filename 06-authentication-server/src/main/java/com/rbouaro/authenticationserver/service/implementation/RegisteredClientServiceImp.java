package com.rbouaro.authenticationserver.service.implementation;

import com.rbouaro.authenticationserver.entity.RegisteredClientEntity;
import com.rbouaro.authenticationserver.repository.RegisteredClientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisteredClientServiceImp implements RegisteredClientRepository {
	private final RegisteredClientEntityRepository registeredClientEntityRepository;

	@Override
	public void save(RegisteredClient registeredClient) {
		RegisteredClientEntity entity = RegisteredClientEntity.fromRegisteredClient(registeredClient);

		registeredClientEntityRepository.save(entity);
	}

	@Override
	public RegisteredClient findById(String id) {
		return registeredClientEntityRepository.findById(Long.parseLong(id))
				.map(RegisteredClientEntity::toRegisteredClient)
				.orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		return registeredClientEntityRepository.findByClientId(clientId)
				.map(RegisteredClientEntity::toRegisteredClient)
				.orElse(null);
	}
}