package com.sourcefuse.jarc.services.notificationservice.controllers;

import com.sourcefuse.jarc.core.constants.NotificationPermissions;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.notificationservice.dtos.NotificationList;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.simple.NotificationRepository;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import com.sourcefuse.jarc.services.notificationservice.service.NotificationUserService;
import com.sourcefuse.jarc.services.notificationservice.types.INotification;
import com.sourcefuse.jarc.services.notificationservice.types.INotificationFilterFunc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

  private static final int MAX_BODY_LENGTH = 1000;

  private final NotificationRepository notificationRepository;

  private final NotificationUserRepository notificationUserRepository;

  private final Validator validator;

  private final NotificationUserService notificationUserService;

  private final INotification notificationProvider;

  @Nullable
  private final INotificationFilterFunc notificationFilterFunc;

  @Operation(summary = "Create and publish Notification")
  @PostMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.CREATE_NOTIFICATION +
    "')"
  )
  @Transactional
  public ResponseEntity<Notification> create(
    @Valid @RequestBody Notification notification
  ) {
    notification.setId(null);
    notification = removeBlackListedUsers(notification);
    notificationProvider.publish(notification);
    if (notification.getBody().length() > MAX_BODY_LENGTH) {
      notification.setBody(
        notification.getBody().substring(0, MAX_BODY_LENGTH - 1)
      );
    }
    Notification notif = this.notificationRepository.save(notification);

    List<NotificationUser> receiversToCreate =
      this.notificationUserService.getNotifUsers(notif);
    this.notificationUserRepository.saveAll(receiversToCreate);
    return new ResponseEntity<>(notif, HttpStatus.OK);
  }

  @Operation(summary = "Create and publish Notifications")
  @PostMapping("/bulk")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.CREATE_NOTIFICATION +
    "')"
  )
  @Transactional
  public ResponseEntity<List<Notification>> createBulkNotificaitions(
    @Valid @RequestBody NotificationList notificationList
  ) {
    List<NotificationUser> notifUsers = new ArrayList<>();

    notificationList
      .getNotifications()
      .stream()
      .forEach((Notification notification) -> {
        notification.setId(null);
        notification = removeBlackListedUsers(notification);
        notificationProvider.publish(notification);
        if (notification.getBody().length() > MAX_BODY_LENGTH) {
          notification.setBody(
            notification.getBody().substring(0, MAX_BODY_LENGTH - 1)
          );
        }
      });
    List<Notification> notifs =
      this.notificationRepository.saveAll(notificationList.getNotifications());

    for (Notification notif : notifs) {
      notifUsers.addAll(this.notificationUserService.getNotifUsers(notif));
    }
    this.notificationUserRepository.saveAll(notifUsers);

    return new ResponseEntity<>(notifs, HttpStatus.OK);
  }

  @Operation(summary = "Get total count of notifications")
  @GetMapping("/count")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.VIEW_NOTIFICATION +
    "')"
  )
  public ResponseEntity<CountResponse> count() {
    return new ResponseEntity<>(
      CountResponse
        .builder()
        .count(this.notificationRepository.count())
        .build(),
      HttpStatus.OK
    );
  }

  @Operation(summary = "get all notifications")
  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<Notification>> find() {
    return new ResponseEntity<>(
      this.notificationRepository.findAll(),
      HttpStatus.OK
    );
  }

  @Operation(summary = "get notification by id")
  @GetMapping("/{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.VIEW_NOTIFICATION +
    "')"
  )
  public ResponseEntity<Notification> findById(@PathVariable("id") UUID id) {
    Notification notification =
      this.notificationRepository.findById(id)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "entity not found for provided id"
          )
        );
    return new ResponseEntity<>(notification, HttpStatus.OK);
  }

  @Operation(summary = "update notification by id")
  @PatchMapping("/{id}")
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.UPDATE_NOTIFICATION +
    "')"
  )
  public ResponseEntity<Object> updateById(
    @PathVariable("id") UUID id,
    @RequestBody Notification notification
  ) {
    Notification existingNotification =
      this.notificationRepository.findById(id)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "entity not found for provided id"
          )
        );
    BeanUtils.copyProperties(
      notification,
      existingNotification,
      CommonUtils.getNullPropertyNames(notification)
    );
    Set<ConstraintViolation<Notification>> violations = validator.validate(
      existingNotification
    );

    if (!violations.isEmpty()) {
      return new ResponseEntity<>(
        violations
          .stream()
          .map(ele -> ele.getPropertyPath() + " " + ele.getMessage())
          .toList(),
        HttpStatus.BAD_REQUEST
      );
    }
    this.notificationRepository.save(existingNotification);
    return new ResponseEntity<>(
      "Notification Updated Successfully",
      HttpStatus.OK
    );
  }

  @Operation(summary = "delete all notifications")
  @DeleteMapping
  @PreAuthorize(
    "isAuthenticated() && hasAuthority('" +
    NotificationPermissions.DELETE_NOTIFICATION +
    "')"
  )
  public ResponseEntity<Object> deleteAll() {
    this.notificationRepository.deleteAll();
    return new ResponseEntity<>(
      "Notifications Deleted Successfully",
      HttpStatus.OK
    );
  }

  private Notification removeBlackListedUsers(Notification notification) {
    if (notificationFilterFunc == null) {
      return notification;
    }
    return notificationFilterFunc.filter(notification);
  }
}
