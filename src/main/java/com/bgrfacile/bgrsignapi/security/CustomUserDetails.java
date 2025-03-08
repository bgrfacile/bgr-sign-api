package com.bgrfacile.bgrsignapi.security;

import com.bgrfacile.bgrsignapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(exclude = "password")
public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username; // Correspond à l'email de l'utilisateur

    @JsonIgnore
    private String password;

    private boolean enabled; // Ajout du champ enabled

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    // Méthode statique pour créer une instance à partir d'un utilisateur
//    public static CustomUserDetails create(User user) {
//        List<GrantedAuthority> authorities = (user.getRoles() != null) ?
//                user.getRoles().stream()
//                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase())) // Ajout du préfixe "ROLE_"
//                        .collect(Collectors.toList())
//                : List.of(); // Si pas de rôle, liste vide
//
//        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.isEnabled(), authorities);
//    }

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.isEnabled(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
