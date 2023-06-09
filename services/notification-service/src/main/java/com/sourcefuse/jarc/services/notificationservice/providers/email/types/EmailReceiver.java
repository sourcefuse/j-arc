package com.sourcefuse.jarc.services.notificationservice.providers.email.types;

import com.sourcefuse.jarc.services.notificationservice.types.Receiver;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import java.util.List;
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
public abstract class EmailReceiver extends Receiver {

  List<Subscriber> to;
  List<Subscriber> cc;
  Subscriber bcc;
}
