package com.sourcefuse.jarc.services.notificationservice.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.sourcefuse.jarc.services.notificationservice.dtos.AccessResponseDto;
import com.sourcefuse.jarc.services.notificationservice.dtos.SuccessResponseDto;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationAccess;
import com.sourcefuse.jarc.services.notificationservice.providers.ChannelManagerService;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubNotification;
import com.sourcefuse.jarc.services.notificationservice.repositories.redis.NotificationAccessRepository;
import com.sourcefuse.jarc.services.notificationservice.types.Config;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notifications/access/{id}")
public class PubNubNotificationController {

	@Autowired(required = false)
	PubNubNotification pubNubNotification;

	@Autowired(required = false)
	ChannelManagerService channelManagerService;

	@Autowired
	NotificationAccessRepository notificationAccessRepository;

	@PatchMapping
	public ResponseEntity<AccessResponseDto> grantAccess(@Valid @RequestBody NotificationAccess notification,
			@PathVariable("id") UUID userId, @RequestHeader(value = "pubnubToken") String token) {
		Config config = Config.builder().receiver(notification.getReceiver()).type(notification.getType())
				.options(notification.getOptions()).build();

		if (config.getOptions() != null) {
			config.getOptions().put("token", token);
		}
//		CurrentUser<?> currentUser = null; // get current user from session
//		if (currentUser != null && this.channelManagerService != null
//				&& !this.channelManagerService.isChannelAccessAllowed(currentUser, config)) {
//			throw new HttpServerErrorException(HttpStatus.FORBIDDEN, "Access Not Allowed");
//		}
		this.pubNubNotification.grantAccess(config);
		notification.setId(userId);
		this.notificationAccessRepository.save(notification);
		return new ResponseEntity<>(new AccessResponseDto(null, ""), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<SuccessResponseDto> revokeAccess(@PathVariable("id") UUID userId) {

		NotificationAccess notification = this.notificationAccessRepository.findById(userId)
				.orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Token not found"));

		Config config = Config.builder().receiver(notification.getReceiver()).type(notification.getType())
				.options(notification.getOptions()).build();

		this.pubNubNotification.revokeAccess(config);
		notification.setId(userId);
		this.notificationAccessRepository.deleteById(userId);
		return new ResponseEntity<>(new SuccessResponseDto(true), HttpStatus.OK);
	}


}
