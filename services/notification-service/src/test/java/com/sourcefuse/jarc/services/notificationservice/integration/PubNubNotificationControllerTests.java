package com.sourcefuse.jarc.services.notificationservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.notificationservice.controllers.PubNubNotificationController;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotificationAccess;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockUserSession;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationAccess;
import com.sourcefuse.jarc.services.notificationservice.providers.push.pubnub.PubNubProvider;
import com.sourcefuse.jarc.services.notificationservice.repositories.redis.NotificationAccessRepository;
import com.sourcefuse.jarc.services.notificationservice.types.Config;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PubNubNotificationControllerTests {

  @Mock
  PubNubProvider pubNubProvider;

  @Mock
  NotificationAccessRepository notificationAccessRepository;

  @InjectMocks
  PubNubNotificationController controller;

  MockMvc mockMvc;

  String basePath = "/notifications/access/%s";

  String pubnubToken = "hjs7hbuygbhjs89bjkuyg67VGH7bhybhs";

  ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    MockUserSession.setCurrentLoggedInUser();
  }

  @Test
  @DisplayName("Grant access - Success")
  void testGrantAccess_Success() throws Exception {
    String url = String.format(
      basePath,
      MockNotifications.SUBSCRIBER_ONE_ID.toString()
    );
    String requestBody = objectMapper.writeValueAsString(
      MockNotificationAccess.getEmailNotificationObj()
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(url)
          .content(requestBody)
          .header("pubnubToken", pubnubToken)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(
        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(pubNubProvider, Mockito.times(1))
      .grantAccess(Mockito.any(Config.class));
    Mockito
      .verify(notificationAccessRepository, Mockito.times(1))
      .save(Mockito.any(NotificationAccess.class));
  }

  @Test
  @DisplayName("Grant access - Fails due to null receiver")
  void testGrantAccess_FailsDueToNullReceiver() throws Exception {
    NotificationAccess notificationAccess =
      MockNotificationAccess.getEmailNotificationObj();
    notificationAccess.setReceiver(null);

    String url = String.format(
      basePath,
      MockNotifications.SUBSCRIBER_ONE_ID.toString()
    );
    String requestBody = objectMapper.writeValueAsString(notificationAccess);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(url)
          .content(requestBody)
          .header("pubnubToken", pubnubToken)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(pubNubProvider, Mockito.times(0))
      .grantAccess(Mockito.any(Config.class));
    Mockito
      .verify(notificationAccessRepository, Mockito.times(0))
      .save(Mockito.any(NotificationAccess.class));
  }

  @Test
  @DisplayName("Grant access - Fails due to message type is null")
  void testGrantAccess_FailsDueMessageTypeIsNull() throws Exception {
    NotificationAccess notificationAccess =
      MockNotificationAccess.getEmailNotificationObj();
    notificationAccess.setType(null);

    String url = String.format(
      basePath,
      MockNotifications.SUBSCRIBER_ONE_ID.toString()
    );
    String requestBody = objectMapper.writeValueAsString(notificationAccess);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(url)
          .content(requestBody)
          .header("pubnubToken", pubnubToken)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(pubNubProvider, Mockito.times(0))
      .grantAccess(Mockito.any(Config.class));
    Mockito
      .verify(notificationAccessRepository, Mockito.times(0))
      .save(Mockito.any(NotificationAccess.class));
  }

  @Test
  @DisplayName("Revoke access - Success")
  void testRevokeAccess_Suceess() throws Exception {
    UUID userId = MockNotifications.SUBSCRIBER_ONE_ID;
    String url = String.format(basePath, userId.toString());

    NotificationAccess notificationAccess =
      MockNotificationAccess.getEmailNotificationObj();
    notificationAccess.setId(userId);

    Mockito
      .when(notificationAccessRepository.findById(userId))
      .thenReturn(Optional.ofNullable(notificationAccess));

    mockMvc
      .perform(MockMvcRequestBuilders.delete(url))
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(pubNubProvider, Mockito.times(1))
      .revokeAccess(Mockito.any(Config.class));
    Mockito
      .verify(notificationAccessRepository, Mockito.times(1))
      .deleteById(Mockito.any(UUID.class));
  }

  @Test
  @DisplayName("Revoke access - Notification access not found")
  void testRevokeAccess_NotificationAccessNotFound() throws Exception {
    UUID userId = MockNotifications.SUBSCRIBER_ONE_ID;
    String url = String.format(basePath, userId.toString());

    Mockito
      .when(notificationAccessRepository.findById(userId))
      .thenReturn(Optional.ofNullable(null));

    mockMvc
      .perform(MockMvcRequestBuilders.delete(url))
      .andExpect(MockMvcResultMatchers.status().isNotFound());

    Mockito
      .verify(pubNubProvider, Mockito.times(0))
      .revokeAccess(Mockito.any(Config.class));
    Mockito
      .verify(notificationAccessRepository, Mockito.times(0))
      .deleteById(Mockito.any(UUID.class));
  }
}
