package com.sourcefuse.jarc.core.models.audit;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sourcefuse.jarc.core.enums.AuditActions;
import com.sourcefuse.jarc.core.serializers.JsonBDeserializer;
import com.sourcefuse.jarc.core.serializers.JsonBSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "audit_logs", schema = "logs")
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(name = "action", nullable = false)
  private AuditActions action;

  @Column(name = "acted_at", nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime actedAt;

  @Column(name = "acted_on", nullable = false)
  private String actedOn;

  @Column(name = "action_key", nullable = false)
  private String actionKey;

  @Column(name = "entity_id", nullable = false)
  private UUID entityId;

  @Column(name = "actor", nullable = false)
  private UUID actor;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  @JsonSerialize(using = JsonBSerializer.class)
  @JsonDeserialize(using = JsonBDeserializer.class)
  private Object before;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  @JsonSerialize(using = JsonBSerializer.class)
  @JsonDeserialize(using = JsonBDeserializer.class)
  private Object after;
}
