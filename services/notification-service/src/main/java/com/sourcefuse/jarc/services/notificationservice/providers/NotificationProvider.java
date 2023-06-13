package com.sourcefuse.jarc.services.notificationservice.providers;

import com.sourcefuse.jarc.core.enums.NotificationError;
import com.sourcefuse.jarc.services.notificationservice.enums.MessageType;
import com.sourcefuse.jarc.services.notificationservice.providers.email.types.EmailNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.push.types.PushNotification;
import com.sourcefuse.jarc.services.notificationservice.providers.sms.types.SmsNotification;
import com.sourcefuse.jarc.services.notificationservice.types.INotification;
import com.sourcefuse.jarc.services.notificationservice.types.Message;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NotificationProvider implements INotification {

  @Nullable
  private final EmailNotification emailNotification;

  @Nullable
  private final PushNotification pushNotification;

  @Nullable
  private final SmsNotification smsNotification;

  @Override
  public void publish(Message message) {
    if (message.getType() == MessageType.EMAIL && emailNotification != null) {
      emailNotification.publish(message);
    } else if (
      message.getType() == MessageType.PUSH && pushNotification != null
    ) {
      pushNotification.publish(message);
    } else if (
      message.getType() == MessageType.SMS && smsNotification != null
    ) {
      smsNotification.publish(message);
    } else {
      throw new ResponseStatusException(
        HttpStatus.UNPROCESSABLE_ENTITY,
        NotificationError.PROVIDER_NOT_FOUND.toString()
      );
    }
  }
}
