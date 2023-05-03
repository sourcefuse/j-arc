package com.sourcefuse.jarc.services.auditservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.auditservice.models.AuditLog;
import com.sourcefuse.jarc.services.auditservice.repositories.AuditLogRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/audit_logs")
@Validated
public class AuditLogController {

	@Autowired
	private AuditLogRepository auditLogRepository;

	// TODO: Remove comments of @PreAuthorize("isAuthenticated()") once
	// authorization service is integrated
	@PostMapping
//	@PreAuthorize("isAuthenticated()")
	public AuditLog create(@Valid @RequestBody AuditLog auditLog) {
		return this.auditLogRepository.save(auditLog);
	}

	@GetMapping("/count")
//	@PreAuthorize("isAuthenticated()")
	public Long count() {
		return this.auditLogRepository.count();
	}

	@GetMapping
//	@PreAuthorize("isAuthenticated()")
	public Iterable<AuditLog> find() {
		return this.auditLogRepository.findAll();
	}

	@GetMapping("/{id}")
//	@PreAuthorize("isAuthenticated()")
	public AuditLog findById(@PathVariable("id") UUID id) {
		return this.auditLogRepository.findById(id).orElse(null);
	}
}
