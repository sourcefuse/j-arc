package com.sourcefuse.jarc.services.notificationservice.providers.sms.sns;

import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SnsConnectionConfig;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.sns.types.SnsSubscriberType;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishRequest.Builder;

@Service
@ConditionalOnProperty(
  value = "notification.provider.sms",
  havingValue = "snsProvider"
)
@RequiredArgsConstructor
public class SnsProvider implements SmsNotification {

  private final SnsConnectionConfig snsConnection;

  private static final String SMS_TYPE_KEY = "smsType";

  @Override
  public void publish(Message message) {
    if (message.getReceiver().getTo().size() == 0) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Message receiver not found in request"
      );
    }

    for (Subscriber receiver : message.getReceiver().getTo()) {
      Builder msgBuilder = PublishRequest.builder();
      msgBuilder.message(message.getBody());
      msgBuilder.subject(message.getSubject());

      if (
        message.getOptions() != null &&
        message.getOptions().get(SMS_TYPE_KEY) != null
      ) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(
          "AWS.SNS.SMS.SMSType",
          MessageAttributeValue
            .builder()
            .dataType("String")
            .stringValue(message.getOptions().get(SMS_TYPE_KEY).toString())
            .build()
        );
        msgBuilder.messageAttributes(messageAttributes);
      }
      if (
        receiver.getType() != null &&
        receiver
          .getType()
          .toString()
          .equals(SnsSubscriberType.PHONE_NUMBER.toString())
      ) {
        msgBuilder.phoneNumber(receiver.getId());
      } else if (
        receiver.getType() != null &&
        receiver.getType().toString().equals(SnsSubscriberType.TOPIC.toString())
      ) {
        msgBuilder.topicArn(receiver.getId());
      } else {
        // Do nothing
      }

      snsConnection.getSnsClient().publish(msgBuilder.build());
    }
  }
}
