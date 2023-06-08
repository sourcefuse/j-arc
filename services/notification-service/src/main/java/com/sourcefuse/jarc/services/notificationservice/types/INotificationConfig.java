package com.sourcefuse.jarc.services.notificationservice.types;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class INotificationConfig {

  @NotNull
  private Boolean sendToMultipleReceivers;

  private String senderEmail;
}
