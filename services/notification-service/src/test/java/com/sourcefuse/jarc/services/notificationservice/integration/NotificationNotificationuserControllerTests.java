package com.sourcefuse.jarc.services.notificationservice.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcefuse.jarc.services.notificationservice.controllers.NotificationNotificationuserController;
import com.sourcefuse.jarc.services.notificationservice.mocks.MockNotificationUsers;
import com.sourcefuse.jarc.services.notificationservice.models.NotificationUser;
import com.sourcefuse.jarc.services.notificationservice.repositories.softdelete.NotificationUserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class NotificationNotificationuserControllerTests {

  @Mock
  NotificationUserRepository notificationUserRepository;

  @Mock
  Validator validator;

  @InjectMocks
  NotificationNotificationuserController controller;

  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  String basePath = "/notifications/%s/notification-users";

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  @DisplayName("Create Notification User in notification - Success")
  void testCreate_Success() throws Exception {
    NotificationUser notificationUser =
      MockNotificationUsers.getNotificationUserRequestBody();

    String url = String.format(
      basePath,
      MockNotificationUsers.NOTIFICATION_USER_ID
    );
    String requestBody = objectMapper.writeValueAsString(notificationUser);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(url)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(notificationUserRepository, Mockito.times(1))
      .save(Mockito.any(NotificationUser.class));
  }

  @Test
  @DisplayName(
    "Create Notification User in notification - Fails Due to null userId"
  )
  void testCreate_FailsDueToNullUserId() throws Exception {
    NotificationUser notificationUser =
      MockNotificationUsers.getNotificationUserRequestBody();
    notificationUser.setUserId(null);

    String url = String.format(
      basePath,
      MockNotificationUsers.NOTIFICATION_USER_ID
    );
    String requestBody = objectMapper.writeValueAsString(notificationUser);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .post(url)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .save(Mockito.any(NotificationUser.class));
  }

  @Test
  @DisplayName("Find Notification User By Notification Id - Success")
  void testFind_Success() throws Exception {
    NotificationUser notificationUser =
      MockNotificationUsers.getNotificationUser();

    Mockito
      .when(
        notificationUserRepository.findAll(
          ArgumentMatchers.<Specification<NotificationUser>>any()
        )
      )
      .thenReturn(Arrays.asList(notificationUser));

    String url = String.format(basePath, notificationUser.getId());

    MvcResult result = mockMvc
      .perform(
        MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(
        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();

    String actualResponseBody = result.getResponse().getContentAsString();

    Assertions.assertEquals(
      Arrays.asList(notificationUser),
      objectMapper.readValue(
        actualResponseBody,
        new TypeReference<List<NotificationUser>>() {}
      )
    );
  }

  @Test
  @DisplayName("Update Notification User in notification - Success")
  void testUpdate_Success() throws Exception {
    Mockito
      .when(
        notificationUserRepository.findAll(
          ArgumentMatchers.<Specification<NotificationUser>>any()
        )
      )
      .thenReturn(Arrays.asList(MockNotificationUsers.getNotificationUser()));

    String url = String.format(
      basePath,
      MockNotificationUsers.NOTIFICATION_USER_ID
    );
    String requestBody = objectMapper.writeValueAsString(
      MockNotificationUsers.getNotificationUserRequestBody()
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(url)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(notificationUserRepository, Mockito.times(1))
      .saveAll(ArgumentMatchers.<List<NotificationUser>>any());
  }

  @Test
  @DisplayName(
    "Update Notification User in notification - fails due to user is is null"
  )
  void testUpdate_FailsBueToUserIdIsNull() throws Exception {
    Set<ConstraintViolation<NotificationUser>> violations = new HashSet<>();

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
      .when(validator.validate(Mockito.any(NotificationUser.class)))
      .thenReturn(violations);

    Mockito
      .when(
        notificationUserRepository.findAll(
          ArgumentMatchers.<Specification<NotificationUser>>any()
        )
      )
      .thenReturn(Arrays.asList(MockNotificationUsers.getNotificationUser()));

    NotificationUser notificationUser =
      MockNotificationUsers.getNotificationUserRequestBody();
    notificationUser.setUserId(null);

    String url = String.format(
      basePath,
      MockNotificationUsers.NOTIFICATION_USER_ID
    );
    String requestBody = objectMapper.writeValueAsString(notificationUser);

    mockMvc
      .perform(
        MockMvcRequestBuilders
          .patch(url)
          .content(requestBody)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito
      .verify(notificationUserRepository, Mockito.times(0))
      .save(Mockito.any(NotificationUser.class));
  }

  @Test
  @DisplayName("Delete Notification users by Notification id - Success")
  void testDelete_NotFound() throws Exception {
    Mockito
      .when(
        notificationUserRepository.findAll(
          ArgumentMatchers.<Specification<NotificationUser>>any()
        )
      )
      .thenReturn(Arrays.asList(MockNotificationUsers.getNotificationUser()));

    String url = String.format(
      basePath,
      MockNotificationUsers.NOTIFICATION_USER_ID
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders.delete(url).accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito
      .verify(notificationUserRepository, Mockito.times(1))
      .deleteAll(ArgumentMatchers.<List<NotificationUser>>any());
  }
}
