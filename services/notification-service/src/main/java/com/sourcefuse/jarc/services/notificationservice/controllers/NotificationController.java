package com.sourcefuse.jarc.services.notificationservice.controllers;

import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.simple.NotificationRepository;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import com.sourcefuse.jarc.services.notificationservice.service.NotificationUserService;
import com.sourcefuse.jarc.services.notificationservice.types.INotification;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class NotificationController {

  private final int maxBodyLen = 1000;

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private NotificationUserRepository notificationUserRepository;

  @Autowired
  private Validator validator;

  @Autowired
  private NotificationUserService notificationUserService;

  @Autowired
  private INotification notificationProvider;

  /*
   * TO_DO: Remove comments of @PreAuthorize("isAuthenticated()") once
   * authorization service is integrated
   */

  @PostMapping
  // @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Notification> create(
    @Valid @RequestBody Notification notification
  ) {
    notification.setId(null);
    notificationProvider.publish(notification);
    if (notification.getBody().length() > maxBodyLen) {
      notification.setBody(notification.getBody().substring(0, maxBodyLen - 1));
    }
    Notification notif = this.notificationRepository.save(notification);

    List<NotificationUser> receiversToCreate =
      this.notificationUserService.getNotifUsers(notif);
    this.notificationUserRepository.saveAll(receiversToCreate);

    return new ResponseEntity<>(notif, HttpStatus.CREATED);
  }

  @PostMapping("/bulk")
  // @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<Notification>> createBulkNotificaitions(
    @Valid @RequestBody List<Notification> notifications
  ) {
    List<NotificationUser> notifUsers = new ArrayList<>();

    notifications
      .stream()
      .forEach((Notification notification) -> {
        notification.setId(null);
        notificationProvider.publish(notification);
        if (notification.getBody().length() > maxBodyLen) {
          notification.setBody(
            notification.getBody().substring(0, maxBodyLen - 1)
          );
        }
      });
    List<Notification> notifs =
      this.notificationRepository.saveAll(notifications);

    for (Notification notif : notifs) {
      notifUsers.addAll(this.notificationUserService.getNotifUsers(notif));
    }
    this.notificationUserRepository.saveAll(notifUsers);

    return new ResponseEntity<>(
      this.notificationRepository.saveAll(notifications),
      HttpStatus.CREATED
    );
  }

  @GetMapping("/count")
  // @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Long> count() {
    return new ResponseEntity<>(
      this.notificationRepository.count(),
      HttpStatus.OK
    );
  }

  @GetMapping
  // @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<Notification>> find() {
    return new ResponseEntity<>(
      this.notificationRepository.findAll(),
      HttpStatus.OK
    );
  }

  @GetMapping("/{id}")
  // @PreAuthorize("isAuthenticated()")
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

  @PatchMapping("/{id}")
  // @PreAuthorize("isAuthenticated()")
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
          .collect(Collectors.toList()),
        HttpStatus.BAD_REQUEST
      );
    }
    this.notificationRepository.save(existingNotification);
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  @DeleteMapping
  // @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteAll() {
    this.notificationRepository.deleteAll();
    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
