package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.core.constants.PermissionKeyConstants;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserCredentials;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserSignupCheckDto;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserCredentialRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserCredentialsSpecification;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserSpecification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/check-signup")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserSignupController {

  private final UserCredentialRepository userCredentialRepository;
  private final UserRepository userRepository;

  @GetMapping("/{email}")
  @PreAuthorize(
    "isAuthenticated() && hasAnyAuthority('" +
    PermissionKeyConstants.VIEW_ANY_USER +
    "','" +
    PermissionKeyConstants.VIEW_TENANT_USER +
    "')"
  )
  public ResponseEntity<UserSignupCheckDto> checkUserSignup(
    @PathVariable String email
  ) {
    boolean isSignedUp = false;
    User user = userRepository
      .findOne(UserSpecification.byEmail(email))
      .orElse(null);
    if (user != null) {
      UserCredentials userCred = userCredentialRepository
        .findOne(UserCredentialsSpecification.byUserId(user.getId()))
        .orElse(null);
      if (
        userCred != null &&
        userCred
          .getAuthProvider()
          .equalsIgnoreCase(AuthProvider.INTERNAL.toString())
      ) {
        isSignedUp =
          userCred.getPassword() != null && !userCred.getPassword().isEmpty();
      } else {
        isSignedUp = true;
      }
    }
    return new ResponseEntity<>(
      UserSignupCheckDto.builder().isSignedUp(isSignedUp).build(),
      HttpStatus.OK
    );
  }
}