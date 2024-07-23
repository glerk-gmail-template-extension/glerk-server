package com.glerk.core.config.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of(99999999L);
        }

        String principal = (String) authentication.getPrincipal();
        Long id = Long.valueOf(principal);

        return Optional.of(id);
    }
}
