package com.sourcefuse.jarc.services.notificationservice.types;

import com.sourcefuse.jarc.services.notificationservice.enums.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Config {

  @NotNull
  private Receiver receiver;

  @NotNull
  @Enumerated(EnumType.STRING)
  private MessageType type;

  private HashMap<String, Object> options;
}
