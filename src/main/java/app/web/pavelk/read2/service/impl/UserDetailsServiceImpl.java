package app.web.pavelk.read2.service.impl;


import app.web.pavelk.read2.repository.UserRepository;
import app.web.pavelk.read2.schema.User;
import app.web.pavelk.read2.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static app.web.pavelk.read2.exceptions.ExceptionMessage.USER_NOT_FOUND;

@Slf4j(topic = "user-service")
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
                    .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));
            userMap.put(user.getUsername(), user);
        }
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, user.getRoles());
    }

    @Override
    @Nullable
    public Long getUserId() {
        try {
            return getUser().getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    @NonNull
    public User getUser() {
        org.springframework.security.core.userdetails.User principal = getPrincipal();
        if (principal == null) throw new UsernameNotFoundException(USER_NOT_FOUND.getMessage());
        return Optional.ofNullable(userMap.get(principal.getUsername()))
                .orElseGet(() -> userRepository.findByUsername(principal.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage())));
    }

    @Override
    @NonNull
    public User getCurrentUserFromDB() {
        org.springframework.security.core.userdetails.User principal = getPrincipal();
        if (principal == null) throw new UsernameNotFoundException(USER_NOT_FOUND.getMessage());
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage().formatted(principal.getUsername())));
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

    @Nullable
    private org.springframework.security.core.userdetails.User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if ((authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            return null;
        }
        return (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    }


}
