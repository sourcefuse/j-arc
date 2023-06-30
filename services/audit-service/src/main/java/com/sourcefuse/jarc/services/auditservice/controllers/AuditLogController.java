package com.sourcefuse.jarc.services.auditservice.controllers;

import com.sourcefuse.jarc.core.constants.AuditPermissions;
import com.sourcefuse.jarc.services.auditservice.models.AuditLog;
import com.sourcefuse.jarc.services.auditservice.repositories.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/audit_logs")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {

  @Autowired
  private AuditLogRepository auditLogRepository;

  @PostMapping
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    AuditPermissions.CREATE_AUDIT +
    "')"
  )
  @Operation(summary = "create audit log")
  public ResponseEntity<AuditLog> create(
    @Valid @RequestBody AuditLog auditLog
  ) {
    return new ResponseEntity<>(
      this.auditLogRepository.save(auditLog),
      HttpStatus.CREATED
    );
  }

  @GetMapping("/count")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    AuditPermissions.VIEW_AUDIT +
    "')"
  )
  @Operation(summary = "get count of audit logs")
  public ResponseEntity<Long> count() {
    return new ResponseEntity<>(this.auditLogRepository.count(), HttpStatus.OK);
  }

  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    AuditPermissions.VIEW_AUDIT +
    "')"
  )
  @Operation(summary = "find audit logs")
  public ResponseEntity<Iterable<AuditLog>> find() {
    return new ResponseEntity<>(
      this.auditLogRepository.findAll(),
      HttpStatus.OK
    );
  }

  @GetMapping("/{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    AuditPermissions.VIEW_AUDIT +
    "')"
  )
  @Operation(summary = "find audit log by provided id")
  public ResponseEntity<AuditLog> findById(@PathVariable("id") UUID id) {
    AuditLog auditLog =
      this.auditLogRepository.findById(id)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "entity not found for provided id"
          )
        );
    return new ResponseEntity<>(auditLog, HttpStatus.OK);
  }
}
