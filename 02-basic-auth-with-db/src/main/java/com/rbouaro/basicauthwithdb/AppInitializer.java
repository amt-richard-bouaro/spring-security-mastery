package com.rbouaro.basicauthwithdb;

import com.rbouaro.basicauthwithdb.entity.Authority;
import com.rbouaro.basicauthwithdb.entity.User;
import com.rbouaro.basicauthwithdb.repository.AuthorityRepository;
import com.rbouaro.basicauthwithdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {

        Authority read =  authorityRepository.save(Authority.builder().name("READ").build());
             Authority write =   authorityRepository.save(Authority.builder().name("WRITE").build());
        Authority delete =   authorityRepository.save(Authority.builder().name("DELETE").build());


        Set<User> users = Set.of(
                User.builder()
                        .username("admin")
                        .password("admin")
                        .email("admin@example.com")
                        .firstName("Vijay Kim")
                        .lastName("Vijay")
                        .authorities(Set.of(write,read, delete))
                        .build(),
                User.builder()
                        .username("user")
                        .password("user")
                        .email("user@example.com")
                        .firstName("John Doe")
                        .lastName("Doe")
                        .authorities(Set.of(read))
                        .build()
        );
        userRepository.saveAll(users);

    }
}