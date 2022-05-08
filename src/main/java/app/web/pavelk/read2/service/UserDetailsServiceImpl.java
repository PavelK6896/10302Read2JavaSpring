package app.web.pavelk.read2.service;


import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.schema.User;
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                return null;
            }
            org.springframework.security.core.userdetails.User principal
                    = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return userMap.get(principal.getUsername()).getId();
        } catch (Exception e) {
            return null;
        }
    }
}
