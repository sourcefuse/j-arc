package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import com.sourcefuse.jarc.services.notificationservice.types.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class EmailMessage extends Message {
  private EmailReceiver receiver;
}
