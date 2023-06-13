package com.sourcefuse.jarc.services.notificationservice.dtos;

import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationList {

  @Valid
  List<Notification> notifications;
}
