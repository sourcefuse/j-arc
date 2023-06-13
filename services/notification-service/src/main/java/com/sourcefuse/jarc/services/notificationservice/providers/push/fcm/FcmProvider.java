package com.sourcefuse.jarc.services.notificationservice.providers.push.fcm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FcmOptions;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.FcmSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.push.fcm.types.GeneralMessage;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@ConditionalOnProperty(
  value = "notification.provider.push",
  havingValue = "FcmProvider"
)
@RequiredArgsConstructor
public class FcmProvider implements PushNotification {

  private static final int MAX_RECEIVERS = 500;
  private static final String TOPIC_KEY = "topic";
  private static final String CONDITION_KEY = "condition";
  private static final String IMAGE_URL_KEY = "imageUrl";
  private static final String DRY_RUN_KEY = "dryRun";
  private static final String ANDROID_KEY = "android";
  private static final String APNS_KEY = "apns";
  private static final String FCM_OPTIONS_KEY = "fcmOptions";
  private static final String WEB_PUSH_KEY = "webPush";

  private final FcmConnectionConfig fcmConnection;

  public void initialValidations(Message message) {
    if (message.getOptions() == null) {
      message.setOptions(new HashMap<>());
    }
    if (
      message.getReceiver().getTo().isEmpty() &&
      message.getOptions().get(TOPIC_KEY) == null &&
      message.getOptions().get(CONDITION_KEY) == null
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVER_OR_TOPIC_OR_CONDITION_NOT_FOUND.toString()
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

  public List<BatchResponse> sendingPushToReceiverTokens(
    Message message,
    GeneralMessage generalMessageObj
  ) throws FirebaseMessagingException {
    List<BatchResponse> responses = new ArrayList<>();

    List<Subscriber> receiverTokens = message
      .getReceiver()
      .getTo()
      .stream()
      .filter(item ->
        item.getType() == null ||
        item
          .getType()
          .toString()
          .equals(FcmSubscriberType.REGISTRATION_TOKEN.toString())
      )
      .toList();

    if (!receiverTokens.isEmpty()) {
      List<String> tokens = receiverTokens
        .stream()
        .map(Subscriber::getId)
        .toList();

      MulticastMessage fcmMessage = MulticastMessage
        .builder()
        .addAllTokens(tokens)
        .setAndroidConfig(generalMessageObj.getAndroid())
        .setApnsConfig(generalMessageObj.getApns())
        .setFcmOptions(generalMessageObj.getFcmOptions())
        .setWebpushConfig(generalMessageObj.getWebpush())
        .setNotification(generalMessageObj.getNotification())
        .build();

      boolean dryRun = Boolean.parseBoolean(
        String.valueOf(message.getOptions().get(DRY_RUN_KEY))
      );
      responses.add(
        fcmConnection.getFirebaseMessaging().sendMulticast(fcmMessage, dryRun)
      );
    }
    return responses;
  }

  public List<String> sendingPushToTopics(
    Message message,
    GeneralMessage generalMessageObj
  ) throws FirebaseMessagingException {
    List<String> responses = new ArrayList<>();

    List<Subscriber> topics = message
      .getReceiver()
      .getTo()
      .stream()
      .filter(item ->
        item.getType() != null &&
        item.getType().toString().equals(FcmSubscriberType.FCM_TOPIC.toString())
      )
      .toList();

    if (!topics.isEmpty()) {
      for (Subscriber item : topics) {
        com.google.firebase.messaging.Message fcmMessage =
          com.google.firebase.messaging.Message
            .builder()
            .setTopic(item.getId())
            .setAndroidConfig(generalMessageObj.getAndroid())
            .setApnsConfig(generalMessageObj.getApns())
            .setFcmOptions(generalMessageObj.getFcmOptions())
            .setWebpushConfig(generalMessageObj.getWebpush())
            .setNotification(generalMessageObj.getNotification())
            .build();

        boolean dryRun = Boolean.parseBoolean(
          String.valueOf(message.getOptions().get(DRY_RUN_KEY))
        );

        responses.add(
          fcmConnection.getFirebaseMessaging().send(fcmMessage, dryRun)
        );
      }
    }
    return responses;
  }

  public List<String> sendingPushToConditions(
    Message message,
    GeneralMessage generalMessageObj
  ) throws FirebaseMessagingException {
    List<String> responses = new ArrayList<>();

    List<Subscriber> topics = message
      .getReceiver()
      .getTo()
      .stream()
      .filter(item ->
        item.getType() != null &&
        item
          .getType()
          .toString()
          .equals(FcmSubscriberType.FCM_CONDITION.toString())
      )
      .toList();

    if (!topics.isEmpty()) {
      for (Subscriber item : topics) {
        com.google.firebase.messaging.Message fcmMessage =
          com.google.firebase.messaging.Message
            .builder()
            .setCondition(item.getId())
            .setAndroidConfig(generalMessageObj.getAndroid())
            .setApnsConfig(generalMessageObj.getApns())
            .setFcmOptions(generalMessageObj.getFcmOptions())
            .setWebpushConfig(generalMessageObj.getWebpush())
            .setNotification(generalMessageObj.getNotification())
            .build();

        boolean dryRun = Boolean.parseBoolean(
          String.valueOf(message.getOptions().get(DRY_RUN_KEY))
        );
        responses.add(
          fcmConnection.getFirebaseMessaging().send(fcmMessage, dryRun)
        );
      }
    }
    return responses;
  }

  @Override
  public void publish(Message message) {
    /**
     * validating the initial request
     */
    this.initialValidations(message);

    /**
     * This method is responsible to send all the required data to mobile
     * application The mobile device will recieve push notification. Push will be
     * sent to the devices with registration token sent in receiver Notification
     * object holds title, body and imageUrl FCM message must contain 2 attributes,
     * i.e title and body
     *
     */
    String imageUrl = message.getOptions().get(IMAGE_URL_KEY) != null
      ? message.getOptions().get(IMAGE_URL_KEY).toString()
      : null;
    Notification standardNotifForFCM = Notification
      .builder()
      .setBody(message.getBody())
      .setTitle(message.getSubject())
      .setImage(imageUrl)
      .build();

    /**
     * Message attributes for all kinds of messages
     *
     * If android configurations are sent in options, it will take the precedence
     * over normal notification
     *
     */
    final ObjectMapper mapper = new ObjectMapper();
    GeneralMessage generalMessageObj = GeneralMessage
      .builder()
      .android(
        mapper.convertValue(
          message.getOptions().get(ANDROID_KEY),
          AndroidConfig.class
        )
      )
      .apns(
        mapper.convertValue(
          message.getOptions().get(APNS_KEY),
          ApnsConfig.class
        )
      )
      .fcmOptions(
        mapper.convertValue(
          message.getOptions().get(FCM_OPTIONS_KEY),
          FcmOptions.class
        )
      )
      .notification(standardNotifForFCM)
      .webpush(
        mapper.convertValue(
          message.getOptions().get(WEB_PUSH_KEY),
          WebpushConfig.class
        )
      )
      .build();
    try {
      /**
       * Sending messages for all the tokens in the request
       */

      this.sendingPushToReceiverTokens(message, generalMessageObj);

      /**
       * Sending messages for all the topics in the request
       */

      this.sendingPushToTopics(message, generalMessageObj);

      /**
       * Sending messages for all the conditions in the request
       */

      this.sendingPushToConditions(message, generalMessageObj);
    } catch (FirebaseMessagingException e) {
      log.error(e.getMessage(), e);
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        NotificationError.SOMETHING_WNET_WRONG.toString()
      );
    }
  }
}
