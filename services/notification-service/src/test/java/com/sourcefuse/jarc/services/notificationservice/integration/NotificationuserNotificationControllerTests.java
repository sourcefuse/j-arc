package com.sourcefuse.jarc.services.notificationservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.notificationservice.controllers.NotificationuserNotificationController;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotificationUsers;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class NotificationuserNotificationControllerTests {

  @Mock
  NotificationUserRepository notificationUserRepository;

  @InjectMocks
  NotificationuserNotificationController controller;

  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  String basePath = "/notification-users/%s/notification";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  @DisplayName("Find Notification By Notification User Id Groups - Success")
  void testFind_Success() throws Exception {
    NotificationUser notificationUser =
      MockNotificationUsers.getNotificationUser();
    Mockito
      .when(notificationUserRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.of(notificationUser));

    String url = String.format(basePath, notificationUser.getId().toString());

    MvcResult result = mockMvc
      .perform(
        MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON)
      )
      //				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();

    String actualResponseBody = result.getResponse().getContentAsString();

    Assertions.assertEquals(
      notificationUser.getNotification(),
      objectMapper.readValue(actualResponseBody, Notification.class)
    );
  }

  @Test
  @DisplayName("Find Notification By Notification User Id Groups - Not Found")
  void testFind_NotFound() throws Exception {
    Mockito
      .when(notificationUserRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(null));

    String url = String.format(
      basePath,
      MockNotificationUsers.NOTIFICATION_USER_ID.toString()
    );

    mockMvc
      .perform(MockMvcRequestBuilders.get(url))
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
