package com.sourcefuse.jarc.services.authservice.models.base;

public interface BaseAuthUser<T> {
  public T getId();

  public void setId(T id);

  public String getUsername();

  public void setUsername(String username);

  public String getPassword();

  public void setPassword(String password);
}
