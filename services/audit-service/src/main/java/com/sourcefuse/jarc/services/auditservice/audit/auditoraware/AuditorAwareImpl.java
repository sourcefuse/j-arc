package com.sourcefuse.jarc.services.auditservice.audit.auditoraware;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sourcefuse.jarc.services.authservice.session.CurrentUser;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

	@Override
	public Optional<UUID> getCurrentAuditor() {
		return Optional.of(((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser().getId());
	}
}