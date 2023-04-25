package com.sourcefuse.example.authenticationaudit.models;

import java.util.UUID;

import com.sourcefuse.jarc.services.auditservice.audit.entitylistener.AuditLogEntityListener;
import com.sourcefuse.jarc.services.auditservice.models.BaseModel;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todo", schema = "main")
@EntityListeners(AuditLogEntityListener.class)
public class Todo implements BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	String taskName;

	Boolean isDone;

}