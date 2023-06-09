package com.sourcefuse.jarc.services.notificationservice.providers.push.apns;

import com.notnoop.apns.APNS;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.apns.types.ApnsConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.apns.types.ApnsSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class ApnsProvider implements PushNotification {

  @Autowired
  ApnsConnectionConfig apnsConnection;

  private void initialValidations(Message message) {
    if (
      message.getOptions().get("messageFrom") == null ||
      message.getOptions().get("messageFrom").toString().isBlank()
    ) {
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.MESSAGE_FROM_NOT_FOUND.toString()
      );
    }
    if (message.getReceiver().getTo().size() == 0) {
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_NOT_FOUND.toString()
      );
    }
    int maxReceivers = 500;
    if (message.getReceiver().getTo().size() > maxReceivers) {
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_EXCEEDS_500.toString()
      );
    }
    if (message.getSubject() == null || message.getSubject().isBlank()) {
      throw new HttpServerErrorException(
        HttpStatus.BAD_REQUEST,
        NotificationError.MESSAGE_TITLE_NOT_FOUND.toString()
      );
    }
  }

  private String getMainNote(Message message) {
    int defaultBadgeCount = Optional
      .ofNullable(apnsConnection.getBadge())
      .orElse(3);

    Map<String, Object> cutomFields = new HashMap<>();
    cutomFields.put("messageFrom", message.getOptions().get("messageFrom"));
    cutomFields.put("messageFrom", message.getOptions().get("messageFrom"));

    String payload = APNS
      .newPayload()
      .alertBody(message.getBody())
      .alertTitle(message.getSubject())
      // The category/ topic is usually the bundle identifier of your application.
      .category(this.apnsConnection.getTopic())
      .customFields(cutomFields)
      .badge(defaultBadgeCount)
      .build();
    return payload;
  }

  @Override
  public void publish(Message message) {
    this.initialValidations(message);
    this.sendingPushToReceiverTokens(message);
  }

  private void sendingPushToReceiverTokens(Message message) {
    int expiresIn = 3600 * 1000; // milli-seconds
    Date expiresDate = new Date();
    expiresDate.setTime(new Date().getTime() + expiresIn);

    List<Subscriber> receiverTokens = message
      .getReceiver()
      .getTo()
      .stream()
      .filter(item ->
        item.getType() == null ||
        item
          .getType()
          .toString()
          .equals(ApnsSubscriberType.RegistrationToken.toString())
      )
      .toList();
    if (receiverTokens.size() >= 1) {
      List<String> tokens = receiverTokens
        .stream()
        .map(item -> item.getId())
        .toList();
      this.apnsConnection.getApnsService()
        .push(tokens, this.getMainNote(message), expiresDate);
    }
  }
}
