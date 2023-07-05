package com.sourcefuse.jarc.services.notificationservice.controllers;

import com.sourcefuse.jarc.core.constants.NotificationPermissions;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notification-users/{id}/notification")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationuserNotificationController {

  private final NotificationUserRepository notificationUserRepository;

  @Operation(summary = "get all notification of given notification user id")
  @GetMapping
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    NotificationPermissions.VIEW_NOTIFICATION +
    "')"
  )
  public ResponseEntity<Notification> find(@PathVariable("id") UUID id) {
    NotificationUser notificationUser =
      this.notificationUserRepository.findById(id)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "entity not found for provided id"
          )
        );

    return new ResponseEntity<>(
      notificationUser.getNotification(),
      HttpStatus.OK
    );
  }
}
