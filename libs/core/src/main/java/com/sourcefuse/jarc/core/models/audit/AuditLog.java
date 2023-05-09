package com.sourcefuse.jarc.core.models.audit;

import com.sourcefuse.jarc.core.constants.AuditActions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
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

  @Column(name = "action", nullable = false)
  private AuditActions action;

  @Column(name = "acted_at", nullable = false)
  @CreationTimestamp
  private Date actedAt;

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
  private String before;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private String after;
}
