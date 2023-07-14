package com.sourcefuse.jarc.services.notificationservice.controllers;

import com.sourcefuse.jarc.core.constants.NotificationPermissions;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.notificationservice.dtos.AccessResponse;
import com.sourcefuse.jarc.services.notificationservice.dtos.SuccessResponse;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationAccess;
import com.sourcefuse.jarc.services.notificationservice.providers.ChannelManagerService;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubNotification;
import com.sourcefuse.jarc.services.notificationservice.repositories.redis.NotificationAccessRepository;
import com.sourcefuse.jarc.services.notificationservice.types.Config;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notifications/access/{id}")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PubNubNotificationController {

  @Nullable
  private final PubNubNotification pubNubNotification;

  @Nullable
  private final ChannelManagerService channelManagerService;

  private final NotificationAccessRepository notificationAccessRepository;

  @Operation(summary = "grant access of notification to user")
  @PatchMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.CAN_GET_NOTIFICATION_ACCESS +
    "')"
  )
  public ResponseEntity<AccessResponse> grantAccess(
    @Valid @RequestBody NotificationAccess notification,
    @PathVariable("id") UUID userId,
    @RequestHeader(value = "pubnubToken") String token
  ) {
    Config config = Config
      .builder()
      .receiver(notification.getReceiver())
      .type(notification.getType())
      .options(notification.getOptions())
      .build();

    if (config.getOptions() != null) {
      config.getOptions().put("token", token);
    }
    CurrentUser currentUser =
      (
        (CurrentUser) SecurityContextHolder
          .getContext()
          .getAuthentication()
          .getPrincipal()
      );
    if (
      currentUser != null &&
      this.channelManagerService != null &&
      !this.channelManagerService.isChannelAccessAllowed(currentUser, config)
    ) {
      throw new ResponseStatusException(
        HttpStatus.FORBIDDEN,
        "Access Not Allowed"
      );
    }
    this.pubNubNotification.grantAccess(config);
    notification.setId(userId);
    this.notificationAccessRepository.save(notification);
    return new ResponseEntity<>(new AccessResponse(null, ""), HttpStatus.OK);
  }

  @Operation(summary = "revoke access of notification of user")
  @DeleteMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<SuccessResponse> revokeAccess(
    @PathVariable("id") UUID userId
  ) {
    NotificationAccess notification =
      this.notificationAccessRepository.findById(userId)
        .orElseThrow(() ->
          new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found")
        );

    Config config = Config
      .builder()
      .receiver(notification.getReceiver())
      .type(notification.getType())
      .options(notification.getOptions())
      .build();

    this.pubNubNotification.revokeAccess(config);
    notification.setId(userId);
    this.notificationAccessRepository.deleteById(userId);
    return new ResponseEntity<>(new SuccessResponse(true), HttpStatus.OK);
  }
}
