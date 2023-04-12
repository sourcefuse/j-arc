package com.sourcefuse.jarc.services.authservice.models.base;

import java.util.Date;
import java.util.List;

public interface AuthUserWithPermissions<ID, TID, UTID>
    extends com.sourcefuse.jarc.services.authservice.models.base.BaseAuthUser<ID> {
  public ID getIdentifier();

  public void setIdentifier(ID identifier);

  public List<String> getPermissions();

  public void setPermissions(List<String> permissions);

  public int getAuthClientId();

  public void setAuthClientId(int authClientId);

  public String getEmail();

  public void setEmail(String email);

  public String getRole();

  public void setRole(String role);

  public String getFirstName();

  public void setFirstName(String firstName);

  public String getLastName();

  public void setLastName(String lastName);

  public String getMiddleName();

  public void setMiddleName(String middleName);

  public TID getTenantId();

  public void setTenantId(TID tenantId);

  public UTID getUserTenantId();

  public void setUserTenantId(UTID userTenantId);

  public Date getPasswordExpiryTime();

  public void setPasswordExpiryTime(Date passwordExpiryTime);

  public List<String> getAllowedResources();

  public void setAllowedResources(List<String> allowedResources);
}
