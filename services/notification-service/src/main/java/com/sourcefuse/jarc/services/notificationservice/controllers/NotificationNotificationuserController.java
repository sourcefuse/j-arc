package com.sourcefuse.jarc.services.notificationservice.controllers;

import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import com.sourcefuse.jarc.services.notificationservice.specifications.NotificationUserSpecifications;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/notifications/{id}/notification-users")
@RequiredArgsConstructor
public class NotificationNotificationuserController {

  private final NotificationUserRepository notificationUserRepository;

  private final Validator validator;

  @Operation(summary = "find all notification users by notification id")
  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<NotificationUser>> find(
    @PathVariable("id") UUID notificationId
  ) {
    return new ResponseEntity<>(
      this.notificationUserRepository.findAll(
          NotificationUserSpecifications.byNotificationId(notificationId)
        ),
      HttpStatus.OK
    );
  }

  @Operation(summary = "create notification user for given notification id")
  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NotificationUser> create(
    @PathVariable("id") UUID notificationId,
    @Valid @RequestBody NotificationUser notificationUser
  ) {
    Notification notification = new Notification();
    notification.setId(notificationId);
    notificationUser.setNotification(notification);

    return new ResponseEntity<>(
      this.notificationUserRepository.save(notificationUser),
      HttpStatus.OK
    );
  }

  @Operation(summary = "update notification user for given notification id")
  @PatchMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Object> update(
    @PathVariable("id") UUID notificationId,
    @RequestBody NotificationUser notificationUser
  ) {
    notificationUser.setId(null);
    notificationUser.setNotification(null);
    List<NotificationUser> notificationUsers =
      this.notificationUserRepository.findAll(
          NotificationUserSpecifications.byNotificationId(notificationId)
        );
    for (NotificationUser target : notificationUsers) {
      BeanUtils.copyProperties(
        notificationUser,
        target,
        CommonUtils.getNullPropertyNames(notificationUser)
      );
      Set<ConstraintViolation<NotificationUser>> violations =
        validator.validate(target);
      if (!violations.isEmpty()) {
        return new ResponseEntity<>(
          violations
            .stream()
            .map(ele -> ele.getPropertyPath() + " " + ele.getMessage())
            .toList(),
          HttpStatus.BAD_REQUEST
        );
      }
    }
    this.notificationUserRepository.saveAll(notificationUsers);
    return new ResponseEntity<>(
      "Notification user updated successfully",
      HttpStatus.OK
    );
  }

  @Operation(
    summary = "mark notification as read for given notification id"
    		+ " and notification user id"
  )
  @PatchMapping("/{notificationUserId}/mark-as-read")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Object> markAsRead(
    @PathVariable("id") UUID notificationId,
    @PathVariable("notificationUserId") UUID notificationUserId
  ) {
    NotificationUser notificationUser =
      this.notificationUserRepository.findOne(
          NotificationUserSpecifications
            .byNotificationId(notificationId)
            .and(NotificationUserSpecifications.byId(notificationUserId))
        )
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "entity not found for provided id"
          )
        );
    notificationUser.setRead(true);
    this.notificationUserRepository.save(notificationUser);
    return new ResponseEntity<>(
      "Notification User Updated Successfully",
      HttpStatus.OK
    );
  }

  @Operation(summary = "delete all notification users of given notification id")
  @DeleteMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Object> delete(
    @PathVariable("id") UUID notificationId
  ) {
    List<NotificationUser> notificationUsers =
      this.notificationUserRepository.findAll(
          NotificationUserSpecifications.byNotificationId(notificationId)
        );
    this.notificationUserRepository.deleteAll(notificationUsers);
    return new ResponseEntity<>(notificationUsers.size(), HttpStatus.OK);
  }
}
