package com.rbouaro.authenticationserver.service.implementation;

import com.rbouaro.authenticationserver.repository.UserRepository;
import com.rbouaro.authenticationserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

	private final UserRepository userRepository;

}