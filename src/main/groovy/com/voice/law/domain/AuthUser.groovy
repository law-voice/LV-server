package com.voice.law.domain

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthUser implements UserDetails {
    private String username
    private String password
    private String option
    private List<Role> roles

    AuthUser(String username, String password, String option, List<Role> roles) {
        this.username = username
        this.password = password
        this.option = option
        this.roles = roles
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.isEmpty() ? Collections.EMPTY_LIST :
                roles.collect {
                    new SimpleGrantedAuthority("ROLE_" + it.getRoleName())
                }
    }

    @Override
    String getPassword() {
        return password
    }

    @Override
    String getUsername() {
        return username
    }

    String getOption() {
        return option
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}