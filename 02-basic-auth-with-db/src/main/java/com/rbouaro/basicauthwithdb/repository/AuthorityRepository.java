package com.rbouaro.basicauthwithdb.repository;

import com.rbouaro.basicauthwithdb.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}