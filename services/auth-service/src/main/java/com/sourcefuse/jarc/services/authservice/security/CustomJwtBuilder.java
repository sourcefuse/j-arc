package com.sourcefuse.jarc.services.authservice.security;

import com.sourcefuse.jarc.services.authservice.Utils;

import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.io.SerializationException;
import lombok.SneakyThrows;

public class CustomJwtBuilder extends DefaultJwtBuilder {
  /**
   * @deprecated
   */
  @SneakyThrows
  @Override
  @Deprecated(since = "1.11.0", forRemoval = true)
  protected byte[] toJson(Object object) throws SerializationException {
    return Utils.getObjectMapperInstance().writeValueAsBytes(object);
  }
}
