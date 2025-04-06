package com.rbouaro.basicauthwithdb.adapter;

import com.rbouaro.basicauthwithdb.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private transient User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream().map(UserGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}