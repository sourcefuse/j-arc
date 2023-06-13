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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@ConditionalOnProperty(
  value = "notification.provider.push",
  havingValue = "ApnsProvider"
)
@RequiredArgsConstructor
public class ApnsProvider implements PushNotification {

  private final ApnsConnectionConfig apnsConnection;

  private static final int DEFAULT_BADGE_COUNT = 3;
  private static final int MAX_RECEIVERS = 500;
  // in milli-seconds
  private static final int EXPIRES_IN = 3600000;

  private static final String MESSAGE_FROM_KEY = "messageFrom";

  void initialValidations(Message message) {
    if (
      message.getOptions() == null ||
      message.getOptions().get(MESSAGE_FROM_KEY) == null ||
      message.getOptions().get(MESSAGE_FROM_KEY).toString().isBlank()
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.MESSAGE_FROM_NOT_FOUND.toString()
      );
    }
    if (message.getReceiver().getTo().size() == 0) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_NOT_FOUND.toString()
      );
    }
    if (message.getReceiver().getTo().size() > MAX_RECEIVERS) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_EXCEEDS_500.toString()
      );
    }
    if (message.getSubject() == null || message.getSubject().isBlank()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.MESSAGE_TITLE_NOT_FOUND.toString()
      );
    }
  }

  String getMainNote(Message message) {
    int badgeCount = Optional
      .ofNullable(apnsConnection.getBadge())
      .orElse(DEFAULT_BADGE_COUNT);

    Map<String, Object> cutomFields = new HashMap<>();
    cutomFields.put(
      MESSAGE_FROM_KEY,
      message.getOptions().get(MESSAGE_FROM_KEY)
    );

    return APNS
      .newPayload()
      .alertBody(message.getBody())
      .alertTitle(message.getSubject())
      // The category/ topic is usually bundle identifier of your app
      .category(this.apnsConnection.getTopic())
      .customFields(cutomFields)
      .badge(badgeCount)
      .build();
  }

  void sendingPushToReceiverTokens(Message message) {
    Date expiresDate = new Date();
    expiresDate.setTime(new Date().getTime() + EXPIRES_IN);

    List<Subscriber> receiverTokens = message
      .getReceiver()
      .getTo()
      .stream()
      .filter(item ->
        item.getType() == null ||
        item
          .getType()
          .toString()
          .equals(ApnsSubscriberType.REGISTRATION_TOKEN.toString())
      )
      .toList();
    if (!receiverTokens.isEmpty()) {
      List<String> tokens = receiverTokens
        .stream()
        .map(item -> item.getId())
        .toList();
      this.apnsConnection.getApnsService()
        .push(tokens, this.getMainNote(message), expiresDate);
    }
  }

  @Override
  public void publish(Message message) {
    this.initialValidations(message);
    this.sendingPushToReceiverTokens(message);
  }
}
