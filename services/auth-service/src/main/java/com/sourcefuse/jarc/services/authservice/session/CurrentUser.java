package com.sourcefuse.jarc.services.authservice.session;

import com.sourcefuse.jarc.services.authservice.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser {

  private User user;
}
