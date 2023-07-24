package com.sourcefuse.jarc.services.auditservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourcefuse.jarc.authlib.utils.JwtUtils;
import com.sourcefuse.jarc.core.adapters.LocalDateTimeTypeAdapter;
import com.sourcefuse.jarc.core.enums.AuditActions;
import com.sourcefuse.jarc.services.auditservice.constant.TestConstants;
import com.sourcefuse.jarc.services.auditservice.models.AuditLog;
import com.sourcefuse.jarc.services.auditservice.repositories.AuditLogRepository;
import com.sourcefuse.jarc.services.auditservice.test.models.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuditControllerTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AuditLogRepository auditLogRepository;

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app-jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  ObjectMapper objectMapper = new ObjectMapper();

  UUID mockUserId = UUID.fromString("db66f86d-a7e8-45b7-a6ad-8a5024158377");
  UUID mockTempRoleId = UUID.fromString("c87a2848-ce70-41a9-95c7-d7ab230ebcac");
  UUID mockUserRoleId = UUID.fromString("0836f6a1-a75e-4468-999a-fa7e61492c98");

  AuditLog auditLog1, auditLog2, auditLog;

  Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();

  private final String BASE_PATH = "/audit_logs";

  String authToken;

  @BeforeEach
  void clearUserAndAuditLog() {
    authToken =
      "Bearer " +
      JwtUtils.generateAccessToken(
        jwtSecret,
        jwtExpirationDate,
        TestConstants.createMockCurrentUser()
      );
    this.clearTables();
    this.addDefaultLogs();
  }

  @Test
  void createAuditLog_Success() throws Exception {
    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isCreated());
  }

  @Test
  void createAuditLog_FailsDueToAuthTokenNotFound() throws Exception {
    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isUnauthorized());
  }

  @Test
  void createAuditLog_FailedDueToEmptyAction() throws Exception {
    auditLog.setAction(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"action\":null", "\"action\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToWrongActionIsProvided() throws Exception {
    auditLog.setAction(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody =
      requestBody.replace("\"action\":null", "\"action\":\"SAVEALL\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToEmptyActedAt() throws Exception {
    auditLog.setActedAt(null);

    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"actedAt\":null", "\"actedAt\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToWrongActedAtIsProvided() throws Exception {
    auditLog.setActedAt(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody =
      requestBody.replace("\"action\":null", "\"actedAt\":\"12/12/2023\"");

    System.out.println(requestBody);
    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToEmptyActedOn() throws Exception {
    auditLog.setActedOn("");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToEmptyActionKey() throws Exception {
    auditLog.setActionKey("");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToEmptyEntityId() throws Exception {
    auditLog.setEntityId(null);

    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"entityId\":null", "\"entityId\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToEmptyActor() throws Exception {
    auditLog.setActor(null);

    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"actor\":null", "\"actor\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToEmptyActionGroup() throws Exception {
    auditLog.setActionGroup("");

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullAction() throws Exception {
    auditLog.setAction(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullActedAt() throws Exception {
    auditLog.setActedAt(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullActedOn() throws Exception {
    auditLog.setActedOn(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullActionKey() throws Exception {
    auditLog.setActionKey(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullEntityId() throws Exception {
    auditLog.setEntityId(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullActor() throws Exception {
    auditLog.setActor(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void createAuditLog_FailedDueToNullActionGroup() throws Exception {
    auditLog.setActionGroup(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .header("Authorization", authToken)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void getAuditLogCount_Success() throws Exception {
    this.mockMvc.perform(
        get(BASE_PATH + "/" + "/count").header("Authorization", authToken)
      )
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("2")));
  }

  @Test
  void getAuditLogCount_FailsDueToAuthTokenNotFound() throws Exception {
    this.mockMvc.perform(get(BASE_PATH + "/" + "/count"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void getAllAuditLogs_Success() throws Exception {
    MvcResult mvcResult =
      this.mockMvc.perform(get(BASE_PATH).header("Authorization", authToken))
        .andExpect(status().isOk())
        .andReturn();
    String actualResponseBody = mvcResult.getResponse().getContentAsString();

    List<AuditLog> responseAuditLogs = objectMapper.readValue(
      actualResponseBody,
      new TypeReference<List<AuditLog>>() {}
    );
    List<AuditLog> expectedResult = new ArrayList<>();
    expectedResult.add(auditLog1);
    expectedResult.add(auditLog2);

    /**
     * Need to sanitize the string json into role object
     */
    responseAuditLogs.forEach(ele -> {
      if (ele.getBefore() != null) {
        ele.setBefore(gson.fromJson((String) ele.getBefore(), Role.class));
      }
      if (ele.getAfter() != null) {
        ele.setAfter(gson.fromJson((String) ele.getAfter(), Role.class));
      }
    });
    assertEquals(
      gson.toJsonTree(responseAuditLogs),
      gson.toJsonTree(expectedResult)
    );
  }

  @Test
  void getAllAuditLogs_FailsDueToAuthTokenNotFound() throws Exception {
    this.mockMvc.perform(get(BASE_PATH)).andExpect(status().isUnauthorized());
  }

  @Test
  void getAuditLogById_Success() throws Exception {
    MvcResult mvcResult =
      this.mockMvc.perform(
          get(BASE_PATH + "/" + auditLog1.getId())
            .header("Authorization", authToken)
        )
        .andExpect(status().isOk())
        .andReturn();
    String actualResponseBody = mvcResult.getResponse().getContentAsString();

    AuditLog responseAuditLog = objectMapper.readValue(
      actualResponseBody,
      AuditLog.class
    );

    /**
     * Need to sanitize the string json into role object
     */
    if (responseAuditLog.getBefore() != null) {
      responseAuditLog.setBefore(
        gson.fromJson((String) responseAuditLog.getBefore(), Role.class)
      );
    }
    if (responseAuditLog.getAfter() != null) {
      responseAuditLog.setAfter(
        gson.fromJson((String) responseAuditLog.getAfter(), Role.class)
      );
    }

    assertEquals(gson.toJsonTree(responseAuditLog), gson.toJsonTree(auditLog1));
  }

  @Test
  void getAuditLogById_FailsDueToAuthTokenNotFound() throws Exception {
    this.mockMvc.perform(get(BASE_PATH + "/" + auditLog1.getId()))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void getAuditLogById_FailedDueToInvalidId() throws Exception {
    this.mockMvc.perform(
        get(
          BASE_PATH +
          "/" +
          UUID.fromString("fc2984b1-5ca5-4242-a522-c83b67909fb1")
        )
          .header("Authorization", authToken)
      )
      .andExpect(status().isNotFound());
  }

  void clearTables() {
    EntityManager em = entityManager
      .getEntityManagerFactory()
      .createEntityManager();
    System.out.println("reps");
    em.getTransaction().begin();
    try {
      em.createNativeQuery("TRUNCATE TABLE logs.audit_logs;").executeUpdate();
      em.getTransaction().commit();
    } catch (Exception e) {
      e.printStackTrace();
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  void addDefaultLogs() {
    this.setAuditLog();
    this.setAuditLog1();
    this.setAuditLog2();

    this.auditLogRepository.save(auditLog1);
    this.auditLogRepository.save(auditLog2);
  }

  private void setAuditLog1() {
    Role after = new Role(mockTempRoleId, "Temp", "Get");
    auditLog1 =
      new AuditLog(
        null,
        AuditActions.SAVE,
        new Date(),
        "role",
        "Role_Logs",
        mockTempRoleId,
        mockUserId,
        null,
        after,
        "Role"
      );
  }

  private void setAuditLog2() {
    Role before = new Role(mockTempRoleId, "Temp", "Get");
    Role after = new Role(mockTempRoleId, "Temp Updated", "Get");
    auditLog2 =
      new AuditLog(
        null,
        AuditActions.UPDATE,
        new Date(),
        "role",
        "Role_Logs",
        mockTempRoleId,
        mockUserId,
        before,
        after,
        "Role"
      );
  }

  private void setAuditLog() {
    Role after = new Role(mockUserRoleId, "Temp", "Get");
    auditLog =
      new AuditLog(
        null,
        AuditActions.SAVE,
        new Date(),
        "role",
        "Role_Logs",
        mockUserRoleId,
        mockUserId,
        null,
        after,
        "Role"
      );
  }
}
