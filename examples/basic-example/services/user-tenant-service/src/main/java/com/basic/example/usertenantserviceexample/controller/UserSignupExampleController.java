package com.basic.example.usertenantserviceexample.controller;

import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserCredentials;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserDto;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserCredentialRepository;
import com.sourcefuse.jarc.services.usertenantservice.specifications.UserCredentialsSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@Slf4j
@RequestMapping("/user-credentials")
@RequiredArgsConstructor
public class UserSignupExampleController {

  private final UserCredentialRepository userCredentialRepository;

  @PostMapping
  public ResponseEntity<String> createUserTenants(
    @RequestBody UserDto userDto
  ) {
    AtomicBoolean isCreated = new AtomicBoolean(true);
    userCredentialRepository
      .findOne(
        UserCredentialsSpecification.byUserId(userDto.getUserDetails().getId())
      )
      .map((UserCredentials credentials) -> {
        isCreated.set(false);
        // Update existing credentials if authProvider is not KEYCLOAK
        log.info("User Credentials present ...!!");
        if (
          !CommonConstants.KEYCLOAK.equalsIgnoreCase(
            credentials.getAuthProvider()
          ) &&
          userDto.getAuthProvider().equalsIgnoreCase(CommonConstants.KEYCLOAK)
        ) {
          log.info("Updating User AuthProvider to Keycloak...!!");
          credentials.setAuthProvider(CommonConstants.KEYCLOAK);
          credentials.setAuthId(userDto.getUserDetails().getEmail());
          userCredentialRepository.save(credentials);
        }
        return credentials;
      })
      .orElseGet(() -> {
        // Create new credentials if they don't exist
        log.info("Creating User Credentials !!");
        UserCredentials newCredentials = UserCredentials.builder()
          .userId(new User(userDto.getUserDetails().getId()))
          .authProvider(CommonConstants.KEYCLOAK)
          .authId(userDto.getUserDetails().getEmail()).build();
        userCredentialRepository.save(newCredentials);
        return newCredentials;
      });

    // Return the UserCredentials object in the ResponseEntity with HTTP status 200 (OK).
    return new ResponseEntity<>(
      "success",
      isCreated.get() ? HttpStatus.CREATED : HttpStatus.OK
    );
  }
}
