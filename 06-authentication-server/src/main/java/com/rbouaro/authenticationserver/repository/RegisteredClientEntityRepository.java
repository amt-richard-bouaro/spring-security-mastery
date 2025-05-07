package com.rbouaro.authenticationserver.repository;

import com.rbouaro.authenticationserver.entity.RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredClientEntityRepository extends JpaRepository<RegisteredClientEntity, Long> {

	Optional<RegisteredClientEntity> findByClientId(String clientId);

	RegisteredClientEntity save(RegisteredClientEntity entity);
}