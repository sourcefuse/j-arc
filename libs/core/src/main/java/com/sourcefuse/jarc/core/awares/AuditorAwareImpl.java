package com.sourcefuse.jarc.core.awares;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

  @Override
  public Optional<UUID> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    return Optional.ofNullable(
      authentication != null
        ? ((CurrentUser<?>) authentication.getPrincipal()).getUser().getId()
        : null
    );
  }
}
