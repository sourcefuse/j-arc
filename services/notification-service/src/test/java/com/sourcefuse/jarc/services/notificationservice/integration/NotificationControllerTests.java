package com.sourcefuse.jarc.services.notificationservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.core.dtos.CountResponse;
import com.sourcefuse.jarc.services.notificationservice.controllers.NotificationController;
import com.sourcefuse.jarc.services.notificationservice.dtos.NotificationList;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotifications;
import com.sourcefuse.jarc.services.notificationservice.models.Notification;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.providers.NotificationProvider;
import com.sourcefuse.jarc.services.notificationservice.repositories.simple.NotificationRepository;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import com.sourcefuse.jarc.services.notificationservice.service.NotificationUserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
class NotificationControllerTests {

  @Mock
  NotificationRepository notificationRepository;

  @Mock
  NotificationUserService notificationUserService;

  @Mock
  NotificationUserRepository notificationUserRepository;

  @Mock
  NotificationProvider notificationProvider;

  @Mock
  Validator validator;

  @InjectMocks
  NotificationController controller;

  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  String basePath = "/notifications";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  @DisplayName("Create Notification - Success")
  void testCreate_Success() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();

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

    Mockito
      .verify(notificationRepository, Mockito.times(1))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(1))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName("Create Notification - Fails Due to null type")
  void testCreate_FailsDueToNullType() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setType(null);

    String requestBody = objectMapper.writeValueAsString(notification);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName("Create Notification - Fails Due to null receivers")
  void testCreate_FailsDueToNullReceiver() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setReceiver(null);

    String requestBody = objectMapper.writeValueAsString(notification);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName("Create Notification - Fails Due to null body")
  void testCreate_FailsDueToNullBody() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setBody(null);

    String requestBody = objectMapper.writeValueAsString(notification);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName("Create Notification - Fails Due to null sent date")
  void testCreate_FailsDueToNullSentDate() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setSentDate(null);

    String requestBody = objectMapper.writeValueAsString(notification);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName("Create Bulk Notification - Success")
  void testCreateBulkNotifications_Success() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    NotificationList notificationList = new NotificationList();
    notificationList.setNotifications(
      Arrays.asList(notification, MockNotifications.getEmailNotificationObj())
    );

    String requestBody = objectMapper.writeValueAsString(notificationList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath + "/bulk")
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(notificationRepository, Mockito.times(1))
      .saveAll(ArgumentMatchers.<List<Notification>>any());

    Mockito
      .verify(notificationUserRepository, Mockito.times(1))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName(
    "Create Bulk Notification - Fails Due to null type in one of the notification"
  )
  void testCreateBulkNotifications_FailsDueToNullType() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setType(null);
    NotificationList notificationList = new NotificationList();
    notificationList.setNotifications(
      Arrays.asList(notification, MockNotifications.getEmailNotificationObj())
    );

    String requestBody = objectMapper.writeValueAsString(notificationList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath + "/bulk")
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName(
    "Create Bulk Notification - Fails Due to null receivers in one of the notification"
  )
  void testCreateBulkNotifications_FailsDueToNullReceiver() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setReceiver(null);
    NotificationList notificationList = new NotificationList();
    notificationList.setNotifications(
      Arrays.asList(notification, MockNotifications.getEmailNotificationObj())
    );

    String requestBody = objectMapper.writeValueAsString(notificationList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath + "/bulk")
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName(
    "Create Bulk Notification - Fails Due to null body in one of the notification"
  )
  void testCreateBulkNotifications_FailsDueToNullBody() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setBody(null);
    NotificationList notificationList = new NotificationList();
    notificationList.setNotifications(
      Arrays.asList(notification, MockNotifications.getEmailNotificationObj())
    );

    String requestBody = objectMapper.writeValueAsString(notificationList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath + "/bulk")
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName(
    "Create Bulk Notification - Fails Due to null sent date in one of the notification"
  )
  void testCreateBulkNotifications_FailsDueToNullSentDate() throws Exception {
    Notification notification = MockNotifications.getEmailNotificationObj();
    notification.setSentDate(null);
    NotificationList notificationList = new NotificationList();
    notificationList.setNotifications(
      Arrays.asList(notification, MockNotifications.getEmailNotificationObj())
    );

    String requestBody = objectMapper.writeValueAsString(notificationList);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(basePath + "/bulk")
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName("Count Notification - Success")
  void testCount_Success() throws Exception {
    Mockito.when(notificationRepository.count()).thenReturn(3L);

    MvcResult result = mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/count")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();
    CountResponse resultCount = objectMapper.readValue(
      result.getResponse().getContentAsString(),
      CountResponse.class
    );

    Assertions.assertEquals(3, resultCount.getCount());

    Mockito.verify(notificationRepository, Mockito.times(1)).count();
  }

  @Test
  @DisplayName("Get all Notification - Success")
  void testFind_Success() throws Exception {
    List<Notification> notifications = new ArrayList<>();
    notifications.add(MockNotifications.dummyNotification());

    Mockito.when(notificationRepository.findAll()).thenReturn(notifications);

    MvcResult result = mockMvc
      .perform(
        MockMvcRequestBuilders.get(basePath).accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();

    Mockito.verify(notificationRepository, Mockito.times(1)).findAll();

    String actualResponseBody = result.getResponse().getContentAsString();

    Assertions.assertEquals(
      objectMapper.writeValueAsString(notifications),
      actualResponseBody
    );
  }

  @Test
  @DisplayName("Get Notification by id - Success")
  void testFindById_Success() throws Exception {
    Notification notification = MockNotifications.dummyNotification();

    Mockito
      .when(notificationRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(notification));

    MvcResult result = mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/" + notification.getId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();

    Mockito
      .verify(notificationRepository, Mockito.times(1))
      .findById(notification.getId());

    String actualResponseBody = result.getResponse().getContentAsString();

    Assertions.assertEquals(
      objectMapper.writeValueAsString(notification),
      actualResponseBody
    );
  }

  @Test
  @DisplayName("Get Notification by id - Notification not found")
  void testFindById_NotificationNotFound() throws Exception {
    Mockito
      .when(notificationRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(null));

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .get(basePath + "/" + MockNotifications.NOTIFICATION_ID.toString())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DisplayName("Update Notification by id - Success")
  void testUpdateById_Success() throws Exception {
    Mockito
      .when(notificationRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(MockNotifications.dummyNotification()));

    String requestBody = objectMapper.writeValueAsString(
      MockNotifications.getFcmNotificationObj()
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/" + MockNotifications.NOTIFICATION_ID.toString())
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(notificationRepository, Mockito.times(1))
      .save(Mockito.any(Notification.class));
  }

  @Test
  @DisplayName("Update Notification by id - fails due to type is is null")
  void testUpdateById_FailsBueToTypeIsNull() throws Exception {
    Set<ConstraintViolation<Notification>> violations = new HashSet<>();

    violations.add(
      ConstraintViolationImpl.forParameterValidation(
        "userId is null",
        null,
        null,
        "userId is null",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
      )
    );
    Mockito
      .when(validator.validate(Mockito.any(Notification.class)))
      .thenReturn(violations);

    Notification notification = MockNotifications.getFcmNotificationObj();
    notification.setType(null);

    Mockito
      .when(notificationRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(notification));

    String requestBody = objectMapper.writeValueAsString(
      MockNotifications.getFcmNotificationObj()
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/" + MockNotifications.NOTIFICATION_ID.toString())
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));
  }

  @Test
  @DisplayName("Update Notification by id - Notification not found")
  void testUpdateByIdNotificationNotFound() throws Exception {
    Mockito
      .when(notificationRepository.findById(Mockito.any(UUID.class)))
      .thenReturn(Optional.ofNullable(null));

    String requestBody = objectMapper.writeValueAsString(
      MockNotifications.getFcmNotificationObj()
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(basePath + "/" + MockNotifications.NOTIFICATION_ID.toString())
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());

    Mockito
      .verify(notificationRepository, Mockito.times(0))
      .save(Mockito.any(Notification.class));
  }

  @Test
  @DisplayName("Delete Notifications - Success")
  void testDeleteAll_Success() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders
          .delete(basePath)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
