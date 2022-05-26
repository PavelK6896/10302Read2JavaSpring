package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    @Getter
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userMap.get(username);
        if (user == null) {
            Optional<User> userOptional = userRepository.findByUsername(username);
            user = userOptional
                    .orElseThrow(() -> new UsernameNotFoundException("No user Found with username : " + username));
            userMap.put(user.getUsername(), user);
        }
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public Long getUserId() {
        try {
            org.springframework.security.core.userdetails.User principal = getPrincipal();
            if (principal == null) return null;
            return userMap.get(principal.getUsername()).getId();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User getUser() {
        try {
            org.springframework.security.core.userdetails.User principal = getPrincipal();
            if (principal == null) return null;
            return userMap.get(principal.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    private org.springframework.security.core.userdetails.User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            return null;
        }
        return (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        if ((authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            return false;
        }
        return authentication.isAuthenticated();
    }

}
