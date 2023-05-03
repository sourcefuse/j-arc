package com.sourcefuse.jarc.services.auditservice.models;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.sourcefuse.jarc.core.constants.Constants.AuditActions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "audit_logs", schema = "main")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull
	@Column(name = "action", nullable = false)
	private AuditActions action;

	@NotNull
	@Column(name = "acted_at", nullable = false)
	@CreationTimestamp
	private Date actedAt;

	@NotNull
	@Column(name = "acted_on", nullable = false)
	private String actedOn;

	@NotNull
	@Column(name = "action_key", nullable = false)
	private String actionKey;

	@NotNull
	@Column(name = "entity_id", nullable = false)
	private UUID entityId;

	@NotNull
	@Column(name = "actor", nullable = false)
	private UUID actor;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", nullable = true)
	private String before;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", nullable = true)
	private String after;

	@NotNull
	@Column(name = "action_group", nullable = false)
	private String actionGroup;

}