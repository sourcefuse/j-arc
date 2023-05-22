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
import com.sourcefuse.jarc.core.enums.AuditActions;
import com.sourcefuse.jarc.services.auditservice.models.AuditLog;
import com.sourcefuse.jarc.services.auditservice.repositories.AuditLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class AuditControllerTests {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AuditLogRepository auditLogRepository;

  ObjectMapper objectMapper = new ObjectMapper();

  UUID mockUserId = UUID.fromString("db66f86d-a7e8-45b7-a6ad-8a5024158377");

  AuditLog auditLog1, auditLog2, auditLog;

  @BeforeEach
  void clearUserAndAuditLog() {
    this.clearTables();
    this.addDefaultLogs();
  }

  @Test
  public void testCreateAuditLog() throws Exception {
    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isCreated());
  }

  @Test
  public void shouldFailedDueToEmptyAction() throws Exception {
    auditLog.setAction(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToWrongActionIsProvided() throws Exception {
    auditLog.setAction(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody =
      requestBody.replace("\"action\":null", "\"action\":\"SAVEALL\"");

    this.mockMvc.perform(
        post("/audit_logs").contentType("application/json").content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToEmptyActedAt() throws Exception {
    auditLog.setActedAt(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToWrongActedAtIsProvided() throws Exception {
    auditLog.setActedAt(null);
    String requestBody = objectMapper.writeValueAsString(auditLog);
    requestBody =
      requestBody.replace("\"action\":null", "\"actedAt\":\"12/12/2023\"");

    System.out.println(requestBody);
    this.mockMvc.perform(
        post("/audit_logs").contentType("application/json").content(requestBody)
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToEmptyActedOn() throws Exception {
    auditLog.setActedOn(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToEmptyActionKey() throws Exception {
    auditLog.setActionKey(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToEmptyEntityId() throws Exception {
    auditLog.setEntityId(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToEmptyActor() throws Exception {
    auditLog.setActor(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldFailedDueToEmptyActionGroup() throws Exception {
    auditLog.setActionGroup(null);

    this.mockMvc.perform(
        post("/audit_logs")
          .contentType("application/json")
          .content(objectMapper.writeValueAsString(auditLog))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetAuditLogCount() throws Exception {
    this.mockMvc.perform(get("/audit_logs/count"))
      .andExpect(status().isOk())
      .andExpect(content().string(equalTo("2")));
  }

  @Test
  public void testGetAllAuditLogs() throws Exception {
    MvcResult mvcResult =
      this.mockMvc.perform(get("/audit_logs"))
        .andExpect(status().isOk())
        .andReturn();
    String actualResponseBody = mvcResult.getResponse().getContentAsString();

    List<AuditLog> responseAuditLogs = objectMapper.readValue(
      actualResponseBody,
      new TypeReference<List<AuditLog>>() {}
    );

    assertEquals(
      this.sanitizeJsonbString(
          objectMapper.writeValueAsString(responseAuditLogs)
        ),
      objectMapper.writeValueAsString(Arrays.asList(auditLog1, auditLog2))
    );
  }

  @Test
  public void testGetAuditLogById() throws Exception {
    MvcResult mvcResult =
      this.mockMvc.perform(get("/audit_logs/" + auditLog1.getId()))
        .andExpect(status().isOk())
        .andReturn();
    String actualResponseBody = mvcResult.getResponse().getContentAsString();

    AuditLog responseAuditLog = objectMapper.readValue(
      actualResponseBody,
      AuditLog.class
    );

    assertEquals(
      this.sanitizeJsonbString(new Gson().toJson(responseAuditLog)),
      new Gson().toJson(auditLog1)
    );
  }

  @Test
  public void testGetAuditLogByIdForInvalidId() throws Exception {
    this.mockMvc.perform(
        get(
          "/audit_logs/" +
          UUID.fromString("fc2984b1-5ca5-4242-a522-c83b67909fb1")
        )
      )
      .andExpect(status().isNotFound());
  }

  public void clearTables() {
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

  public void addDefaultLogs() {
    this.setAuditLog();
    this.setAuditLog1();
    this.setAuditLog2();

    this.auditLogRepository.save(auditLog1);
    this.auditLogRepository.save(auditLog2);
  }

  private void setAuditLog1() {
    auditLog1 = new AuditLog();
    auditLog1.setAction(AuditActions.SAVE);
    auditLog1.setActedAt(new Date());
    auditLog1.setActedOn("role");
    auditLog1.setActionKey("Role_Logs");
    auditLog1.setBefore(null);
    auditLog1.setAfter(
      "{\"id\": \"c87a2848-ce70-41a9-95c7-d7ab230ebcac\", \"name\": \"Temp\", \"deleted\": false, \"permissons\": \"Get\"}"
    );
    auditLog1.setEntityId(
      UUID.fromString("c87a2848-ce70-41a9-95c7-d7ab230ebcac")
    );
    auditLog1.setActor(mockUserId);
    auditLog1.setActionGroup("Role");
  }

  private void setAuditLog2() {
    auditLog2 = new AuditLog();
    auditLog2.setAction(AuditActions.UPDATE);
    auditLog2.setActedAt(new Date());
    auditLog2.setActedOn("role");
    auditLog2.setActionKey("Role_Logs");
    auditLog2.setBefore(
      "{\"id\": \"c87a2848-ce70-41a9-95c7-d7ab230ebcac\", \"name\": \"Temp\", \"deleted\": false, \"permissons\": \"Get\"}"
    );
    auditLog2.setAfter(
      "{\"id\": \"c87a2848-ce70-41a9-95c7-d7ab230ebcac\", \"name\": \"Temp Updated\", \"deleted\": false, \"permissons\": \"Get\"}"
    );
    auditLog2.setEntityId(
      UUID.fromString("c87a2848-ce70-41a9-95c7-d7ab230ebcac")
    );
    auditLog2.setActor(mockUserId);
    auditLog2.setActionGroup("Role");
  }

  private void setAuditLog() {
    auditLog = new AuditLog();
    auditLog.setAction(AuditActions.SAVE);
    auditLog.setActedAt(new Date());
    auditLog.setActedOn("role");
    auditLog.setActionKey("Role_Logs");
    auditLog.setBefore(
      "{\"id\": \"c87a2848-ce70-41a9-95c7-d7ab230ebcac\", \"name\": \"Temp Updated\", \"deleted\": false, \"permissons\": \"Get\"}"
    );
    auditLog.setEntityId(
      UUID.fromString("c87a2848-ce70-41a9-95c7-d7ab230ebcac")
    );
    auditLog.setAfter(null);
    auditLog.setActor(mockUserId);
    auditLog.setActionGroup("Role");
  }

  /**
   * in case of h2 db it does not have jsonb so it format changes in jsonb column
   *
   * @throws JSONException
   */
  private String sanitizeJsonbString(String jsonB) throws JSONException {
    if (jsonB != null && this.isH2DB()) {
      jsonB = jsonB.replace("\\\\\\", "\\");
      jsonB = jsonB.replace("}\\\"", "}");
      jsonB = jsonB.replace("\\\"{", "{");
    }
    return jsonB;
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
