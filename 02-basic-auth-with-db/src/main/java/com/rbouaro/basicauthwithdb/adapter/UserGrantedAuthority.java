package com.rbouaro.basicauthwithdb.adapter;

import com.rbouaro.basicauthwithdb.entity.Authority;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class UserGrantedAuthority implements GrantedAuthority {
    private transient Authority authority;
    @Override
    public String getAuthority() {
        return authority.getName();
    }
}