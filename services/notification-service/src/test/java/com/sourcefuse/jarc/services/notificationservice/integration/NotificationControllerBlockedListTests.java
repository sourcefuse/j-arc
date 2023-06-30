package com.sourcefuse.jarc.services.notificationservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.notificationservice.controllers.NotificationController;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.mocks.NotificationFilterService;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.providers.NotificationProvider;
import com.sourcefuse.jarc.services.notificationservice.repositories.simple.NotificationRepository;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import com.sourcefuse.jarc.services.notificationservice.service.NotificationUserService;
import com.sourcefuse.jarc.services.notificationservice.types.INotificationFilterFunc;
import com.sourcefuse.jarc.services.notificationservice.types.Subscriber;
import jakarta.validation.Validator;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
class NotificationControllerBlockedListTests {

  @Mock
  NotificationRepository notificationRepository;

  @Mock
  NotificationUserService notificationUserService;

  @Mock
  NotificationUserRepository notificationUserRepository;

  @Mock
  NotificationProvider notificationProvider;

  @Mock
  INotificationFilterFunc notificationFilterFunc;

  @Mock
  Validator validator;

  @InjectMocks
  NotificationController controller;

  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  String basePath = "/notifications";

  NotificationFilterService notificationFilterService =
    new NotificationFilterService();

  @Captor
  private ArgumentCaptor<Notification> objectCaptor;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  @DisplayName("Create Notification - Pass")
  void testCreate_Success() throws Exception {
    Notification notification =
      MockNotifications.notificationWithBlockedUsers();
    Mockito
      .when(notificationFilterFunc.filter(notification))
      .thenReturn(notificationFilterService.filter(notification));

    String requestBody = objectMapper.writeValueAsString(notification);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    // Verify that the save method is called with the correct object
    Mockito.verify(notificationRepository).save(objectCaptor.capture());

    // Retrieve the captured object and perform your assertions
    Notification capturedObject = objectCaptor.getValue();

    List<String> allowedUserIds = capturedObject
      .getReceiver()
      .getTo()
      .stream()
      .map(Subscriber::getId)
      .toList();
    Assertions
      .assertThat(allowedUserIds)
      .isNotEmpty()
      .doesNotContain(
        MockNotifications.BLOCKED_USER_ONE_ID.toString(),
        MockNotifications.BLOCKED_USER_TWO_ID.toString()
      );
  }
}
