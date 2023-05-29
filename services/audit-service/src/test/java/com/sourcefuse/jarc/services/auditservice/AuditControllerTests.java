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
import com.sourcefuse.jarc.core.adapters.LocalDateTimeTypeAdapter;
import com.sourcefuse.jarc.core.enums.AuditActions;
import com.sourcefuse.jarc.core.enums.ContentTypes;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

  ObjectMapper objectMapper = new ObjectMapper();

  UUID mockUserId = UUID.fromString("db66f86d-a7e8-45b7-a6ad-8a5024158377");
  UUID mockTempRoleId = UUID.fromString("c87a2848-ce70-41a9-95c7-d7ab230ebcac");
  UUID mockUserRoleId = UUID.fromString("0836f6a1-a75e-4468-999a-fa7e61492c98");

  AuditLog auditLog1, auditLog2, auditLog;

  Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();

  private final String BASE_PATH = "/audit_logs";

  @BeforeEach
  void clearUserAndAuditLog() {
    this.clearTables();
    this.addDefaultLogs();
  }

  @Test
  void testCreateAuditLog() throws Exception {
    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isCreated());
  }

  @Test
  void shouldFailedDueToEmptyAction() throws Exception {
    auditLog.setAction(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"action\":null", "\"action\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToWrongActionIsProvided() throws Exception {
    auditLog.setAction(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody =
      requestBody.replace("\"action\":null", "\"action\":\"SAVEALL\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToEmptyActedAt() throws Exception {
    auditLog.setActedAt(null);

    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"actedAt\":null", "\"actedAt\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToWrongActedAtIsProvided() throws Exception {
    auditLog.setActedAt(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody =
      requestBody.replace("\"action\":null", "\"actedAt\":\"12/12/2023\"");

    System.out.println(requestBody);
    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToEmptyActedOn() throws Exception {
    auditLog.setActedOn("");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToEmptyActionKey() throws Exception {
    auditLog.setActionKey("");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToEmptyEntityId() throws Exception {
    auditLog.setEntityId(null);

    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"entityId\":null", "\"entityId\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToEmptyActor() throws Exception {
    auditLog.setActor(null);

    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody = requestBody.replace("\"actor\":null", "\"actor\":\"\"");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToEmptyActionGroup() throws Exception {
    auditLog.setActionGroup("");

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullAction() throws Exception {
    auditLog.setAction(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullActedAt() throws Exception {
    auditLog.setActedAt(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullActedOn() throws Exception {
    auditLog.setActedOn(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullActionKey() throws Exception {
    auditLog.setActionKey(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullEntityId() throws Exception {
    auditLog.setEntityId(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullActor() throws Exception {
    auditLog.setActor(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailedDueToNullActionGroup() throws Exception {
    auditLog.setActionGroup(null);

    this.mockMvc.perform(
        post(BASE_PATH)
          .contentType(ContentTypes.APPLICATION_JSON.toString())
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void testGetAuditLogCount() throws Exception {
    this.mockMvc.perform(get("/audit_logs/count"))
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("2")));
  }

  @Test
  void testGetAllAuditLogs() throws Exception {
    MvcResult mvcResult =
      this.mockMvc.perform(get(BASE_PATH))
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
     * Need to sanitize the before and after object, since this since H2 DB Don't
     * support JsonB it stores the JsonB data in stringify format
     */
    if (isH2DB()) {
      responseAuditLogs.forEach(ele -> {
        if (ele.getBefore() != null) {
          ele.setBefore(gson.fromJson((String) ele.getBefore(), Role.class));
        }
        if (ele.getAfter() != null) {
          ele.setAfter(gson.fromJson((String) ele.getAfter(), Role.class));
        }
      });
    }
    assertEquals(
      gson.toJsonTree(responseAuditLogs),
      gson.toJsonTree(expectedResult)
    );
  }

  @Test
  void testGetAuditLogById() throws Exception {
    MvcResult mvcResult =
      this.mockMvc.perform(get("/audit_logs/" + auditLog1.getId()))
        .andExpect(status().isOk())
        .andReturn();
    String actualResponseBody = mvcResult.getResponse().getContentAsString();

    AuditLog responseAuditLog = objectMapper.readValue(
      actualResponseBody,
      AuditLog.class
    );
    /**
     * Need to sanitize the before and after object, since this since H2 DB Don't
     * support JsonB it stores the JsonB data in stringify format
     */
    if (isH2DB()) {
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
    }
    assertEquals(gson.toJsonTree(responseAuditLog), gson.toJsonTree(auditLog1));
  }

  @Test
  void testGetAuditLogByIdForInvalidId() throws Exception {
    this.mockMvc.perform(
        get(
          "/audit_logs/" +
          UUID.fromString("fc2984b1-5ca5-4242-a522-c83b67909fb1")
        )
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
      em.createNativeQuery("TRUNCATE TABLE main.audit_logs;").executeUpdate();
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

  private Boolean isH2DB() {
    String databaseProductName = entityManager
      .unwrap(org.hibernate.Session.class)
      .doReturningWork(conn -> conn.getMetaData().getDatabaseProductName());
    if ("H2".equals(databaseProductName)) {
      return true;
    } else {
      return false;
    }
  }
}
