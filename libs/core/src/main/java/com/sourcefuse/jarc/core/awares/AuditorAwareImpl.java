package com.sourcefuse.jarc.core.awares;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.of(
            (
                (CurrentUser<?>) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal()
            ).getUser()
                .getId()
        );
    }
}
