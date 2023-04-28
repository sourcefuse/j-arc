package com.sourcefuse.jarc.services.auditservice.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class SoftDeleteEntity {
	
	@Column(columnDefinition = "boolean default false")
	boolean deleted;
	
	Date deletedOn;
	
	UUID deletedBy;

}