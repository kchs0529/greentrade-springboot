package com.mega.greentrade.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserDTO userDTO;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(UserDTO userDTO, List<GrantedAuthority> authorities) {
        this.userDTO = userDTO;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDTO.getUser_password();
    }

    @Override
    public String getUsername() {
        return userDTO.getUser_id();
    }

    public int getUserno() {
        return userDTO.getUserno();
    }

    public String getNickname() {
        return userDTO.getNickname();
    }
}
