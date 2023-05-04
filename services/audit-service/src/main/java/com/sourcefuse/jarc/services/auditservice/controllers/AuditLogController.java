package com.sourcefuse.jarc.services.auditservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	public ResponseEntity<AuditLog> create(@Valid @RequestBody AuditLog auditLog) {
		return new ResponseEntity<AuditLog>(this.auditLogRepository.save(auditLog), HttpStatus.CREATED);
	}

	@GetMapping("/count")
//	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Long> count() {
		return new ResponseEntity<Long>(this.auditLogRepository.count(), HttpStatus.OK);
	}

	@GetMapping
//	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Iterable<AuditLog>> find() {
		return new ResponseEntity<Iterable<AuditLog>>(this.auditLogRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
//	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AuditLog> findById(@PathVariable("id") UUID id) {
		AuditLog auditLog = this.auditLogRepository.findById(id).orElse(null);
		if (auditLog == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found for provided id");
		}
		return new ResponseEntity<AuditLog>(auditLog, HttpStatus.OK);
	}
}
