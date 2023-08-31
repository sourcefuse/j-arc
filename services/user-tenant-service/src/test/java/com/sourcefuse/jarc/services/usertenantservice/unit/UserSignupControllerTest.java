package com.sourcefuse.jarc.services.usertenantservice.unit;

import com.sourcefuse.jarc.services.usertenantservice.controller.UserSignupController;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserCredentials;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserSignupCheckDto;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthProvider;
import com.sourcefuse.jarc.services.usertenantservice.mocks.MockTenantUser;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserCredentialRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class UserSignupControllerTest {

  @InjectMocks
  private UserSignupController userSignupController;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserCredentialRepository userCredentialsRepository;

  private User user;

  private UserCredentials userCredentials;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(MockTenantUser.USER_ID);
    userCredentials = new UserCredentials();
    userCredentials.setUserId(new User(MockTenantUser.USER_ID));
    userCredentials.setAuthProvider(AuthProvider.KEY_CLOAK.toString());
  }

  @Test
  @DisplayName("Not Signed Up :User not found..!!!")
  public void testCheckUserSignupWithEmailNotFound() {
    String email = "nonexistent@example.com";
    Mockito
      .when(userRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.empty());

    ResponseEntity<UserSignupCheckDto> response =
      userSignupController.checkUserSignup(email);

    Mockito
      .verify(userRepository)
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito.verifyNoInteractions(userCredentialsRepository);

    assert response.getStatusCode() == HttpStatus.OK;
    assert !response.getBody().isSignedUp();
  }

  @Test
  @DisplayName("Signed Up :User found with AuthProvider as Internal")
  public void testCheckUserSignupWithSignedUpInternalUser() {
    String email = "internal@example.com";
    User user = new User();
    user.setId(MockTenantUser.USER_ID);
    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUserId(new User(MockTenantUser.USER_ID));
    userCredentials.setAuthProvider(AuthProvider.INTERNAL.toString());
    userCredentials.setPassword("password");

    Mockito
      .when(userRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(user));
    Mockito
      .when(
        userCredentialsRepository.findOne(
          ArgumentMatchers.any(Specification.class)
        )
      )
      .thenReturn(Optional.of(userCredentials));

    ResponseEntity<UserSignupCheckDto> response =
      userSignupController.checkUserSignup(email);

    Mockito
      .verify(userRepository)
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userCredentialsRepository)
      .findOne(ArgumentMatchers.any(Specification.class));

    assert response.getStatusCode() == HttpStatus.OK;
    assert response.getBody().isSignedUp();
  }

  @Test
  @DisplayName("Signed Up :User found with AuthProvider as Keycloak")
  public void testCheckUserSignupWithNonInternalUser() {
    String email = "keycloak@example.com";

    Mockito
      .when(userRepository.findOne(ArgumentMatchers.any(Specification.class)))
      .thenReturn(Optional.of(user));
    Mockito
      .when(
        userCredentialsRepository.findOne(
          ArgumentMatchers.any(Specification.class)
        )
      )
      .thenReturn(Optional.of(userCredentials));

    ResponseEntity<UserSignupCheckDto> response =
      userSignupController.checkUserSignup(email);

    Mockito
      .verify(userRepository)
      .findOne(ArgumentMatchers.any(Specification.class));
    Mockito
      .verify(userCredentialsRepository)
      .findOne(ArgumentMatchers.any(Specification.class));

    assert response.getStatusCode() == HttpStatus.OK;
    assert response.getBody().isSignedUp();
  }
}
