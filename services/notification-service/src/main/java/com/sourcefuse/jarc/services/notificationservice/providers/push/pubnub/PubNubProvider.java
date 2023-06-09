package com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub;

import com.pubnub.api.PubNubException;
import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubPayloadType;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.types.PubNubSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.types.Config;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
  havingValue = "PubNubProvider"
)
@RequiredArgsConstructor
public class PubNubProvider implements PubNubNotification {

  private static final String TOKEN_KEY = "token";
  private static final String TTL_KEY = "ttl";
  private static final String ALLOW_READ_KEY = "allowRead";
  private static final String ALLOW_WRITE_KEY = "allowWrite";
  private static final String PAYLOAD_TYPE_KEY = "payloadType";
  private static final String SOUND_KEY = "sound";

  private final PubNubConnectionConfig pubnubConnection;

  private Map<String, Object> getGeneralMessageObject(Message message) {
    Map<String, Object> commonDataNotification = new HashMap<>();
    commonDataNotification.put("title", message.getSubject());
    commonDataNotification.put("description", message.getBody());

    Map<String, Object> pnGcm = new HashMap<>();
    if (
      message.getOptions().get(PAYLOAD_TYPE_KEY) != null &&
      message
        .getOptions()
        .get(PAYLOAD_TYPE_KEY)
        .toString()
        .equals(PubNubPayloadType.DATA.toString())
    ) {
      pnGcm.put("data", commonDataNotification);
    } else {
      pnGcm.put("notification", commonDataNotification);
    }

    Map<String, Object> apsData = new HashMap<>();
    apsData.put("alert", commonDataNotification);
    apsData.put("key", message.getSubject());
    apsData.put(
      SOUND_KEY,
      message.getOptions().getOrDefault(SOUND_KEY, "default").toString()
    );

    Map<String, Object> targetTypeData = new HashMap<>();
    Map<String, Object> target = new HashMap<>();

    target.put("environment", pubnubConnection.getPubNubApns2Env());
    target.put("topic", pubnubConnection.getPubNubApns2BundleId());

    targetTypeData.put("targets", Collections.singletonList(target));
    targetTypeData.put("version", "v2");

    Map<String, Object> pnApns = new HashMap<>();
    pnApns.put("aps", apsData);
    pnApns.put("pnPush", Collections.singletonList(targetTypeData));

    Map<String, Object> result = new HashMap<>();
    result.put("pnGcm", pnGcm);
    result.put("pnApns", pnApns);

    return result;
  }

  @Override
  public void publish(Message message) {
    if (message.getReceiver().getTo().isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        NotificationError.RECEIVERS_NOT_FOUND.toString()
      );
    }
    if (message.getOptions() == null) {
      message.setOptions(new HashMap<>());
    }
    Map<String, Object> generalMessageObj = getGeneralMessageObject(message);
    Map<String, Object> pubnubMessage = new HashMap<>();

    pubnubMessage.putAll(generalMessageObj);
    pubnubMessage.putAll(
      Map.of("title", message.getSubject(), "description", message.getBody())
    );

    for (Subscriber receiver : message.getReceiver().getTo()) {
      String channel = "";
      if (
        receiver.getType() != null &&
        receiver
          .getType()
          .toString()
          .equals(PubNubSubscriberType.CHANNEL.toString())
      ) {
        channel = receiver.getId();
      }
      try {
        pubnubConnection
          .getPubNub()
          .publish()
          .message(pubnubMessage)
          .channel(channel)
          .sync();
      } catch (PubNubException e) {
        log.error(e.getMessage(), e);
        throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          NotificationError.SOMETHING_WNET_WRONG.toString()
        );
      }
    }
  }

  @Override
  public Object grantAccess(Config config) {
    if (
      config.getOptions() != null &&
      config.getOptions().get(TOKEN_KEY) != null &&
      config.getOptions().get(TTL_KEY) != null
    ) {
      try {
        pubnubConnection
          .getPubNub()
          .grant()
          .authKeys(
            Collections.singletonList(
              config.getOptions().get(TOKEN_KEY).toString()
            )
          )
          .channels(
            config
              .getReceiver()
              .getTo()
              .stream()
              .map(Subscriber::getId)
              .toList()
          )
          .read(allowRead(config))
          .write(allowWrite(config))
          .ttl(Integer.valueOf(config.getOptions().get(TTL_KEY).toString()))
          .sync();
      } catch (NumberFormatException | PubNubException e) {
        log.error(e.getMessage(), e);
        throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          NotificationError.SOMETHING_WNET_WRONG.toString()
        );
      }
      Map<String, Object> result = new HashMap<>();
      result.put(TTL_KEY, config.getOptions().get(TTL_KEY));
      return result;
    }
    throw new ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      NotificationError.ATHORIZATION_TOKEN_OR_TTL_NOT_FOUND.toString()
    );
  }

  boolean allowRead(Config config) {
    return Optional
      .ofNullable(config.getOptions())
      .map(options -> options.get(ALLOW_READ_KEY))
      .map(Object::toString)
      .map(Boolean::parseBoolean)
      .orElse(true);
  }

  boolean allowWrite(Config config) {
    return Optional
      .ofNullable(config.getOptions())
      .map(options -> options.get(ALLOW_WRITE_KEY))
      .map(Object::toString)
      .map(Boolean::parseBoolean)
      .orElse(false);
  }

  @Override
  public Object revokeAccess(Config config) {
    if (
      config.getOptions() != null && config.getOptions().get(TOKEN_KEY) != null
    ) {
      try {
        pubnubConnection
          .getPubNub()
          .grant()
          .authKeys(
            Collections.singletonList(
              config.getOptions().get(TOKEN_KEY).toString()
            )
          )
          .channels(
            config
              .getReceiver()
              .getTo()
              .stream()
              .map(Subscriber::getId)
              .toList()
          )
          .read(false)
          .write(false)
          .sync();
      } catch (NumberFormatException | PubNubException e) {
        log.error(e.getMessage(), e);
        throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          NotificationError.SOMETHING_WNET_WRONG.toString()
        );
      }
      Map<String, Object> result = new HashMap<>();
      result.put(TTL_KEY, config.getOptions().get(TTL_KEY));
      return result;
    }
    throw new ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      "Authorization token not found in request"
    );
  }
}
