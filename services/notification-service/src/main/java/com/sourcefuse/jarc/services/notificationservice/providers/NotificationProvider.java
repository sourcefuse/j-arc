package com.sourcefuse.jarc.services.notificationservice.providers;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.enums.MessageType;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.EmailNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.INotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@NoArgsConstructor
public class NotificationProvider implements INotification {

  @Autowired(required = false)
  EmailNotification emailNotification;

  @Autowired(required = false)
  PushNotification pushNotification;

  @Autowired(required = false)
  SmsNotification smsNotification;

  @Override
  public void publish(Message message) {
    if (
      message.getType().equals(MessageType.EMAIL) && emailNotification != null
    ) {
      emailNotification.publish(message);
    } else if (
      message.getType().equals(MessageType.PUSH) && pushNotification != null
    ) {
      pushNotification.publish(message);
    } else if (
      message.getType().equals(MessageType.SMS) && smsNotification != null
    ) {
      smsNotification.publish(message);
    } else {
      throw new HttpServerErrorException(
        HttpStatus.UNPROCESSABLE_ENTITY,
        NotificationError.PROVIDER_NOT_FOUND.toString()
      );
    }
  }
}
