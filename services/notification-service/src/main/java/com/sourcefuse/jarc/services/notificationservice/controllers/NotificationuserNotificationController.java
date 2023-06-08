package com.sourcefuse.jarc.services.notificationservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;

@RestController
@RequestMapping("/notification-users/{id}/notification")
public class NotificationuserNotificationController {

	@Autowired
	private NotificationUserRepository notificationUserRepository;

	@GetMapping
	// @PreAuthorize("isAuthenticated()")
	public ResponseEntity<Notification> find(@PathVariable("id") UUID notificationId) {
		NotificationUser notificationUser = this.notificationUserRepository.findById(notificationId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found for provided id"));

		return new ResponseEntity<>(notificationUser.getNotification(), HttpStatus.OK);
	}
}
