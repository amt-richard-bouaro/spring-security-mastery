package com.rbouaro.basicauthwithdb.security;

import com.rbouaro.basicauthwithdb.adapter.SecurityUser;
import com.rbouaro.basicauthwithdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}